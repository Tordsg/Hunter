package HPack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.scene.image.ImageView;

public class Game {
	private List<GameObject> objects = new ArrayList<GameObject>(); 
	private List<DynamicAnimal> dynamicAnimals= new ArrayList<DynamicAnimal>();
	HunterController controller;
	Timer timer = new Timer(this);
	ToFile toFile = new ToFile(this);
	double time = 0;
	Hunter hunter;
	Item trap;
	Item trapHitBox;
	
	
	public Item getTrapHitBox() {
		return trapHitBox;
	}


	Game(HunterController controller){
		this.controller = controller;
	}
	
	
	public void main() {
		Hunter hunter = new Hunter((controller.gamePane.getPrefWidth())/2,(controller.gamePane.getPrefHeight())/2,new ImageView(),controller.images.get("hunterD"));
		add(hunter);
		controller.movement(hunter);
		this.hunter = hunter;
		timer.start();
	}
	public void initFromFile() {
		toFile.read();
		resumeTimer();
		for(GameObject obj: objects) {
			controller.gamePane.getChildren().add(obj.getImageView());
			if(obj.getType().equals("water") || obj.getType().equals("trap") || obj.getType().equals("rabbit")) {
				obj.getImageView().toBack();
			}
			if(obj.getType().equals("hunter")) {
				controller.movement((Hunter)obj);
			}
		}
	}
	public void setTrapHitBoxPosition() {
		trapHitBox.setX(trap.getX()+trap.getWidth()/2);
		trapHitBox.setY(trap.getY());
	}
	void moveAnimal(double amount) {
		ListIterator<DynamicAnimal> iterator = dynamicAnimals.listIterator();
		while(iterator.hasNext()) {
			DynamicAnimal animal = iterator.next();
			if(animal.getType().equals("bird")) {
			animal.setX(animal.getX()+amount);
				if(animal.getX()>670) {
					remove(animal);
				}
			} else if (animal.getType().equals("rabbit")){
				animal.setX(animal.getX()-amount);
				if(animal.getX()<-20){
					remove(animal);
				}
			}
		}
	}

	public void initGameObject(String type, double x, double y) {
		switch(type) {
		case "rabbit": DynamicAnimal obj = new DynamicAnimal(); initClassObject(obj,x,y,type); break;
		case "bird": DynamicAnimal obj2 = new DynamicAnimal(); initClassObject(obj2,x,y,type); break;
		case "water": Item obj3 = new Item(); initClassObject(obj3,x,y,type); break;
		case "rabbitMeat": Item obj4 = new Item(); initClassObject(obj4,x,y,type); break;
		case "trap": Item obj5 = new Item(); initClassObject(obj5,x,y,type); this.trap = obj5; initGameObject("trapHitBox", x + obj5.getWidth()/2, y); break;
		case "trapHitBox": Item obj6 = new Item(); initClassObject(obj6,x,y,type);  obj6.getImageView().setVisible(false); obj6.setWidth(0.01); break;
		}
	}
	private void initClassObject(GameObject obj, double x, double y, String type) {
		obj.setImageView(new ImageView(), controller.images.get(type));
		obj.setType(type);
		obj.setX(x);
		obj.setY(y);
		add(obj);
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
	void objectInteraction() {
		ListIterator<GameObject> iterator = objects.listIterator();
		while(iterator.hasNext()) {
			GameObject next = iterator.next();
			if(next.getClass().getInterfaces()[0].toString()!="DynamicObject" && GameObject.isOver(next, hunter)) {
				switch(next.getType()) {
				case "rabbit": controller.setHunger(62); break;
				case "trap" : controller.getTrapIcon().setVisible(true); break;
				case "water": controller.setThirst(62); break;
				}
				remove(next);
			}
		}
		if(controller.getTrapIcon().isVisible()) {
			initGameObject("trap", getHunter().getX(),getHunter().getY());
			trap.getImageView().toBack();
			setTrapHitBoxPosition();
			controller.getTrapIcon().setVisible(false);
			HunterApp.interact = false;
		}
	}
	public Item getTrap() {
		return trap;
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
