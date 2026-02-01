package testP;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import HPack.*;

public class ToFileTest {
	private String jsonPath = "src/test/resources/gameState.json";
	private String csvPath = "src/test/resources/gameState.txt";

	// Test invalid JSON files (should return false)
	@Test
	public void notLegalJSONFile() {
		ToFile toFile = new ToFile();
		// Test with invalid JSON files (non-existent or malformed)
		for(int i = 1; i < 8 ; i++) {
			String path2 = "src/test/resources/gameState" + i + ".json";
			toFile.setPath(path2);
			assertFalse(toFile.isLegalFile(), "File " + i + " should be invalid");
		}
	}
	
	// Test valid JSON files
	@Test
	public void LegalJSONFile() {
		ToFile toFile = new ToFile();
		for(int i = 8; i < 11 ; i++) {
			String path2 = "src/test/resources/gameState" + i + ".json";
			toFile.setPath(path2);
			assertTrue(toFile.isLegalFile(), "File " + i + " should be valid JSON");
		}
	}
	
	// Test backward compatibility with CSV files
	@Test
	public void LegalCSVFile() {
		ToFile toFile = new ToFile();
		for(int i = 8; i < 11 ; i++) {
			String path2 = "src/test/resources/gameState" + i + ".txt";
			toFile.setPath(path2);
			assertTrue(toFile.isLegalFile(), "CSV file " + i + " should be valid");
		}
	}
	
	// Test invalid CSV files
	@Test
	public void notLegalCSVFile() {
		ToFile toFile = new ToFile();
		for(int i = 1; i < 8 ; i++) {
			String path2 = "src/test/resources/gameState" + i + ".txt";
			toFile.setPath(path2);
			assertFalse(toFile.isLegalFile(), "CSV file " + i + " should be invalid");
		}
	}
	
	// Test JSON write and read
	@Test
	public void writeAndReadJSON() {
		List<GameObject> objs = new ArrayList<GameObject>();
		Item trap = new Item();
		trap.setType("trap");
		trap.setImage("trap");
		trap.setX(10);
		trap.setY(20);
		objs.add(trap);
		
		DynamicAnimal animal = new DynamicAnimal();
		animal.setType("bird");
		animal.setImage("birdD");
		animal.setX(100);
		animal.setY(210);
		objs.add(animal);
		
		Item water = new Item();
		water.setType("water");
		water.setImage("water");
		water.setX(200);
		water.setY(300);
		objs.add(water);
		
		ToFile toFile = new ToFile();
		toFile.setPath(jsonPath);
		
		Hunter testHunter = new Hunter(400, 400);
		testHunter.setHealth(50.0);
		testHunter.setHunger(45.0);
		testHunter.setThirst(40.0);
		testHunter.setImage("hunterR");
		
		// Write JSON
		toFile.write(objs, testHunter, 0.02, 50, 1);
		
		// Read back
		List<GameObject> readObjs = toFile.readObjects();
		assertEquals(objs.size(), readObjs.size(), "Object count should match");
		
		Hunter readHunter = toFile.readHunter();
		assertNotNull(readHunter, "Hunter should not be null");
		assertEquals(testHunter.getX(), readHunter.getX(), 0.001, "Hunter X position should match");
		assertEquals(testHunter.getY(), readHunter.getY(), 0.001, "Hunter Y position should match");
		assertEquals(testHunter.getHealth(), readHunter.getHealth(), 0.001, "Hunter health should match");
		assertEquals(testHunter.getHunger(), readHunter.getHunger(), 0.001, "Hunter hunger should match");
		assertEquals(testHunter.getThirst(), readHunter.getThirst(), 0.001, "Hunter thirst should match");
		assertEquals(testHunter.getImage(), readHunter.getImage(), "Hunter image should match");
		
		assertEquals(0.02, toFile.readTime(), 0.001, "Time should match");
		assertEquals(50, toFile.readDays(), "Days should match");
		assertEquals(1, toFile.readYears(), "Years should match");
	}
	
	// Test CSV write and read (backward compatibility)
	@Test
	public void writeAndReadCSV() {
		List<GameObject> objs = new ArrayList<GameObject>();
		Item trap = new Item();
		trap.setType("trap");
		trap.setImage("trap");
		trap.setX(10);
		trap.setY(20);
		objs.add(trap);
		
		DynamicAnimal animal = new DynamicAnimal();
		animal.setType("bird");
		animal.setImage("birdD");
		animal.setX(100);
		animal.setY(210);
		objs.add(animal);
		
		ToFile toFile = new ToFile();
		toFile.setPath(csvPath);
		
		Hunter testHunter = new Hunter(400, 400);
		
		// Write CSV
		toFile.write(objs, testHunter, 0.02, 50, 1);
		
		// Read back
		List<GameObject> readObjs = toFile.readObjects();
		assertEquals(objs.size(), readObjs.size(), "Object count should match");
		
		Hunter readHunter = toFile.readHunter();
		assertNotNull(readHunter, "Hunter should not be null");
		assertEquals(testHunter.getX(), readHunter.getX(), 0.001, "Hunter X position should match");
		assertEquals(testHunter.getY(), readHunter.getY(), 0.001, "Hunter Y position should match");
		
		assertEquals(0.02, toFile.readTime(), 0.001, "Time should match");
		assertEquals(50, toFile.readDays(), "Days should match");
		assertEquals(1, toFile.readYears(), "Years should match");
	}
}
