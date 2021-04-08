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
	Pane pane;
	@FXML
	Button Pause;
	@FXML
	Button help;
	@FXML
	Slider volumeSlider;
	@FXML
	Pane gamePane;
	@FXML
	Rectangle health;
	@FXML
	Rectangle hunger;
	@FXML
	Rectangle thirst;
	@FXML
	Text days;
	@FXML
	Text years;
	@FXML
	ImageView trapIcon;
	Game game = new Game(this);
	HashMap<String, Image> images = new HashMap<String,Image>(); 
	Media media;
	MediaPlayer mediaPlayer;
	double volume = 0.03;
	ImageView hunterImage;
	boolean isPaused = false;
	GameObject removed;
	
	@FXML
	void initialize() {
		addBackground();
		addMusic(volume);
		initSprites();
		game.main();
		
	}
	void initializeFromFile() {
		if(!isPaused) {
		addBackground();
		addMusic(volume);
		initSprites(); 
		}
		game.initFromFile();
	}
	
	@FXML
	void Pause() {
		if(Pause.getText().equals("Pause")) {
			Pause.setText("Resume");
			game.pauseTimer();
			game.toFile.write();
			isPaused = true;
			
		} else {
			Pause.setText("Pause");
			initializeFromFile();
			isPaused = false;
			game.resumeTimer();
		}
	}

	@FXML
	void help() {
		
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
	
	void movement(Hunter hunter) {	
		AnimationTimer animation = new AnimationTimer() {
			double delta;
			long lastFrame;
			@Override
			public void handle(long now) {
				delta = (now-lastFrame)/1e7;
				lastFrame = now;

				double speed = hunter.getSpeed();
				double X = hunter.getX();
				double Y = hunter.getY();
				
				if(HunterApp.up) Y-=speed*delta;
				if(HunterApp.down) Y+=speed*delta;
				if(HunterApp.left) X-=speed*delta; 
				if(HunterApp.right) X+=speed*delta;
				if(HunterApp.interact) {
					game.objectInteraction();
				}
				if(X+hunter.getWidth()>650 || X<50 || isPaused) {
					X = hunter.getX();
				}
				if(Y+hunter.getHeight()>600 ||Y<0|| isPaused) {
					Y = hunter.getY();
				}
				if(X<hunter.getX()) hunter.setImageView(hunter.getImageView(),images.get("hunterL"));
				else if(X>hunter.getX())  hunter.setImageView(hunter.getImageView(),images.get("hunterR"));
				else if (Y<hunter.getY())  hunter.setImageView(hunter.getImageView(),images.get("hunterU"));
				else if(Y>hunter.getY())  hunter.setImageView(hunter.getImageView(),images.get("hunterD"));
				hunter.setPosition(X, Y);
			}
		};
		animation.start();
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
	public void addItem(GameObject obj) {
		ImageView imageView = new ImageView();
		obj.setImageView(imageView, images.get(obj.getType()));
		gamePane.getChildren().add(imageView);
		if(obj.getType()!="bird") {
			imageView.toBack();
		}
		
	}
//	Pre initialize sprites, so that game-performance is increased.
	public void initSprites() {
		images.put("trap", new Image("HPack/trap.png"));
		images.put("water",new Image("HPack/water.png"));
		images.put("birdU",new Image("HPack/birdU.png"));
		images.put("bird",new Image("HPack/bird.png"));
		images.put("bird2",new Image("HPack/bird2.png"));
		images.put("birdD",new Image("HPack/birdD.png"));
		images.put("hunterD",new Image("HPack/hunterD.png"));
		images.put("hunterU",new Image("HPack/hunterU.png"));
		images.put("hunterL",new Image("HPack/hunterL.png"));
		images.put("hunterR",new Image("HPack/hunterR.png"));
		images.put("rabbitU",new Image("HPack/rabbitU.png"));
		images.put("rabbit",new Image("HPack/rabbit.png"));
		images.put("rabbitD",new Image("HPack/rabbitD.png"));
		images.put("rabbitMeat",new Image("HPack/rabbitMeat.png"));
	}
	public void addMusic(double volume) {
		media = new Media(new File("src/main/java/Hpack/Retro_lyder.mp3").toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setVolume(volume);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();
	}
	public ImageView getTrapIcon() {
		return trapIcon;
	}
	public void setRemoved(GameObject obj) {
		this.removed = obj;
	}
	public void setMediaVolume(double volume) {
		mediaPlayer.setVolume(volume);
	}
	public void pauseMusic() {
		mediaPlayer.pause();
	}
	public void resumeMusic() {
		mediaPlayer.play();
	}
	public GameObject getRemoved() {
		return removed;
	}
	public void addBackground() {
		BackgroundImage bgI = new BackgroundImage(new Image("HPack/Grass.png",600.0,600.0,false,true
				), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		gamePane.setBackground(new Background(bgI));
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
