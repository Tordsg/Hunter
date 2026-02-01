#!/bin/bash
# Prepare Hunter Game release for macOS.
# Run after ./build-mac.sh. Creates release/Hunter-macOS-{version}.dmg and .zip.

set -e

echo "Preparing Hunter Game Release for macOS..."
echo

# Get version from pom.xml (first <version> is the project version)
VERSION="1.0.0"
if [ -f "project/pom.xml" ]; then
  VERSION=$(sed -n 's/.*<version>\([^<]*\)<\/version>.*/\1/p' project/pom.xml | head -1)
fi

echo "Version: $VERSION"
echo

DMG_SOURCE="dist/Hunter-${VERSION}.dmg"

if [ ! -f "$DMG_SOURCE" ]; then
  echo "ERROR: $DMG_SOURCE not found!"
  echo "Please run ./build-mac.sh first to create the DMG."
  exit 1
fi

# Create release directory
mkdir -p release

# Copy DMG to release with consistent name
DMG_RELEASE="release/Hunter-macOS-${VERSION}.dmg"
cp "$DMG_SOURCE" "$DMG_RELEASE"
echo "Created $DMG_RELEASE"

# Create ZIP containing the DMG (for consistency with Windows release format)
ZIP_NAME="Hunter-macOS-${VERSION}.zip"
ZIP_PATH="release/${ZIP_NAME}"
rm -f "$ZIP_PATH"
cd release && zip -q "$ZIP_NAME" "Hunter-macOS-${VERSION}.dmg" && cd ..
echo "Created $ZIP_PATH"

# Show file sizes
echo
echo "Release assets:"
ls -lh "$DMG_RELEASE" "$ZIP_PATH" 2>/dev/null || true
echo
echo "Release package ready."
echo
echo "Next steps:"
echo "  1. Test: open $DMG_RELEASE and run the app"
echo "  2. Create a GitHub release and upload:"
echo "     - $DMG_RELEASE (or $ZIP_PATH)"
echo "  3. See RELEASE_GUIDE.md for release notes template"
echo
