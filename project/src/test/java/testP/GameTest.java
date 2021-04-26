package testP;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import HPack.*;


class GameTest {
	Game game;

	@Test
	void spawnPlayer() {
		game = new Game();
		game.spawnPlayer();
		assertTrue(game.getHunter()!=null);
	}
	@Test
	void initGameObject() {
		game = new Game();
		game.initGameObject("trap", 0, 0);
		assertTrue(game.getObjects().stream().anyMatch(o -> o.getType().equals("trap")) && game.getObjects().stream().anyMatch(o -> o.getType().equals("trapHitBox")));
		game.initGameObject("bird", 0, 0);
		assertTrue(game.getObjects().stream().anyMatch(o -> o.getType().equals("bird")) && game.getDynamicAnimals().stream().anyMatch(o -> o.getType().equals("bird")));

	}
	@Test
	void moveAnimal() {
		game = new Game();
		DynamicAnimal bird = new DynamicAnimal();
		bird.setType("bird");
		bird.setX(50);
		bird.setY(50);
		DynamicAnimal rabbit = new DynamicAnimal();
		rabbit.setType("rabbit");
		rabbit.setX(100);
		rabbit.setY(100);
		game.add(bird);
		game.add(rabbit);
		assertEquals(bird.getX(),50);
		assertEquals(rabbit.getX(),100);
		game.moveAnimal(10);
		assertEquals(bird.getX(),60);
		assertEquals(rabbit.getX(),90);
		game.moveAnimal(700);
		assertFalse(game.getDynamicAnimals().contains(bird) || game.getDynamicAnimals().contains(rabbit));
	}
	@Test
	void consume() {
		game = new Game();
		Item water = new Item();
		water.setType("water");
		water.setX(100);
		water.setY(100);
		game.initGameObject("rabbitMeat", 200, 200);
		game.add(water);
		Hunter hunter = new Hunter(100,100);
		hunter.setThirst(30);
		hunter.setHunger(40);
		game.setHunter(hunter);
		assertEquals(30,game.getHunter().getThirst());
		game.consume();
		assertNotEquals(62, hunter.getHunger());
		assertEquals(62,hunter.getThirst());
		hunter.setPosition(180, 180);
		assertTrue(game.getObjects().stream().anyMatch(o -> o.getType().equals("rabbitMeat")));
		game.consume();
		assertEquals(62, hunter.getHunger());
		assertFalse(game.getObjects().stream().anyMatch(o -> o.getType().equals("rabbitMeat")));

		
		
		
	}
	@Test
	public void trap() {
		game = new Game();
		game.spawnPlayer();
		game.getHunter().setPosition(200, 200);
		assertTrue(game.isTrapIcon());
		game.trap();
		assertFalse(game.isTrapIcon());
		assertTrue(game.getObjects().stream().anyMatch(o -> o.getType().equals("trap")) && game.getObjects().stream().anyMatch(o -> o.getType().equals("trapHitBox")));
		game.getHunter().setPosition(100, 100);
		game.trap();
		assertFalse(game.isTrapIcon());
		assertTrue(game.getObjects().stream().anyMatch(o -> o.getType().equals("trap")) && game.getObjects().stream().anyMatch(o -> o.getType().equals("trapHitBox")));
		game.getHunter().setPosition(210, 210);
		game.trap();
		assertNull(game.getTrap());
		assertTrue(game.isTrapIcon());
		assertFalse(game.getObjects().stream().anyMatch(o -> o.getType().equals("trap")) && game.getObjects().stream().anyMatch(o -> o.getType().equals("trapHitBox")));

	}
}
