@echo off
echo Building Hunter Game for Windows...
echo.

REM Set JAVA_HOME to Java 17 if not already set correctly
if "%JAVA_HOME%"=="" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    echo Setting JAVA_HOME to Java 17...
) else (
    echo JAVA_HOME is set to: %JAVA_HOME%
)

REM Ensure Java 17 is in PATH
if exist "%JAVA_HOME%\bin\java.exe" (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)

cd project

echo Cleaning previous builds...
call mvn clean

echo Building JAR...
call mvn package

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Creating Windows distribution...
if not exist ..\dist mkdir ..\dist

REM Remove existing Hunter directory if it exists
if exist ..\dist\Hunter (
    echo Removing existing distribution...
    echo WARNING: If files are locked, please close:
    echo   - Any running instances of Hunter.exe
    echo   - Windows Explorer windows showing the dist\Hunter folder
    echo.
    echo Attempting to remove old distribution...
    
    REM Try to remove with retry (up to 3 attempts)
    rmdir /s /q ..\dist\Hunter 2>nul
    if exist ..\dist\Hunter (
        echo Waiting 2 seconds before retry...
        timeout /t 2 /nobreak >nul
        rmdir /s /q ..\dist\Hunter 2>nul
        if exist ..\dist\Hunter (
            echo Waiting 2 seconds before final retry...
            timeout /t 2 /nobreak >nul
            rmdir /s /q ..\dist\Hunter 2>nul
            if exist ..\dist\Hunter (
                echo.
                echo ERROR: Could not remove dist\Hunter directory!
                echo Files are locked by another process.
                echo Please close Hunter.exe and any Explorer windows, then try again.
                echo.
                pause
                exit /b 1
            )
        )
    )
    echo Old distribution removed successfully.
)

REM Try to create app-image first (doesn't require WiX)
echo Attempting to create app-image (no WiX required)...
echo Including JavaFX modules...

REM Check if JavaFX modules were copied by Maven dependency plugin
if not exist target\javafx-modules (
    echo ERROR: JavaFX modules not found in target\javafx-modules
    echo Make sure Maven dependency plugin copied them during package phase
    pause
    exit /b 1
)

echo Found JavaFX modules in target\javafx-modules

REM Final check - make sure directory doesn't exist before jpackage
if exist ..\dist\Hunter (
    echo.
    echo ERROR: dist\Hunter still exists after removal attempts!
    echo Please manually close:
    echo   1. Any running Hunter.exe processes
    echo   2. Windows Explorer windows showing dist\Hunter
    echo   3. Any other programs using files in that folder
    echo.
    echo Then delete the folder manually or restart this script.
    echo.
    pause
    exit /b 1
)

REM Build module path for jpackage (Windows path format)
set JAVAFX_MODULE_PATH=%CD%\target\javafx-modules

REM Use app icon if present (create GameIcon.ico from icons\GameIcon.png with an image editor or ImageMagick)
set ICON_ARG=
if exist icons\GameIcon.ico set ICON_ARG=--icon icons\GameIcon.ico

jpackage --input target ^
    %ICON_ARG% ^
    --name Hunter ^
    --main-jar hunter-game-1.0.0.jar ^
    --main-class HPack.HunterApp ^
    --type app-image ^
    --dest ..\dist ^
    --app-version 1.0.0 ^
    --description "Hunter Survival Game" ^
    --vendor "Hunter Game" ^
    --module-path "%JAVAFX_MODULE_PATH%" ^
    --add-modules javafx.controls,javafx.fxml,javafx.media ^
    --java-options "--add-modules javafx.controls,javafx.fxml,javafx.media" ^
    --java-options "--add-opens javafx.base/javafx.util=ALL-UNNAMED"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build complete! Application created in dist\Hunter\
    echo Run the game with: dist\Hunter\Hunter.exe
    echo.
    echo IMPORTANT: If Hunter.exe doesn't work, a launcher script is available
    echo.
    REM Create launcher script as backup
    echo Creating launcher script...
    echo @echo off > ..\dist\Hunter\Hunter-Launcher.bat
    echo cd /d "%%~dp0" >> ..\dist\Hunter\Hunter-Launcher.bat
    echo if exist "runtime\bin\java.exe" ^( >> ..\dist\Hunter\Hunter-Launcher.bat
    echo     "runtime\bin\java.exe" --add-modules javafx.controls,javafx.fxml,javafx.media --add-opens javafx.base/javafx.util=ALL-UNNAMED -jar "app\hunter-game-1.0.0.jar" >> ..\dist\Hunter\Hunter-Launcher.bat
    echo ^) else ^( >> ..\dist\Hunter\Hunter-Launcher.bat
    echo     java --add-modules javafx.controls,javafx.fxml,javafx.media --add-opens javafx.base/javafx.util=ALL-UNNAMED -jar "app\hunter-game-1.0.0.jar" >> ..\dist\Hunter\Hunter-Launcher.bat
    echo ^) >> ..\dist\Hunter\Hunter-Launcher.bat
    echo pause >> ..\dist\Hunter\Hunter-Launcher.bat
    pause
    exit /b 0
)

REM If app-image failed, try MSI (also doesn't require WiX, but might not work on all systems)
echo.
echo App-image failed, trying MSI format...
jpackage --input target ^
    %ICON_ARG% ^
    --name Hunter ^
    --main-jar hunter-game-1.0.0.jar ^
    --main-class HPack.HunterApp ^
    --type msi ^
    --dest ..\dist ^
    --app-version 1.0.0 ^
    --description "Hunter Survival Game" ^
    --vendor "Hunter Game"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build complete! MSI installer created in dist\Hunter-1.0.0.msi
    echo.
    pause
    exit /b 0
)

REM If both failed, create a simple distribution
echo.
echo jpackage failed. Creating simple distribution instead...
if not exist ..\dist\Hunter mkdir ..\dist\Hunter
if not exist ..\dist\Hunter\app mkdir ..\dist\Hunter\app

REM Copy JAR
copy target\hunter-game-1.0.0.jar ..\dist\Hunter\app\ >nul

REM Create launcher
echo @echo off > ..\dist\Hunter\Hunter-Launcher.bat
echo cd /d "%%~dp0" >> ..\dist\Hunter\Hunter-Launcher.bat
echo echo Starting Hunter Game... >> ..\dist\Hunter\Hunter-Launcher.bat
echo java --add-modules javafx.controls,javafx.fxml,javafx.media --add-opens javafx.base/javafx.util=ALL-UNNAMED -jar "app\hunter-game-1.0.0.jar" >> ..\dist\Hunter\Hunter-Launcher.bat
echo if %%ERRORLEVEL%% NEQ 0 pause >> ..\dist\Hunter\Hunter-Launcher.bat

echo.
echo Created simple distribution in dist\Hunter\
echo Run: dist\Hunter\Hunter-Launcher.bat
echo.
echo NOTE: Users need Java 17+ installed to run this.
echo.
pause
