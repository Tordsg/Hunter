package HPack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ToFile implements ToFileInterface{
	private List<GameObject> objects = new ArrayList<GameObject>();
	private String path = Paths.get(System.getProperty("user.home"), ".hunter", "gameState.json").toString();
	private String oldPath = Paths.get(System.getProperty("user.home"), ".hunter", "gameState.txt").toString();
	private String tempPath = Paths.get(System.getProperty("user.home"), ".hunter", "gameState.tmp").toString();
	private String backupPath = Paths.get(System.getProperty("user.home"), ".hunter", "gameState.bak").toString();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public void makeFile() {
		File file = new File(path);
		try {
			file.getParentFile().mkdirs();
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch(IOException e) {
			System.err.println("Error creating save directory: " + e.getMessage());
		}
	}
	
	private void createBackup() {
		File saveFile = new File(path);
		File backupFile = new File(backupPath);
		if (saveFile.exists()) {
			try {
				Files.copy(saveFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("Warning: Could not create backup: " + e.getMessage());
			}
		}
	}
	
	public void write(List<GameObject> objs, Hunter hunter, double time, int days, int years) {
		// Create backup of existing save
		createBackup();
		
		// If path ends with .txt, write CSV format (for backward compatibility with tests)
		if (path.endsWith(".txt")) {
			writeCSV(objs, hunter, time, days, years);
			return;
		}
		
		// Build JSON save data structure
		GameSaveData saveData = new GameSaveData();
		
		// Convert hunter to JSON format
		GameSaveData.HunterData hunterData = new GameSaveData.HunterData(
			hunter.getX(),
			hunter.getY(),
			hunter.getHealth(),
			hunter.getHunger(),
			hunter.getThirst(),
			hunter.getImage()
		);
		
		// Convert game state
		GameSaveData.GameStateData gameState = new GameSaveData.GameStateData(time, days, years);
		
		// Convert game objects
		List<GameSaveData.GameObjectData> objectDataList = new ArrayList<>();
		for (GameObject o : objs) {
			if (!o.getType().equals("trapHitBox")) {
				GameSaveData.GameObjectData objData = new GameSaveData.GameObjectData(
					o.getClass().getSimpleName(),
					o.getType(),
					o.getImage(),
					o.getX(),
					o.getY()
				);
				objectDataList.add(objData);
			}
		}
		
		saveData.setHunter(hunterData);
		saveData.setGameState(gameState);
		saveData.setObjects(objectDataList);
		
		// Use atomic write: write to temp file first, then rename
		try {
			makeFile();
			File tempFile = new File(tempPath);
			tempFile.getParentFile().mkdirs();
			
			// Write JSON to temporary file using try-with-resources
			try (FileWriter writer = new FileWriter(tempPath)) {
				gson.toJson(saveData, writer);
			}
			
			// Atomic operation: replace old file with new one
			Path tempFilePath = Paths.get(tempPath);
			Path saveFilePath = Paths.get(path);
			try {
				// Try atomic move first (faster, but not supported on all file systems)
				Files.move(tempFilePath, saveFilePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException atomicEx) {
				// Fall back to regular move if atomic move not supported
				Files.move(tempFilePath, saveFilePath, StandardCopyOption.REPLACE_EXISTING);
			}
			
		} catch (IOException e) {
			System.err.println("Error saving game: " + e.getMessage());
			// Try to restore from backup if save failed
			File backupFile = new File(backupPath);
			File saveFile = new File(path);
			if (backupFile.exists() && !saveFile.exists()) {
				try {
					Files.copy(backupFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					System.err.println("Restored from backup");
				} catch (IOException restoreEx) {
					System.err.println("Could not restore from backup: " + restoreEx.getMessage());
				}
			}
		}
	}
	
	private void writeCSV(List<GameObject> objs, Hunter hunter, double time, int days, int years) {
		// Use atomic write: write to temp file first, then rename
		try {
			makeFile();
			String csvTempPath = path + ".tmp";
			File tempFile = new File(csvTempPath);
			tempFile.getParentFile().mkdirs();
			
			// Write CSV to temporary file using try-with-resources
			try (FileWriter writer = new FileWriter(csvTempPath)) {
				// Write all game objects
				for (GameObject o : objs) {
					if (!o.getType().equals("trapHitBox")) {
						writer.write(
							o.getClass().getSimpleName() + "," + 
							o.getType() + "," + 
							o.getImage() + "," + 
							o.getX() + "," + 
							o.getY() + "\n");
					}
				}
				// Write hunter data (last line)
				writer.write(
					hunter.getClass().getSimpleName() + "," + 
					hunter.getType() + "," +
					hunter.getImage() + "," + 
					hunter.getX() + "," + 
					hunter.getY() + "," + 
					days + "," + 
					years + "," + 
					hunter.getHealth() + "," + 
					hunter.getHunger() + "," + 
					hunter.getThirst() + "," + 
					time);
			}
			
			// Atomic operation: replace old file with new one
			Path tempFilePath = Paths.get(csvTempPath);
			Path saveFilePath = Paths.get(path);
			try {
				Files.move(tempFilePath, saveFilePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException atomicEx) {
				Files.move(tempFilePath, saveFilePath, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			System.err.println("Error saving CSV file: " + e.getMessage());
		}
	}
	
	public List<GameObject> readObjects() {
		objects.clear();
		
		// If path ends with .txt, read CSV format (for backward compatibility with tests)
		if (path.endsWith(".txt")) {
			return readObjectsFromCSV();
		}
		
		// Try to read from JSON file first
		File jsonFile = new File(path);
		if (jsonFile.exists()) {
			try (FileReader reader = new FileReader(jsonFile)) {
				GameSaveData saveData = gson.fromJson(reader, GameSaveData.class);
				
				if (saveData != null && saveData.getObjects() != null) {
					for (GameSaveData.GameObjectData objData : saveData.getObjects()) {
						GameObject obj = createGameObjectFromData(objData);
						if (obj != null) {
							objects.add(obj);
						}
					}
				}
				return objects;
			} catch (IOException e) {
				System.err.println("Error reading JSON save file: " + e.getMessage());
			}
		}
		
		// Fall back to old CSV format for backward compatibility
		return readObjectsFromCSV();
	}
	
	private List<GameObject> readObjectsFromCSV() {
		// Use the path that was set (could be test path or oldPath)
		File csvFile = new File(path);
		if (!csvFile.exists()) {
			// Fall back to oldPath if the set path doesn't exist
			csvFile = new File(oldPath);
			if (!csvFile.exists()) {
				return objects;
			}
		}
		
		try {
			boolean trap = false;
			try (Scanner scanner = new Scanner(csvFile)) {
				while(scanner.hasNextLine()) {
					String objInfo = scanner.nextLine();
					if (objInfo.trim().isEmpty()) continue;
					String[] line = objInfo.split(",");
					
					switch(line[0]) {
						case "Item": 
							Item item = new Item();
							item.setType(line[1]); 
							item.setImage(line[2]);
							item.setX(Double.parseDouble(line[3]));
							item.setY(Double.parseDouble(line[4]));
							if(trap && line[1].equals("trap")) break; 
							if(line[1].equals("trap")) trap = true;
							objects.add(item);
							break;
						case "DynamicAnimal": 
							DynamicAnimal dynamicAnimal = new DynamicAnimal();
							dynamicAnimal.setType(line[1]);
							dynamicAnimal.setImage(line[2]);
							dynamicAnimal.setPosition(Double.parseDouble(line[3]), Double.parseDouble(line[4]));
							objects.add(dynamicAnimal);
							break;
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error reading CSV save file: " + e.getMessage());
		}
		return objects;
	}
	
	private GameObject createGameObjectFromData(GameSaveData.GameObjectData objData) {
		switch(objData.getClassName()) {
			case "Item":
				Item item = new Item();
				item.setType(objData.getType());
				item.setImage(objData.getImage());
				item.setX(objData.getX());
				item.setY(objData.getY());
				return item;
			case "DynamicAnimal":
				DynamicAnimal animal = new DynamicAnimal();
				animal.setType(objData.getType());
				animal.setImage(objData.getImage());
				animal.setPosition(objData.getX(), objData.getY());
				return animal;
			default:
				return null;
		}
	}
	
	public Hunter readHunter() {
		// Try JSON first (if path ends with .json)
		if (path.endsWith(".json")) {
			File jsonFile = new File(path);
			if (jsonFile.exists()) {
				try (FileReader reader = new FileReader(jsonFile)) {
					GameSaveData saveData = gson.fromJson(reader, GameSaveData.class);
					if (saveData != null && saveData.getHunter() != null) {
						GameSaveData.HunterData hunterData = saveData.getHunter();
						Hunter hunter = new Hunter(hunterData.getX(), hunterData.getY());
						hunter.setHealth(hunterData.getHealth());
						hunter.setHunger(hunterData.getHunger());
						hunter.setThirst(hunterData.getThirst());
						hunter.setImage(hunterData.getImage());
						return hunter;
					}
				} catch (IOException e) {
					System.err.println("Error reading hunter from JSON: " + e.getMessage());
				}
			}
		}
		
		// Fall back to CSV
		return readHunterFromCSV();
	}
	
	private Hunter readHunterFromCSV() {
		String[] line = fetchLastLineFromCSV();
		if (line == null || line.length < 11) {
			return null;
		}
		Hunter hunter = new Hunter(Double.parseDouble(line[3]), Double.parseDouble(line[4]));
		hunter.setHealth(Double.parseDouble(line[7]));
		hunter.setHunger(Double.parseDouble(line[8]));
		hunter.setThirst(Double.parseDouble(line[9]));
		return hunter;
	}
	
	public int readDays() {
		// Try JSON first (if path ends with .json)
		if (path.endsWith(".json")) {
			File jsonFile = new File(path);
			if (jsonFile.exists()) {
				try (FileReader reader = new FileReader(jsonFile)) {
					GameSaveData saveData = gson.fromJson(reader, GameSaveData.class);
					if (saveData != null && saveData.getGameState() != null) {
						return saveData.getGameState().getDays();
					}
				} catch (IOException e) {
					System.err.println("Error reading days from JSON: " + e.getMessage());
				}
			}
		}
		
		// Fall back to CSV
		String[] line = fetchLastLineFromCSV();
		if (line != null && line.length >= 6) {
			return Integer.parseInt(line[5]);
		}
		return 0;
	}
	
	public int readYears() {
		// Try JSON first (if path ends with .json)
		if (path.endsWith(".json")) {
			File jsonFile = new File(path);
			if (jsonFile.exists()) {
				try (FileReader reader = new FileReader(jsonFile)) {
					GameSaveData saveData = gson.fromJson(reader, GameSaveData.class);
					if (saveData != null && saveData.getGameState() != null) {
						return saveData.getGameState().getYears();
					}
				} catch (IOException e) {
					System.err.println("Error reading years from JSON: " + e.getMessage());
				}
			}
		}
		
		// Fall back to CSV
		String[] line = fetchLastLineFromCSV();
		if (line != null && line.length >= 7) {
			return Integer.parseInt(line[6]);
		}
		return 0;
	}
	
	public double readTime() {
		// Try JSON first (if path ends with .json)
		if (path.endsWith(".json")) {
			File jsonFile = new File(path);
			if (jsonFile.exists()) {
				try (FileReader reader = new FileReader(jsonFile)) {
					GameSaveData saveData = gson.fromJson(reader, GameSaveData.class);
					if (saveData != null && saveData.getGameState() != null) {
						return saveData.getGameState().getTime();
					}
				} catch (IOException e) {
					System.err.println("Error reading time from JSON: " + e.getMessage());
				}
			}
		}
		
		// Fall back to CSV
		String[] line = fetchLastLineFromCSV();
		if (line != null && line.length >= 11) {
			return Double.parseDouble(line[10]);
		}
		return 0.0;
	}
	
	public boolean isLegalFile() {
		File file = new File(path);
		
		// Check if it's a JSON file (ends with .json)
		if (path.endsWith(".json")) {
			if (!file.exists()) {
				return false;
			}
			try (FileReader reader = new FileReader(file)) {
				GameSaveData saveData = gson.fromJson(reader, GameSaveData.class);
				if (saveData == null) return false;
				if (saveData.getHunter() == null) return false;
				if (saveData.getGameState() == null) return false;
				// Basic validation
				GameSaveData.HunterData hunter = saveData.getHunter();
				if (hunter.getHealth() < 0 || hunter.getHealth() > 62) return false;
				if (hunter.getHunger() < 0 || hunter.getHunger() > 62) return false;
				if (hunter.getThirst() < 0 || hunter.getThirst() > 62) return false;
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
		// Check if it's a CSV file (ends with .txt or any other extension)
		// This handles both test files and old save files
		return isLegalCSVFile();
	}
	
	private boolean isLegalCSVFile() {
		try {
			// Use the path that was set (could be test path or oldPath)
			File file = new File(path);
			if (!file.exists()) {
				// Fall back to oldPath if the set path doesn't exist
				file = new File(oldPath);
				if (!file.exists()) {
					return false;
				}
			}
			
			try (Scanner scanner = new Scanner(file)) {
				if(!scanner.hasNextLine()) return false;
				while(scanner.hasNextLine()) {
					String objInfo = scanner.nextLine();
					if (objInfo.trim().isEmpty()) continue;
					String[] line = objInfo.split(",");
					if(!islegalLine(line)) return false;
				}
			}
			
			String[] lastLine = fetchLastLineFromCSV();
			if(lastLine == null || lastLine.length == 0) return false;
			if(!lastLine[0].equals("Hunter")) return false;
			return true;
		} catch(IOException e) {
			return false;
		}
	}
	
	private boolean islegalLine(String[] line) {
		if(line.length<5) return false;
		String type = line[1];
		String image = line[2];
		double x,y,health,hunger,thirst,time;
		int days,years;
		try {
		 x = Double.parseDouble(line[3]);
		 y = Double.parseDouble(line[4]);
		}catch(NumberFormatException e) {
			return false;
		}
		switch(line[0]) {
		case "Hunter": {
			if(line.length<11) return false;
			if(!line[1].equals("hunter")) return false;
			if(!image.equals("hunterD") && !image.equals("hunterU") && !image.equals("hunterL") && !image.equals("hunterR")) return false;
			if(x<50 || x>628) return false;
			if(y<0 || y>565) return false;
			try {
				 days = Integer.parseInt(line[5]);
				 years = Integer.parseInt(line[6]);
				 health = Double.parseDouble(line[7]);
				 hunger = Double.parseDouble(line[8]);
				 thirst = Double.parseDouble(line[9]);
				 time = Double.parseDouble(line[10]);
			} catch(NumberFormatException e) {
			return false;
			}
			if(days<0 || days>364) return false;
			if(years<0) return false;
			if(health<0 || health>62) return false;
			if(hunger<0 || hunger>62) return false;
			if(thirst<0 || thirst>62) return false;
			if(time<0) return false;
			break;
		}
		case "Item": {
			if(!type.equals(image) || (!type.equals("water") && !type.equals("trap") && !type.equals("rabbitMeat"))) return false;
			if(x<50 || x>626) return false;
			if(y<0 || y>576) return false;
			break;
		}
		case "DynamicAnimal":{
			if(type.equals("bird")) {
				if(!image.equals("bird") && !image.equals("birdU") && !image.equals("bird2") && !image.equals("birdD")) return false;
			} else if(type.equals("rabbit")) {
				if(!image.equals("rabbit") && !image.equals("rabbitU") && !image.equals("rabbitD")) return false;
			} else return false;
			if(x<22 || x>650) return false;
			if(y<0 || y>572) return false;
			break;
		}
		default: return false;
		}
		return true;
	}
	
	private String[] fetchLastLineFromCSV() {
		try {
			// Use the path that was set (could be test path or oldPath)
			File file = new File(path);
			if (!file.exists()) {
				// Fall back to oldPath if the set path doesn't exist
				file = new File(oldPath);
				if (!file.exists()) {
					return null;
				}
			}
			
			String lastLine = null;
			try (Scanner scanner = new Scanner(file)) {
				while(scanner.hasNextLine()) {
					lastLine = scanner.nextLine();
				}
			}
			
			if (lastLine != null && !lastLine.trim().isEmpty()) {
				return lastLine.split(",");
			}
		} catch(IOException e) {
			System.err.println("Error reading last line from CSV: " + e.getMessage());
		}
		return null;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}
