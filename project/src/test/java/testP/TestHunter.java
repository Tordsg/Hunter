package testP;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import HPack.*;
import javafx.scene.image.ImageView;


class TestGame {
	Game game = new Game();
	@Test
	void test() {
		assertTrue(true);
	} 
//	@Test
	void Hunter() {
		game.spawnPlayer();
		game.getHunter().setPosition(0, 0);
		Hunter hunter = new Hunter(0, 0, null,null);
		assertEquals(game.getHunter(), hunter);
	}
//	@Test
	void initGameObject() {
		game.initGameObject("trap", 0, 0);
		Item item = new Item();
		item.setX(0);
		item.setY(0);
		item.setType("trap");
		item.setImageView(new ImageView(), HunterController.getImages().get("trap"));
		assertEquals(game.getTrap(), item);
	}
	@Test
	void moveBird() {
		DynamicAnimal bird = new DynamicAnimal();
		bird.setType("bird");
		bird.setX(50);
		bird.setY(50);
		game.add(bird);
		assertEquals(bird.getX(),50);
		game.moveAnimal(10);
		assertEquals(bird.getX(),60);
		game.moveAnimal(700);
		assertFalse(game.getDynamicAnimals().contains(bird));
	}
	@Test
	void objectInteraction() {
		game = new Game();
		Item water = new Item();
		water.setType("water");
		water.setX(100);
		water.setY(100);
		game.add(water);
		Hunter hunter = new Hunter(100,100,null,null);
		hunter.setThirst(30);
		game.setHunter(hunter);
		assertEquals(30,game.getHunter().getThirst());
		assertTrue(game.isTrapIcon());
		game.objectInteraction();
		assertEquals(62,hunter.getThirst());
		assertFalse(game.isTrapIcon());
		assertNull(game.getTrap());
		
	}
}
