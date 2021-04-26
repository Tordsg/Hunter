package testP;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import HPack.*;

public class ToFileTest {
	ToFile toFile;
	String path  = "src/test/resources/gameState.txt";

	@Test
	public void notLegalFile() {
		ToFile toFile = new ToFile();
		for(int i = 1; i < 8 ; i++) {
			String path2 = "src/test/resources/gameState" + i + ".txt";
			toFile.setPath(path2);
			assertFalse(toFile.isLegalFile());
		}
	}
	@Test
	public void LegalFile() {
		ToFile toFile = new ToFile();
		for(int i = 8; i < 11 ; i++) {
			String path2 = "src/test/resources/gameState" + i + ".txt";
			toFile.setPath(path2);
			assertTrue(toFile.isLegalFile());
		}
	}
	@Test
	public void writeAndRead() {
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
		toFile.setPath(path);
		toFile.write(objs, new Hunter(400,400), 0.02, 50, 1);
		List<GameObject> readObjs = toFile.readObjects();
		assertEquals(objs.size(),readObjs.size());
		Hunter hunter = new Hunter(400,400);
		assertEquals(toFile.readHunter().getX(),hunter.getX());
		assertEquals(toFile.readHunter().getY(),hunter.getY());
		assertEquals(0.02, toFile.readTime());
		assertEquals(50, toFile.readDays());
		assertEquals(1, toFile.readYears());
		
		
	}
	
}
