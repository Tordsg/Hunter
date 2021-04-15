package HPack;

import java.io.File;
import java.util.HashMap;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;



public class HunterController {
	@FXML
	Pane pane, gamePane, infoPane;
	@FXML
	Button Pause, help, play;
	@FXML
	Slider volumeSlider;
	@FXML
	Rectangle health, hunger, thirst;
	@FXML
	Text days, years, header;
	@FXML
	ImageView trapIcon;
	Game game = new Game(this);
	static HashMap<String, Image> images = new HashMap<String,Image>();
	static HashMap<String, MediaPlayer> sounds = new HashMap<String, MediaPlayer>();
	double volume = 0.03;
	
	@FXML
	void initialize() {
		headerUp(false);
		addMusic(volume);
		game.addPlayer();
	}
	@FXML
	void play() {
		play.setVisible(false);
		play.setDisable(true);
		Pause.setText("Pause");
		headerUp(true);
		game.start();
	}
	void gameOver() {
		headerUp(false);
		Pause.setText("Resume");
	}
	void initializeFromFile() {
//		if(!isPaused) {
//		addBackground();
//		addMusic(volume);
//		game.initFromFile();
	}
	
	@FXML
	void Pause() {
		if(Pause.getText().equals("Pause")) {
			Pause.setText("Resume");
			game.pauseTimer();
			game.toFile.write();
		} else {
			Pause.setText("Pause");
			initializeFromFile();
			game.resumeTimer();
		}
	}

	@FXML
	void help() {
		if(infoPane.isVisible()) {
			if(!Pause.getText().equals("Resume")) {
				game.timer.resume();
				game.movement.start();
			}
			infoPane.setVisible(false);
			infoPane.setDisable(true);
		} else {
			infoPane.setVisible(true);
			infoPane.setDisable(false);
			infoPane.toFront();
			if(!Pause.getText().equals("Resume")) {
			game.timer.pause();
			game.movement.stop();
			}
		}
	}
	@FXML
	void setSliderVolume() {
		if(!volumeSlider.isDisabled()) {
		double value = volumeSlider.getValue();
		setMediaVolume(value);
		volume = value;
		}
	}
	@FXML
	void enableSlider() {
		if(volumeSlider.isDisable()) {
			volumeSlider.setDisable(false);
			volumeSlider.setVisible(true);
			volumeSlider.adjustValue(volume);
		} else {
			volumeSlider.setDisable(true);
			volumeSlider.setVisible(false);
		}
	}
	@FXML
	public void disableFocus() {
		volumeSlider.setDisable(true);
		volumeSlider.setVisible(false);
	}
	public void headerUp(boolean direction){
		AnimationTimer up = new AnimationTimer() {
			long last = System.nanoTime();
			long delta;
			@Override
			public void handle(long now) {
				delta = now-last;
				last = now;
				if(direction) {
					if(header.getLayoutY()<-200) {
						this.stop();
						header.setVisible(false);
						header.setDisable(true);
					}
					header.setLayoutY(header.getLayoutY()-delta/2e6);
				}
				else {
					header.setVisible(true);
					header.setDisable(false);
					if(header.getLayoutY()>=175) {
						play.setVisible(true);
						play.setDisable(false);
						this.stop();
					}
					header.setLayoutY(header.getLayoutY()+delta/6e6);
				}
			}
		};
		up.start();
	}
	void nextImages(String type) {
		for(DynamicAnimal obj : game.getDynamicAnimals()) {
			if(obj.getType().equals(type)) {
				if(obj.getImageView().getImage().equals(images.get(obj.getType()))) {
					obj.getImageView().setImage(images.get(obj.getType()+"U"));
				} else if (obj.getImageView().getImage().equals(images.get(obj.getType() + "U"))){
					if(obj.getType().equals("rabbit")) {
						obj.getImageView().setImage(images.get(obj.getType()+"D"));
					}else {
						obj.getImageView().setImage(images.get(obj.getType()+"2"));
					}
				} else if(obj.getImageView().getImage().equals(images.get(obj.getType() + "2"))){
					obj.getImageView().setImage(images.get(obj.getType()+"D"));
				}else if(obj.getImageView().getImage().equals(images.get(obj.getType()+"D"))){
					obj.getImageView().setImage(images.get(obj.getType()));
				}
			}
		}
	}
	public void setDays(String days) {
		this.days.setText(days);
	}
	static {
		images.put("trap", new Image("trap.png"));
		images.put("water",new Image("water.png"));
		images.put("birdU",new Image("birdU.png"));
		images.put("bird",new Image("bird.png"));
		images.put("bird2",new Image("bird2.png"));
		images.put("birdD",new Image("birdD.png"));
		images.put("hunterD",new Image("hunterD.png"));
		images.put("hunterU",new Image("hunterU.png"));
		images.put("hunterL",new Image("hunterL.png"));
		images.put("hunterR",new Image("hunterR.png"));
		images.put("rabbitU",new Image("rabbitU.png"));
		images.put("rabbit",new Image("rabbit.png"));
		images.put("rabbitD",new Image("rabbitD.png"));
		images.put("rabbitMeat",new Image("rabbitMeat.png"));
		
		sounds.put("music",new MediaPlayer(new Media(new File("src/main/resources/Retro_lyder.mp3").toURI().toString())));
		sounds.put("gameOver", new MediaPlayer(new Media(new File("src/main/resources/gameOver.wav").toURI().toString())));
		sounds.put("heal", new MediaPlayer(new Media(new File("src/main/resources/heal.mp3").toURI().toString())));
		sounds.put("hit",new MediaPlayer(new Media(new File("src/main/resources/hit.wav").toURI().toString())));
	}
	public void addMusic(double volume) {
		sounds.forEach((k,v) -> v.setVolume(volume));
		MediaPlayer music = sounds.get("music");
		music.setCycleCount(MediaPlayer.INDEFINITE);
		music.play();	
	}
	public ImageView getTrapIcon() {
		return trapIcon;
	}
	public void setMediaVolume(double volume) {
		sounds.forEach((k,v) -> v.setVolume(volume));
	}
	public void pauseMusic() {
		sounds.get("music").pause();
	}
	public void resumeMusic() {
		sounds.get("music").play();
	}	
	public void setYears(String years) {
		this.years.setText(years);
	}
	public Rectangle getHealth() {
		return health;
	}
	public Rectangle getHunger() {
		return hunger;
	}
	public Rectangle getThirst() {
		return thirst;
	}
	public void setHealth(double health) {
		game.getHunter().setHealth(health);
		this.health.setWidth(health);
	}
	public void setThirst(double thirst) {
		game.getHunter().setThirst(thirst);
		getThirst().setWidth(thirst);
	}public void setHunger(double hunger) {
		game.getHunter().setHunger(hunger);
		this.hunger.setWidth(hunger);
	}
}
