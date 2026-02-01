#!/bin/bash

echo "Building Hunter Game for macOS..."
echo

# Use Homebrew OpenJDK when Java isn't on PATH or JAVA_HOME not set (common with multi-account Macs)
if [ -z "$JAVA_HOME" ] || ! command -v java >/dev/null 2>&1; then
  for dir in /opt/homebrew/opt/openjdk /opt/homebrew/opt/openjdk@17 /opt/homebrew/opt/openjdk@21 /opt/homebrew/opt/openjdk@25; do
    if [ -x "$dir/bin/java" ]; then
      export JAVA_HOME="$dir"
      export PATH="$dir/bin:$PATH"
      echo "Using Java from: $JAVA_HOME"
      break
    fi
  done
  # Fallback: any other openjdk* in Homebrew (e.g. from brew install maven)
  if ! command -v java >/dev/null 2>&1; then
    for dir in /opt/homebrew/opt/openjdk@*/bin; do
      [ -d "$dir" ] || continue
      if [ -x "$dir/java" ]; then
        export JAVA_HOME="${dir%/bin}"
        export PATH="$dir:$PATH"
        echo "Using Java from: $JAVA_HOME"
        break
      fi
    done
  fi
fi
if ! command -v java >/dev/null 2>&1; then
  echo "ERROR: No Java runtime found. Install a JDK 17+ (e.g. brew install openjdk@17) or set JAVA_HOME."
  exit 1
fi

cd project

if ! command -v mvn >/dev/null 2>&1; then
  echo "ERROR: mvn not found. Install Maven (e.g. brew install maven) or add it to PATH."
  exit 1
fi

echo "Cleaning previous builds..."
mvn clean

echo "Building JAR (tests skipped - run 'mvn test' separately for full test suite)..."
mvn package -DskipTests

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

# Ensure JAVA_HOME is set for jpackage (same detection as at top of script)
if [ -z "$JAVA_HOME" ] || [ ! -x "$JAVA_HOME/bin/jpackage" ]; then
  for dir in /opt/homebrew/opt/openjdk /opt/homebrew/opt/openjdk@17 /opt/homebrew/opt/openjdk@21 /opt/homebrew/opt/openjdk@25; do
    if [ -x "$dir/bin/jpackage" ]; then
      export JAVA_HOME="$dir"
      export PATH="$dir/bin:$PATH"
      echo "Using JDK for jpackage: $JAVA_HOME"
      break
    fi
  done
  if [ -z "$JAVA_HOME" ]; then
    for dir in /opt/homebrew/opt/openjdk@*/bin; do
      [ -d "$dir" ] || continue
      if [ -x "$dir/jpackage" ]; then
        export JAVA_HOME="${dir%/bin}"
        export PATH="$dir:$PATH"
        echo "Using JDK for jpackage: $JAVA_HOME"
        break
      fi
    done
  fi
fi
if [ -z "$JAVA_HOME" ] || [ ! -x "$JAVA_HOME/bin/jpackage" ]; then
  echo "ERROR: No JDK with jpackage found. Set JAVA_HOME to a JDK 17+ (e.g. from brew install openjdk@17)."
  exit 1
fi

# Build module path for jpackage
JAVAFX_MODULE_PATH="$(pwd)/target/javafx-modules"

# Generate macOS app icon (.icns) from PNG if present
if [ -f "icons/GameIcon.png" ]; then
  echo "Creating app icon..."
  ICONSET="icons/Hunter.iconset"
  rm -rf "$ICONSET"
  mkdir -p "$ICONSET"
  sips -z 16 16 icons/GameIcon.png --out "$ICONSET/icon_16x16.png" >/dev/null 2>&1
  sips -z 32 32 icons/GameIcon.png --out "$ICONSET/icon_16x16@2x.png" >/dev/null 2>&1
  sips -z 32 32 icons/GameIcon.png --out "$ICONSET/icon_32x32.png" >/dev/null 2>&1
  sips -z 64 64 icons/GameIcon.png --out "$ICONSET/icon_32x32@2x.png" >/dev/null 2>&1
  sips -z 128 128 icons/GameIcon.png --out "$ICONSET/icon_128x128.png" >/dev/null 2>&1
  sips -z 256 256 icons/GameIcon.png --out "$ICONSET/icon_128x128@2x.png" >/dev/null 2>&1
  sips -z 256 256 icons/GameIcon.png --out "$ICONSET/icon_256x256.png" >/dev/null 2>&1
  sips -z 512 512 icons/GameIcon.png --out "$ICONSET/icon_256x256@2x.png" >/dev/null 2>&1
  sips -z 512 512 icons/GameIcon.png --out "$ICONSET/icon_512x512.png" >/dev/null 2>&1
  sips -z 1024 1024 icons/GameIcon.png --out "$ICONSET/icon_512x512@2x.png" >/dev/null 2>&1
  iconutil -c icns "$ICONSET" -o icons/Hunter.icns 2>/dev/null && rm -rf "$ICONSET"
fi

jpackage --input target \
    $( [ -f "icons/Hunter.icns" ] && echo "--icon $(pwd)/icons/Hunter.icns" ) \
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
