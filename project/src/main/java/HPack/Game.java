package HPack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import javafx.scene.image.ImageView;

public class Game {
	private List<GameObject> objects = new ArrayList<GameObject>(); 
	private List<DynamicAnimal> dynamicAnimals= new ArrayList<DynamicAnimal>();
	private HunterController controller;
	private Timer timer;
	private Movement movement = new Movement(this);
	private ToFile toFile = new ToFile(this);
	private double time = 0;
	private Hunter hunter;
	private Item trap,trapHitBox;
	private boolean check = false;
	
	Game(HunterController controller){
		this.controller = controller;
	}
	
	public void start() {
		timer = new Timer(this);
		HunterController.getSounds().get("gameOver").stop();
		timer.start();
		movement.start();
	}
	public void save() {
		toFile.write();
	}
	public void load() {
		start();
		toFile.read();
		objects.forEach(o -> {
			if(o.getType().equals("trap")) controller.getTrapIcon().setVisible(false);
		});
		dynamicAnimals.forEach(o -> {
			
			controller.getGamePane().getChildren().add(o.getImageView());
		});
		controller.getGamePane().getChildren().add(hunter.getImageView());
		orderZ();
	}
	public void gameOver() {
		timer.stop();
		movement.stop();
		controller.getGamePane().getChildren().remove(hunter.getImageView());
		controller.setHealth(62);
		controller.setHunger(62);
		controller.setThirst(62);
		HunterApp.setUp(false);
		HunterApp.setDown(false);
		HunterApp.setLeft(false);
		HunterApp.setRight(false);
		controller.getTrapIcon().setVisible(true);
		objects.forEach(o -> controller.getGamePane().getChildren().remove(o.getImageView()));
		objects.clear();
		addPlayer(); 
		hunter.setImageView(hunter.getImageView(),HunterController.getImages().get("hunterD"));
	}
	public void addPlayer() {
		Hunter hunter = new Hunter(338,270,new ImageView(),HunterController.getImages().get("hunterD"));
		this.hunter = hunter;
		controller.getGamePane().getChildren().add(hunter.getImageView());
		
	}
	public void moveAnimal(double amount) {
		ListIterator<DynamicAnimal> iterator = dynamicAnimals.listIterator();
		while(iterator.hasNext()) {
			DynamicAnimal animal = iterator.next();
			if(animal.getType().equals("bird")) {
			animal.setX(animal.getX()+amount);
				if(animal.getX()>670) {
					remove(animal, true);
					iterator.remove();
				}
			} else if (animal.getType().equals("rabbit")){
				animal.setX(animal.getX()-amount);
				if(animal.getX()<30){
					remove(animal, true);
					iterator.remove();
				}
			}
		}
	}

	public void initGameObject(String type, double x, double y) {
		switch(type) {
		case "rabbit": DynamicAnimal obj = new DynamicAnimal(); initClassObject(obj,x,y,type); break;
		case "bird": DynamicAnimal obj2 = new DynamicAnimal(); initClassObject(obj2,x,y,type); break;
		case "water": Item obj3 = new Item(); initClassObject(obj3,x,y,type); break;
		case "rabbitMeat": Item obj4 = new Item(); initClassObject(obj4,x,y,type); obj4.setHeight(15); break;
		case "trap": Item obj5 = new Item(); initClassObject(obj5,x,y,type); this.trap = obj5; initGameObject("trapHitBox", x + obj5.getWidth()/2, y+obj5.getHeight()/2); break;
		case "trapHitBox": Item obj6 = new Item(); initClassObject(obj6,x,y,type);  obj6.getImageView().setVisible(false); obj6.setWidth(0.01); obj6.setHeight(0.01); this.trapHitBox = obj6; break;
		}
		orderZ();
	}
	private void initClassObject(GameObject obj, double x, double y, String type) {
		obj.setImageView(new ImageView(), HunterController.getImages().get(type));
		obj.setType(type);
		obj.setX(x);
		obj.setY(y);
		add(obj);
	}
	private void orderZ() {
		getObjects().sort(Comparator.comparing(GameObject::getType));
		for(GameObject obj : getObjects()) {
			if(!obj.getClass().getSimpleName().equals("DynamicAnimal")) {
				obj.getImageView().toBack();
			}
		}
	}
	public void add(GameObject obj) {
		controller.getGamePane().getChildren().add(obj.getImageView());
		objects.add(obj);
		if(obj.getClass().getSimpleName().equals("DynamicAnimal")) {
		dynamicAnimals.add((DynamicAnimal)obj);
		}
	}
	public void remove(GameObject obj) {
		controller.getGamePane().getChildren().remove(obj.getImageView());
		objects.remove(obj);
		if(obj.getClass().getSimpleName().equals("DynamicAnimal")) {
		dynamicAnimals.remove(obj);
		}
	}
	public void remove(GameObject obj, boolean b) {
		controller.getGamePane().getChildren().remove(obj.getImageView());
		if(b) {
			objects.remove(obj);
		} else {
			if(obj.getClass().getSimpleName().equals("DynamicAnimal")) {
			dynamicAnimals.remove(obj);
			}
		}
	}
	public void objectInteraction() {
		ListIterator<GameObject> iterator = objects.listIterator();
		while(iterator.hasNext()) {
			GameObject next = iterator.next();
			if(!next.getClass().getSimpleName().toString().equals("DynamicAnimal") && GameObject.isOver(next, hunter)) {
				switch(next.getType()) {
				case "rabbitMeat": controller.setHunger(62); break;
				case "trap" : controller.getTrapIcon().setVisible(true); check = false; break;
				case "water": controller.setThirst(62); break;
				}
				HunterApp.setInteract(false);
				remove(next, false);
				iterator.remove();
			}
		}
		
		if(controller.getTrapIcon().isVisible() && check) {
			ListIterator<GameObject> iterator2 = objects.listIterator();
			while(iterator2.hasNext()) {
				GameObject next = iterator2.next();
				if(next.getType().equals("trapHitBox")) {
					remove(next, false);
					iterator2.remove();
				}
			}
			initGameObject("trap", hunter.getX(),hunter.getY());
			trap.getImageView().toBack();
			controller.getTrapIcon().setVisible(false);
			HunterApp.setInteract(false);
		} else check = true;
	}
	public HunterController getController() {
		return controller;
	}

	public Movement getMovement() {
		return movement;
	}
	public Item getTrap() {
		return trap;
	}	
	public Item getTrapHitBox() {
		return trapHitBox;
	}
	List<GameObject> getObjects(){
		return objects;
	}
	public List<DynamicAnimal> getDynamicAnimals(){
		return dynamicAnimals;
	}
	public void pauseTimer() {
		time = timer.getTime();
		timer.stop();
	}
	public void resumeTimer() {
		timer.setTime(time);
		timer.start();
	}
	public Hunter getHunter() {
		return hunter;
	}
	public void setHunter(Hunter hunter) {
		this.hunter = hunter;
	}

	public Timer getTimer() {
		return timer;
	}
}
