#!/bin/bash

echo "Preparing Hunter Game Release for macOS..."
echo

# Check if DMG exists
if [ ! -f "dist/Hunter-1.0.0.dmg" ]; then
    echo "ERROR: dist/Hunter-1.0.0.dmg not found!"
    echo "Please run build-mac.sh first to create the distribution."
    exit 1
fi

# Create release directory
mkdir -p release

# Get version from pom.xml or use default
VERSION="1.0.0"
if [ -f "project/pom.xml" ]; then
    VERSION=$(grep -oP '<version>\K[^<]+' project/pom.xml | head -1)
fi

echo "Version: $VERSION"
echo

# Copy DMG to release folder
DMG_NAME="Hunter-macOS-${VERSION}.dmg"
cp "dist/Hunter-1.0.0.dmg" "release/${DMG_NAME}"

if [ -f "release/${DMG_NAME}" ]; then
    echo "SUCCESS: Created release/${DMG_NAME}"
    ls -lh "release/${DMG_NAME}"
    echo
else
    echo "ERROR: Failed to copy DMG file"
    exit 1
fi

echo "Release package ready: release/${DMG_NAME}"
echo
echo "Next steps:"
echo "1. Test the DMG by opening and installing"
echo "2. Create a GitHub release (see CREATE_RELEASE.md for instructions)"
echo "3. Upload release/Hunter-macOS-* and release/Hunter-Windows-* as release assets"
echo
