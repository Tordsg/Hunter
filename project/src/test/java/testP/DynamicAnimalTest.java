package testP;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import HPack.*;
public class DynamicAnimalTest {

	@Test
	public void nextImages() {
		 DynamicAnimal bird = new DynamicAnimal();
		 bird.setType("bird");
		 bird.setImage("bird");
		 bird.nextImage();
		 assertEquals("birdU",bird.getImage());
		 bird.nextImage();
		 assertEquals("bird2",bird.getImage());
		 bird.nextImage();
		 assertEquals("birdD",bird.getImage());
		 bird.nextImage();
		 assertEquals("bird",bird.getImage());
		 DynamicAnimal rabbit = new DynamicAnimal();
		 rabbit.setType("rabbit");
		 rabbit.setImage("rabbit");
		 rabbit.nextImage();
		 assertEquals("rabbitU",rabbit.getImage());
		 rabbit.nextImage();
		 assertEquals("rabbitD",rabbit.getImage());
		 rabbit.nextImage();
		 assertEquals("rabbit",rabbit.getImage());	}
}
