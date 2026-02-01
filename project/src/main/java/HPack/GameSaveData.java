package HPack;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class representing the game save file structure in JSON format.
 */
public class GameSaveData {
    private String version = "1.0";
    private HunterData hunter;
    private GameStateData gameState;
    private List<GameObjectData> objects;
    
    // Default constructor for Gson
    public GameSaveData() {
        this.objects = new ArrayList<>();
    }
    
    public GameSaveData(HunterData hunter, GameStateData gameState, List<GameObjectData> objects) {
        this.hunter = hunter;
        this.gameState = gameState;
        this.objects = objects;
    }
    
    // Getters and setters
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public HunterData getHunter() {
        return hunter;
    }
    
    public void setHunter(HunterData hunter) {
        this.hunter = hunter;
    }
    
    public GameStateData getGameState() {
        return gameState;
    }
    
    public void setGameState(GameStateData gameState) {
        this.gameState = gameState;
    }
    
    public List<GameObjectData> getObjects() {
        return objects;
    }
    
    public void setObjects(List<GameObjectData> objects) {
        this.objects = objects;
    }
    
    /**
     * Inner class for hunter data
     */
    public static class HunterData {
        private double x;
        private double y;
        private double health;
        private double hunger;
        private double thirst;
        private String image;
        
        public HunterData() {}
        
        public HunterData(double x, double y, double health, double hunger, double thirst, String image) {
            this.x = x;
            this.y = y;
            this.health = health;
            this.hunger = hunger;
            this.thirst = thirst;
            this.image = image;
        }
        
        // Getters and setters
        public double getX() { return x; }
        public void setX(double x) { this.x = x; }
        
        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
        
        public double getHealth() { return health; }
        public void setHealth(double health) { this.health = health; }
        
        public double getHunger() { return hunger; }
        public void setHunger(double hunger) { this.hunger = hunger; }
        
        public double getThirst() { return thirst; }
        public void setThirst(double thirst) { this.thirst = thirst; }
        
        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }
    }
    
    /**
     * Inner class for game state data
     */
    public static class GameStateData {
        private double time;
        private int days;
        private int years;
        
        public GameStateData() {}
        
        public GameStateData(double time, int days, int years) {
            this.time = time;
            this.days = days;
            this.years = years;
        }
        
        // Getters and setters
        public double getTime() { return time; }
        public void setTime(double time) { this.time = time; }
        
        public int getDays() { return days; }
        public void setDays(int days) { this.days = days; }
        
        public int getYears() { return years; }
        public void setYears(int years) { this.years = years; }
    }
    
    /**
     * Inner class for game object data
     */
    public static class GameObjectData {
        private String className;
        private String type;
        private String image;
        private double x;
        private double y;
        
        public GameObjectData() {}
        
        public GameObjectData(String className, String type, String image, double x, double y) {
            this.className = className;
            this.type = type;
            this.image = image;
            this.x = x;
            this.y = y;
        }
        
        // Getters and setters
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }
        
        public double getX() { return x; }
        public void setX(double x) { this.x = x; }
        
        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
    }
}
