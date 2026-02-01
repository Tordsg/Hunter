# Hunter Game

A survival game built with JavaFX where you play as a hunter trying to survive in the wilderness. Manage your health, hunger, and thirst while avoiding dangerous animals and collecting resources.

## 🎮 Game Description

Hunter is a 2D survival game where you must:
- **Survive** by managing your health, hunger, and thirst
- **Hunt** animals (rabbits and birds) using traps
- **Collect** water and food to stay alive
- **Avoid** dangerous animals that can damage you
- **Track** your progress through days and years

## 🎯 Gameplay

### Objective
Survive as long as possible by maintaining your health, hunger, and thirst levels. The game tracks your survival time in days and years.

### Controls
- **Arrow Keys** - Move the hunter (Up, Down, Left, Right)
- **Q** - Place/pick up trap
- **W** - Consume food or water (when standing on it)
- **S** - Save game
- **L** - Load game
- **P** - Pause/Resume game
- **H** - Show/Hide help menu
- **M** - Toggle music on/off

### Game Mechanics
- **Health**: Decreases when hit by animals or when hunger/thirst reach zero
- **Hunger**: Decreases over time, restore by consuming rabbit meat
- **Thirst**: Decreases over time, restore by consuming water
- **Traps**: Place traps to catch rabbits and get meat
- **Animals**: Birds and rabbits move across the screen - avoid them or trap rabbits
- **Auto-healing**: Health regenerates when hunger and thirst are above 45

## 🛠️ Requirements

- **Java 17** or higher
- **Maven 3.6+** (for building from source)

## 🚀 Running the Game

### Option 1: Using Pre-built Executables

#### Windows
1. Download the Windows build from the releases
2. Extract the ZIP file
3. Run `hunter.exe`

#### macOS
1. Download the macOS build from the releases
2. Extract the DMG file
3. Open the DMG and drag the app to Applications
4. Run the Hunter app

### Option 2: Running from Source

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Hunter
   ```

2. **Navigate to project directory**
   ```bash
   cd project
   ```

3. **Run with Maven**
   ```bash
   mvn clean javafx:run
   ```

4. **Or build and run JAR**
   ```bash
   mvn clean package
   java --module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls,javafx.fxml,javafx.media -jar target/hunter-game-1.0.0.jar
   ```

## 📦 Building Native Executables

### Quick Build (Recommended)

#### Windows
```powershell
.\build-windows.bat
```

Or use the fixed version:
```powershell
.\build-windows-fixed.bat
```

This will:
- Build the JAR with all dependencies
- Create a native Windows executable in `dist\Hunter\`
- Create a `Hunter-Launcher.bat` as a backup launcher

**To run the game after building:**
- Try `dist\Hunter\Hunter.exe` first
- If that doesn't work, use `dist\Hunter\Hunter-Launcher.bat`

#### macOS
```bash
chmod +x build-mac.sh
./build-mac.sh
```

### Manual Build

If you prefer to build manually:

1. **Build the JAR**
   ```bash
   cd project
   mvn clean package
   ```

2. **Create native executable** (requires JDK 17+ with jpackage)
   ```bash
   # Windows
   jpackage --input target --name Hunter --main-jar hunter-game-1.0.0.jar --main-class HPack.HunterApp --type app-image --dest ..\dist --app-version 1.0.0 --description "Hunter Survival Game" --vendor "Hunter Game" --java-options "--add-modules javafx.controls,javafx.fxml,javafx.media" --java-options "--add-opens javafx.base/javafx.util=ALL-UNNAMED"
   
   # macOS
   jpackage --input target --name Hunter --main-jar hunter-game-1.0.0.jar --main-class HPack.HunterApp --type dmg --dest ../dist --app-version 1.0.0 --description "Hunter Survival Game" --vendor "Hunter Game"
   ```

## 📁 Project Structure

```
Hunter/
├── project/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/HPack/     # Main game source code
│   │   │   └── resources/      # Images, sounds, FXML files
│   │   └── test/               # Unit tests
│   └── pom.xml                 # Maven configuration
├── README.md                   # This file
├── build-windows.bat           # Windows build script
└── build-mac.sh                # macOS build script
```

## 🎨 Features

- ✅ Survival mechanics (health, hunger, thirst)
- ✅ Animal AI (birds and rabbits)
- ✅ Trap system for hunting
- ✅ Save/Load game functionality
- ✅ Day/Year tracking system
- ✅ Sound effects and background music
- ✅ Help menu with game instructions
- ✅ Volume control

## 💾 Save System

- **Save Format**: JSON (human-readable, robust)
- **Save Location**: 
  - Windows: `C:\Users\<YourName>\.hunter\gameState.json`
  - macOS/Linux: `~/.hunter/gameState.json`
- **Backward Compatible**: Old CSV saves will still load automatically
- **Auto-backup**: Creates backup before each save for safety

## 🐛 Known Issues

- Game window is not resizable (by design)
- Requires Java 17+ to compile and run

## 📝 Development Notes

This project was originally created as a university assignment (TDT4100) and has been cleaned up and modernized for distribution.

### Technologies Used
- **Java 17** - Programming language
- **JavaFX 21** - GUI framework
- **Maven** - Build tool
- **JUnit 5** - Testing framework

## 📄 License

This project is provided as-is for educational and personal use.

## 🤝 Contributing

Feel free to fork this project and make improvements! Some ideas:
- Add more animal types
- Implement different difficulty levels
- Add achievements system
- Improve graphics and animations
- Add multiplayer support

## 📧 Contact

For questions or issues, please open an issue on the repository.

---

**Enjoy hunting and surviving!** 🎯
