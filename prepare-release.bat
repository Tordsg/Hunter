@echo off
echo Preparing Hunter Game Release...
echo.

REM Check if dist\Hunter exists
if not exist dist\Hunter (
    echo ERROR: dist\Hunter not found!
    echo Please run build-windows.bat first to create the distribution.
    pause
    exit /b 1
)

REM Create release directory
if not exist release mkdir release

REM Get version from pom.xml or use default
set VERSION=1.0.0
if exist project\pom.xml (
    for /f "tokens=2 delims=<>" %%a in ('findstr /C:"<version>" project\pom.xml') do set VERSION=%%a
)

echo Version: %VERSION%
echo.

REM Create Windows ZIP
echo Creating Windows release package...
set ZIP_NAME=Hunter-Windows-%VERSION%.zip
if exist release\%ZIP_NAME% del release\%ZIP_NAME%

REM Use PowerShell to create ZIP (works on Windows 10+)
powershell -Command "Compress-Archive -Path 'dist\Hunter\*' -DestinationPath 'release\%ZIP_NAME%' -Force"

if exist release\%ZIP_NAME% (
    echo.
    echo SUCCESS: Created release\%ZIP_NAME%
    echo.
) else (
    echo.
    echo ERROR: Failed to create ZIP file
    echo Make sure you have PowerShell available
    echo.
    pause
    exit /b 1
)

REM Show file size
for %%A in (release\%ZIP_NAME%) do echo File size: %%~zA bytes

echo.
echo Release package ready: release\%ZIP_NAME%
echo.
echo Next steps:
echo 1. Test the ZIP by extracting and running Hunter.exe
echo 2. On Mac, run prepare-release.sh to create Hunter-macOS release assets
echo 3. Create a GitHub release at: https://github.com/YOUR_USERNAME/YOUR_REPO/releases/new
echo 4. Upload release\Hunter-Windows-* and release\Hunter-macOS-* as release assets
echo 5. Add release notes (see RELEASE_GUIDE.md)
echo.
pause
