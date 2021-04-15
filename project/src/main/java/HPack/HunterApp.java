package HPack;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class HunterApp extends Application{

	static boolean up = false;
	static boolean down = false;
	static boolean left = false;
	static boolean right = false;
	static boolean interact = false;
	
	public void start(final Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hunter");
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Hunter1.FXML")));
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		keyInput(scene,KeyCode.UP,KeyCode.DOWN,KeyCode.LEFT,KeyCode.RIGHT,KeyCode.SPACE);
	}
	
	public void keyInput(Scene scene, KeyCode Up, KeyCode Down, KeyCode Left, KeyCode Right, KeyCode Interact) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(Up)) up=true;
				if(event.getCode().equals(Down)) down=true;
				if(event.getCode().equals(Left)) left=true;
				if(event.getCode().equals(Right)) right=true;
				if(event.getCode().equals(Interact)) interact = true;
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(Up)) up=false;
				if(event.getCode().equals(Down)) down=false;
				if(event.getCode().equals(Left)) left=false;
				if(event.getCode().equals(Right)) right=false;
				if(event.getCode().equals(Interact)) interact = false;
			}
		});
	}

	public static void main(final String[] args) {
		Application.launch(args);
	}
}
