# GitHub Release Guide

This guide will help you create a GitHub release for Hunter Game.

## Prerequisites

1. **Build the game** for both Windows and macOS:
   ```bash
   # Windows
   .\build-windows.bat
   
   # macOS (on a Mac)
   ./build-mac.sh
   ```

2. **Test the builds** to make sure they work:
   - Windows: Run `dist\Hunter\Hunter.exe`
   - macOS: Open the DMG and test the app

## Step 1: Prepare Release Assets

### Windows

Run the release preparation script:
```bash
.\prepare-release.bat
```

This will create a ZIP file in the `release/` folder: `Hunter-Windows-1.0.0.zip`

### macOS

Run the release preparation script (after `./build-mac.sh`):
```bash
./prepare-release.sh
```

This will create in the `release/` folder:
- `Hunter-macOS-1.0.0.dmg` – ready to upload to GitHub
- `Hunter-macOS-1.0.0.zip` – ZIP containing the DMG (optional)

## Step 2: Commit and Tag Your Release

1. **Commit all changes**:
   ```bash
   git add .
   git commit -m "Release v1.0.0"
   ```

2. **Create a git tag**:
   ```bash
   git tag -a v1.0.0 -m "Release version 1.0.0"
   ```

3. **Push commits and tags**:
   ```bash
   git push origin main
   git push origin v1.0.0
   ```

## Step 3: Create GitHub Release

1. **Go to your GitHub repository**
2. **Click "Releases"** in the right sidebar (or go to `https://github.com/YOUR_USERNAME/YOUR_REPO/releases`)
3. **Click "Draft a new release"**
4. **Fill in the release form**:
   - **Tag version**: Select `v1.0.0` (or type it if it doesn't exist)
   - **Release title**: `Hunter Game v1.0.0` (or similar)
   - **Description**: Use the template below

### Release Notes Template

```markdown
## 🎮 Hunter Game v1.0.0

### What's New
- Native Windows executable (no Java installation required)
- Improved save system with JSON format
- Better error handling and stability

### Downloads
- **Windows**: Download `Hunter-Windows-1.0.0.zip` and extract. Run `Hunter.exe`
- **macOS**: Download `Hunter-macOS-1.0.0.dmg` and install

### System Requirements
- **Windows**: Windows 10 or later
- **macOS**: macOS 10.15 or later
- No Java installation required (bundled with the game)

### How to Run
1. Download the appropriate file for your OS
2. Extract/Install the game
3. Run `Hunter.exe` (Windows) or `Hunter.app` (macOS)
4. If `Hunter.exe` doesn't work, try `Hunter-Launcher.bat` instead

### Game Controls
- **Arrow Keys** - Move
- **Q** - Place/pick up trap
- **W** - Consume food/water
- **S** - Save game
- **L** - Load game
- **P** - Pause/Resume
- **H** - Help menu
- **M** - Toggle music

### Known Issues
- None at this time

### Full Changelog
See [commit history](https://github.com/YOUR_USERNAME/YOUR_REPO/commits/main) for details.
```

5. **Upload release assets**:
   - Click "Attach binaries" or drag and drop
   - Upload `Hunter-Windows-1.0.0.zip`
   - Upload `Hunter-macOS-1.0.0.dmg` (if available)

6. **Click "Publish release"**

## Step 4: Update README (Optional)

Update the README.md to point to the latest release:

```markdown
## 🚀 Running the Game

### Option 1: Using Pre-built Executables

#### Windows
1. Download the latest release from [Releases](https://github.com/YOUR_USERNAME/YOUR_REPO/releases/latest)
2. Download `Hunter-Windows-X.X.X.zip`
3. Extract the ZIP file
4. Run `Hunter.exe`

#### macOS
1. Download the latest release from [Releases](https://github.com/YOUR_USERNAME/YOUR_REPO/releases/latest)
2. Download `Hunter-macOS-X.X.X.dmg`
3. Open the DMG and drag the app to Applications
4. Run the Hunter app
```

## Quick Release Checklist

- [ ] Build works on Windows
- [ ] Build works on macOS (if applicable)
- [ ] Tested the executables
- [ ] Created release ZIP files
- [ ] Committed all changes
- [ ] Created git tag
- [ ] Pushed to GitHub
- [ ] Created GitHub release
- [ ] Uploaded release assets
- [ ] Added release notes
- [ ] Updated README (optional)

## Troubleshooting

### ZIP file is too large
GitHub has a 2GB limit per file. If your release is too large:
- Check if `dist/Hunter` includes unnecessary files
- Remove test files and build artifacts
- Consider using a different compression method

### Tag already exists
If the tag already exists:
```bash
# Delete local tag
git tag -d v1.0.0

# Delete remote tag
git push origin --delete v1.0.0

# Create new tag
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

### Release assets not uploading
- Check file size (must be under 2GB)
- Try a different browser
- Clear browser cache
- Try uploading one file at a time
