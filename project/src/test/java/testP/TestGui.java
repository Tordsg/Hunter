package testP;
import java.io.IOException;

import org.loadui.testfx.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import HPack.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class TestGui extends GuiTest{

	@Override
	protected Parent getRootNode() {
		 try {
	            return FXMLLoader.load(this.getClass().getResource("hunter1.fxml"));
	        } catch (IOException e) {
	            System.err.println(e);
	        }
	        return null;
	}
	@Test
	void imageV() {
		Image image = new Image("hunterL.png");
		Game game = new Game();
		game.initGameObject("water", 0, 0);
		Item item = new Item();
		item.setType("water");
		item.setImageView(new ImageView(), HunterController.getImages().get("water"));
		item.setX(0);
		item.setY(0);
		assertEquals(item, game.getObjects().stream().findFirst());
	}
}
