#!/bin/bash
# Build, prepare release assets, and publish to GitHub Releases.
# Prereqs: ./build-mac.sh and ./prepare-release.sh (or run them from here).
# For uploading: GitHub CLI (gh) or create the release manually on GitHub.

set -e

VERSION=$(sed -n 's/.*<version>\([^<]*\)<\/version>.*/\1/p' project/pom.xml | head -1)
REPO="Tordsg/Hunter"
RELEASE_DIR="release"

echo "Hunter Game – Publish release v${VERSION}"
echo

# 1. Build Mac DMG if not present
if [ ! -f "dist/Hunter-${VERSION}.dmg" ]; then
  echo "Building macOS app..."
  ./build-mac.sh
else
  echo "Using existing dist/Hunter-${VERSION}.dmg"
fi

# 2. Prepare release assets (copy DMG + zip to release/)
echo "Preparing release assets..."
./prepare-release.sh

DMG="${RELEASE_DIR}/Hunter-macOS-${VERSION}.dmg"
ZIP="${RELEASE_DIR}/Hunter-macOS-${VERSION}.zip"

if [ ! -f "$DMG" ]; then
  echo "ERROR: $DMG not found after prepare-release.sh"
  exit 1
fi

# 3. Publish to GitHub Releases
if command -v gh >/dev/null 2>&1; then
  echo "Creating GitHub Release v${VERSION}..."
  gh release create "v${VERSION}" \
    "$DMG" "$ZIP" \
    --repo "$REPO" \
    --title "Hunter Game v${VERSION}" \
    --notes "## Hunter Game v${VERSION}

### Downloads
- **macOS**: \`Hunter-macOS-${VERSION}.dmg\` – open and drag app to Applications
- **macOS (ZIP)**: \`Hunter-macOS-${VERSION}.zip\` – contains the same DMG

### Requirements
- macOS 10.15+
- No Java installation needed (bundled)
"
  echo "Done. Release: https://github.com/${REPO}/releases/tag/v${VERSION}"
else
  echo "GitHub CLI (gh) not installed. Upload release assets manually:"
  echo "  1. Open: https://github.com/${REPO}/releases/new"
  echo "  2. Tag: v${VERSION}"
  echo "  3. Title: Hunter Game v${VERSION}"
  echo "  4. Attach: $(pwd)/${DMG}"
  echo "  5. Attach: $(pwd)/${ZIP}"
  echo ""
  echo "Or install gh and run this script again:"
  echo "  brew install gh"
  echo "  gh auth login"
fi
