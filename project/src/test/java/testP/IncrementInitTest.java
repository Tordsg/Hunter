package testP;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import HPack.*;
public class IncrementInitTest {
	@Test
	public void update() {
		IncrementInit init = new IncrementInit(1000);
		assertFalse(init.update(500));
		assertTrue(init.update(500));
		init.setIncrement(400);
		assertFalse(init.update(300));
		assertTrue(init.update(200));	}
}
