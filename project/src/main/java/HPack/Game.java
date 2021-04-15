package HPack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.scene.image.ImageView;

public class Game {
	private List<GameObject> objects = new ArrayList<GameObject>(); 
	private List<DynamicAnimal> dynamicAnimals= new ArrayList<DynamicAnimal>();
	HunterController controller;
	Timer timer;
	Movement movement = new Movement(this);
	ToFile toFile = new ToFile(this);
	double time = 0;
	Hunter hunter;
	Item trap,trapHitBox;
	boolean check = false;
	boolean isPaused = false;
	IncrementInit iI5;
	
	Game(HunterController controller){
		this.controller = controller;
	}
	
	public void start() {
		HunterController.sounds.get("gameOver").stop();
		timer = new Timer(this);
		timer.start();
		movement.start();
	}
	public void stop() {
		timer.stop();
		movement.stop();
	}
	public void gameOver() {
		stop();
		controller.gamePane.getChildren().remove(hunter.getImageView());
		addPlayer(); 
		controller.setHealth(62);
		controller.setHunger(62);
		controller.setThirst(62);
		controller.getTrapIcon().setVisible(true);
		objects.forEach(o -> controller.gamePane.getChildren().remove(o.getImageView()));
		objects.clear();
		hunter.setImageView(hunter.getImageView(),HunterController.images.get("hunterD"));
		
	}
	public void addPlayer() {
		Hunter hunter = new Hunter(338,270,new ImageView(),HunterController.images.get("hunterD"));
		this.hunter = hunter;
		controller.gamePane.getChildren().add(hunter.getImageView());
		
	}
	public void initFromFile() {
		toFile.read();
		resumeTimer();
		for(GameObject obj: objects) {
			controller.gamePane.getChildren().add(obj.getImageView());
			if(obj.getType().equals("water") || obj.getType().equals("trap") || obj.getType().equals("rabbit")) {
				obj.getImageView().toBack();
			}
		}
	}
	void moveAnimal(double amount) {
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
		obj.setImageView(new ImageView(), HunterController.images.get(type));
		obj.setType(type);
		obj.setX(x);
		obj.setY(y);
		add(obj);
	}
	private void orderZ() {
		getObjects().sort(Comparator.comparing(GameObject::getType));
		for(GameObject obj : getObjects()) {
			if(!obj.getClass().getName().equals("HPack.DynamicAnimal")) {
				obj.getImageView().toBack();
			}
		}
	}
	void add(GameObject obj) {
		controller.gamePane.getChildren().add(obj.getImageView());
		objects.add(obj);
		if(obj.getClass().getName().equals("HPack.DynamicAnimal")) {
		dynamicAnimals.add((DynamicAnimal)obj);
		}
	}
	void remove(GameObject obj) {
		controller.gamePane.getChildren().remove(obj.getImageView());
		objects.remove(obj);
		if(obj.getClass().getName().equals("HPack.DynamicAnimal")) {
		dynamicAnimals.remove(obj);
		}
	}
	void remove(GameObject obj, boolean b) {
		controller.gamePane.getChildren().remove(obj.getImageView());
		if(b) {
			objects.remove(obj);
		} else {
			if(obj.getClass().getName().equals("HPack.DynamicAnimal")) {
			dynamicAnimals.remove(obj);
			}
		}
	}
	void objectInteraction() {
		ListIterator<GameObject> iterator = objects.listIterator();
		while(iterator.hasNext()) {
			GameObject next = iterator.next();
			if(!next.getClass().getInterfaces()[0].toString().equals("interface HPack.DynamicObject") && GameObject.isOver(next, hunter)) {
				switch(next.getType()) {
				case "rabbitMeat": controller.setHunger(62); break;
				case "trap" : controller.getTrapIcon().setVisible(true); check = false; break;
				case "water": controller.setThirst(62); break;
				}
				HunterApp.interact = false;
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
			HunterApp.interact = false;
		} else check = true;
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
	void pauseTimer() {
		time = timer.getTime();
		timer.stop();
	}
	void resumeTimer() {
		timer.setTime(time);
		timer.start();
	}
	public int nrOfType(String type) {
		int nr = 0;
		for(GameObject obj: objects) {
			if(obj.getType().equals(type)) {
				nr++;
			}
		}
		return nr;
	}
	public Hunter getHunter() {
		return hunter;
	}
}
