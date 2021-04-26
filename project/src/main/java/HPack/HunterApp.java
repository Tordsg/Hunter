package HPack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HunterApp extends Application{

	public void start(final Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hunter");
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Hunter1.FXML")));
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		scene.getRoot().requestFocus();
	}
	
	public static void main(final String[] args) {
		Application.launch(args);
	}
}