package HPack;

import java.io.File;
import java.util.HashMap;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class HunterController implements Listener{
	@FXML
	private Pane pane, gamePane, infoPane;
	@FXML
	private Button Save, help, play;
	@FXML
	private Slider volumeSlider;
	@FXML
	private Rectangle health, hunger, thirst;
	@FXML
	private Text days, years, header;
	@FXML
	private ImageView trapIcon;
	private double volume = 0.03;
	private Game game = new Game();
	private Timer timer = new Timer(game,this);
	private static HashMap<String, Image> images = new HashMap<String,Image>();
	private static HashMap<String, MediaPlayer> sounds = new HashMap<String, MediaPlayer>();
	private HashMap<GameObject, ImageView> objectImages = new HashMap<GameObject, ImageView>();
	private static boolean up,down,left,right,trap,consume;

	
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
	
	public void update() {
		objectImages.forEach((k,v) -> {
			if(!k.getClass().getSimpleName().equals("Item")) {
				v.setImage(images.get(k.getImage()));
			} else {
				v.toBack();
			}
			v.setLayoutX(k.getX());
			v.setLayoutY(k.getY());
		});
		if(!trapIcon.isVisible()) objectImages.get(game.getTrap()).toBack();
		objectImages.forEach((k,v) -> {
			if(k.getType().equals("water")) v.toBack();
		});
	}
	@FXML
	public void keyPressed(KeyEvent e) {
		switch(e.getCode()) {
		case Q : game.trap(); break;
		case W : game.consume(); break;
		case UP : up = true; break;
		case DOWN : down = true; break;
		case LEFT : left = true; break;
		case RIGHT : right = true; break;
		default: 
			break;
		}
	}
	@FXML
	public void keyReleased(KeyEvent e) {
		switch(e.getCode()) {
		case UP : up = false; break;
		case DOWN : down = false; break;
		case LEFT : left = false; break;
		case RIGHT : right = false; break;
		default: 
			break;
		}	
	}
	@FXML
	public void keyTyped(KeyEvent e) {
		switch(e.getCharacter()) {
		case "p": if(!infoPane.isVisible()) play(); break;
		case "l": if(Save.getText().equals("Load") && !Save.isDisabled()) save(); break;
		case "s": if(Save.getText().equals("Save")) { System.out.println("saved"); save(); } break;
		case "h": help(); break;
		case "m": if(sounds.get("music").getVolume()>0)setMediaVolume(0); else setMediaVolume(volume); break;
		default:
			break; 
		}
	}
	
	@FXML
	public void initialize() {
		pane.requestFocus();
		game.addListener(this);
		game.setTimer(timer);
		game.spawnPlayer();
		headerUp(false);
		addMusic(volume);
	}
	@FXML
	public void play() {
		play.setVisible(false);
		Save.setText("Save");
		headerUp(true);
		game.start();
	}
	@FXML
	public void save() {

		if(Save.getText().equals("Save")) {
			game.save();
		} else {
			Save.setText("Save");
			removeObject(game.getHunter());
			game.start();
			game.load();
			timer.setTime(game.getTime());
			addObject(game.getHunter());
			headerUp(true);
			}
		}
	@FXML
	public void help() {
		if(infoPane.isVisible()) {
			if(!Save.getText().equals("Load")) {
				timer.start();
				game.getMovement().start();
			}else {
				play.setDisable(false);
				Save.setDisable(false);
			}
			infoPane.setVisible(false);
			infoPane.setDisable(true);
		} else {
			
			infoPane.setVisible(true);
			infoPane.setDisable(false);
			infoPane.toFront();
			if(!Save.getText().equals("Load")) {
				timer.pause();
				game.getMovement().stop();
			} else {
				play.setDisable(true);
				Save.setDisable(true);
			}
		}
	}
	@FXML
	public void setSliderVolume() {
		if(!volumeSlider.isDisabled()) {
			volumeSlider.toFront();
			double value = volumeSlider.getValue();
			setMediaVolume(value);
			volume = value;
			pane.requestFocus();
		}
	}
	@FXML
	public void enableSlider() {
		if(volumeSlider.isDisable()) {
			volumeSlider.toFront();
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
	
	public void gameOver() {
		headerUp(false);
		Save.setText("Load");
		objectImages.forEach((k,v)-> gamePane.getChildren().remove(v));
		objectImages.clear();
	}
	public double getVolume() {
		return volume;
	}
	public static HashMap<String, Image> getImages() {
		return images;
	}
	public static HashMap<String, MediaPlayer> getSounds() {
		return sounds;
	}
	public void addMusic(double volume) {
		sounds.forEach((k,v) -> v.setVolume(volume));
		MediaPlayer music = sounds.get("music");
		music.setCycleCount(MediaPlayer.INDEFINITE);
		music.play();	
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
	private void headerUp(boolean direction){
		AnimationTimer up = new AnimationTimer() {
			long last = System.nanoTime();
			long delta;
			@Override
			public void handle(long now) {
				delta = now-last;
				last = now;
				if(direction) {
					play.setVisible(false);
					if(header.getLayoutY()<-200) {
						this.stop();
						header.setVisible(false);
						header.setDisable(true);
					}
					header.setLayoutY(header.getLayoutY()-delta/2e6);
				}
				else {
					Save.setDisable(true);
					header.setVisible(true);
					header.setDisable(false);
					if(header.getLayoutY()>=175) {
						Save.setDisable(false);
						play.setVisible(true);
						play.setDisable(false);
						play.toFront();
						this.stop();
					}
					header.setLayoutY(header.getLayoutY()+delta/6e6);
				}
			}
		};
		up.start();
	}
	public void updateTrapIcon(boolean trapIcon) {
		this.trapIcon.setVisible(trapIcon);
	}
	public void addObject(GameObject obj) {
		if(!obj.getType().equals("trapHitBox")) {
			ImageView view = new ImageView(images.get(obj.getImage()));
			view.setFitWidth(obj.getWidth());
			view.setFitHeight(obj.getHeight());
			view.setLayoutX(obj.getX());
			view.setLayoutY(obj.getY());
			objectImages.put(obj, view);
			gamePane.getChildren().add(view);
		}
	}
	public void removeObject(GameObject obj) {
		if(objectImages.containsKey(obj)) {
			gamePane.getChildren().remove(objectImages.get(obj));
			objectImages.remove(obj);
		}
	}
	public void updateDays(int days) {
		this.days.setText(String.valueOf(days));
	}
	public void updateYears(int years) {
		this.years.setText(String.valueOf(years));
	}
	public void setStats(Hunter hunter) {
		this.health.setWidth(hunter.getHealth());
		this.hunger.setWidth(hunter.getHunger());
		this.thirst.setWidth(hunter.getThirst());
	}
	public static boolean isUp() {
		return up;
	}
	public static void setUp(boolean up) {
		HunterController.up = up;
	}
	public static boolean isDown() {
		return down;
	}
	public static void setDown(boolean down) {
		HunterController.down = down;
	}
	public static boolean isLeft() {
		return left;
	}
	public static void setLeft(boolean left) {
		HunterController.left = left;
	}
	public static boolean isRight() {
		return right;
	}
	public static void setRight(boolean right) {
		HunterController.right = right;
	}
	public static boolean isTrap() {
		return trap;
	}
	public static void setTrap(boolean trap) {
		HunterController.trap = trap;
	}
	public static boolean isConsume() {
		return consume;
	}
	public static void setConsume(boolean consume) {
		HunterController.consume = consume;
	}
}
