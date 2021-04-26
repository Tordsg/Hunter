package testP;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import HPack.*;

class GameObjectTest {
	Item item;
	DynamicAnimal animal;
	@Test
	public void isOver() {
		item = new Item();
		animal = new DynamicAnimal();
		item.setX(40);
		item.setY(40);
		animal.setPosition(60, 60);
		assertTrue(GameObject.isOver(item, animal));
	}
	@Test
	public void isNotOver() {
		item = new Item();
		animal = new DynamicAnimal();
		item.setX(40);
		item.setY(40);
		animal.setPosition(65, 65);
		assertFalse(GameObject.isOver(item, animal));
	}
}
