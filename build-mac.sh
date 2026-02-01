#!/bin/bash

echo "Building Hunter Game for macOS..."
echo

cd project

echo "Cleaning previous builds..."
mvn clean

echo "Building JAR..."
mvn package

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

echo
echo "Creating macOS app bundle..."
mkdir -p ../dist

# Check if JavaFX modules were copied by Maven dependency plugin
if [ ! -d "target/javafx-modules" ]; then
    echo "ERROR: JavaFX modules not found in target/javafx-modules"
    echo "Make sure Maven dependency plugin copied them during package phase"
    exit 1
fi

echo "Found JavaFX modules in target/javafx-modules"

# Build module path for jpackage
JAVAFX_MODULE_PATH="$(pwd)/target/javafx-modules"

jpackage --input target \
    --name Hunter \
    --main-jar hunter-game-1.0.0.jar \
    --main-class HPack.HunterApp \
    --type dmg \
    --dest ../dist \
    --mac-package-name "Hunter Game" \
    --app-version 1.0.0 \
    --description "Hunter Survival Game" \
    --vendor "Hunter Game" \
    --module-path "$JAVAFX_MODULE_PATH" \
    --add-modules javafx.controls,javafx.fxml,javafx.media \
    --java-options "--add-modules javafx.controls,javafx.fxml,javafx.media" \
    --java-options "--add-opens javafx.base/javafx.util=ALL-UNNAMED"

if [ $? -ne 0 ]; then
    echo
    echo "jpackage failed. Make sure you have JDK 17+ with jpackage installed."
    echo "You can still run the game using: mvn javafx:run"
    echo "Or use the JAR file: java -jar target/hunter-game-1.0.0.jar"
    exit 1
fi

echo
echo "Build complete! DMG file created in dist/Hunter-1.0.0.dmg"
echo
