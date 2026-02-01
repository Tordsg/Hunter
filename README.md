# Hunter Game

A survival game built with JavaFX where you play as a hunter trying to survive in the wilderness.

## 🎮 How to Play

Survive as long as possible by managing your health, hunger, and thirst. Hunt rabbits using traps to get food, collect water, and avoid dangerous animals.

### Controls
- **Arrow Keys** - Move
- **Q** - Place/pick up trap
- **W** - Consume food or water
- **S** - Save game
- **L** - Load game
- **H** - Show help menu (pauses game)
- **M** - Toggle music

### Gameplay
- **Health**: Decreases when hit by animals or when hunger/thirst reach zero
- **Hunger**: Decreases over time, restore by consuming rabbit meat
- **Thirst**: Decreases over time, restore by consuming water
- **Traps**: Place traps to catch rabbits and get meat
- **Animals**: Avoid birds and rabbits - they can damage you
- **Auto-healing**: Health regenerates when hunger and thirst are above 45

## 🚀 Running the Game

### Windows
1. Download the latest release from [Releases](https://github.com/Tordsg/Hunter/releases/tag/v1.0.0)
2. Download `Hunter-Windows-1.0.0.zip`
3. Extract and run `Hunter.exe`
4. If `Hunter.exe` doesn't work, try `Hunter-Launcher.bat`

### macOS
1. Download the latest release from [Releases](https://github.com/Tordsg/Hunter/releases/tag/v1.0.0)
2. Download `Hunter-macOS-1.0.0.dmg`
3. Install and run the app

### From Source
```bash
cd project
mvn clean javafx:run
```

## 📦 Building

### Windows
```powershell
.\build-windows.bat
```

### macOS
```bash
chmod +x build-mac.sh
./build-mac.sh
```

## 💾 Save Files

Game saves are stored in:
- **Windows**: `C:\Users\<YourName>\.hunter\gameState.json`
- **macOS/Linux**: `~/.hunter/gameState.json`

Saves are in JSON format and automatically backed up.

## 🛠️ Requirements

- **Java 17+** (for building from source)
- **Maven 3.6+** (for building from source)

## 📝 Technologies

- Java 17
- JavaFX 21
- Maven
- JUnit 5

---

**Enjoy hunting and surviving!** 🎯
