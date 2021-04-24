package HPack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import javafx.scene.image.ImageView;

public class Game {
	private List<GameObject> objects = new ArrayList<GameObject>(); 
	private List<DynamicAnimal> dynamicAnimals= new ArrayList<DynamicAnimal>();
	private Timer timer;
	private int days, years;
	private Movement movement = new Movement(this);
	private ToFile toFile = new ToFile();
	private double time = 0;
	private Hunter hunter;
	private Item trap,trapHitBox;
	private boolean check = true;
	private Listener listener;
	private boolean trapIcon = true;
	
	public void init() {
		spawnPlayer();
	}
	public void start() {
		HunterController.getSounds().get("gameOver").stop();
		days = 0;
		years = 0;
		timer.start();
		movement.start();
	}

	public void save() {
		toFile.write(objects, hunter, timer.getTime(),days,years);
	}
	public void load() {
		start();
		objects = toFile.readObjects();
		hunter = toFile.readHunter();
		days = toFile.readDays();
		years = toFile.readYears();
		time = toFile.readTime();
		objects.forEach(o -> {
			if(o.getType().equals("trap")) {
				trapIcon = false;
				this.trap = (Item) o;
			}
			if(o.getClass().getSimpleName().equals("DynamicAnimal")) dynamicAnimals.add((DynamicAnimal) o);
		});
		if(objects.contains(trap)) {
			remove(trap);
			initGameObject("trap", trap.getX(), trap.getY());
		}
		orderZ();
	}
	public void gameOver() {
		timer.stop();
		movement.stop();
		hunter.setHunger(62);
		hunter.setHealth(62);
		hunter.setThirst(62);
		HunterApp.setUp(false);
		HunterApp.setDown(false);
		HunterApp.setLeft(false);
		HunterApp.setRight(false);
		trapIcon = true;
		objects.clear();
		dynamicAnimals.clear();
		spawnPlayer();
	}
	public void spawnPlayer() {
		Hunter hunter = new Hunter(338,270,new ImageView(),HunterController.getImages().get("hunterD"));
		this.hunter = hunter;
		if(listener!=null) listener.addObject(hunter);
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
	}
	private void initClassObject(GameObject obj, double x, double y, String type) {
		obj.setImageView(new ImageView(), HunterController.getImages().get(type));
		obj.setType(type);
		obj.setX(x);
		obj.setY(y);
		add(obj);
	}
	public void orderZ() {
		objects.sort(Comparator.comparing(GameObject::getType));
		for(GameObject obj : objects) {
			if(!obj.getClass().getSimpleName().equals("DynamicAnimal")) {
				obj.getImageView().toBack();
			}
		}
	}
	public void add(GameObject obj) {
		objects.add(obj);
		if(obj.getClass().getSimpleName().equals("DynamicAnimal")) {
		dynamicAnimals.add((DynamicAnimal)obj);
		}
		if(listener!=null) listener.addObject(obj);
	}
	public void remove(GameObject obj) {
		objects.remove(obj);
		if(obj.getClass().getSimpleName().equals("DynamicAnimal")) {
		dynamicAnimals.remove(obj);
		}
		if(listener!=null) listener.removeObject(obj);
	}
	public void remove(GameObject obj, boolean b) {
		if(b) {
			objects.remove(obj);
		} else {
			if(obj.getClass().getSimpleName().equals("DynamicAnimal")) {
			dynamicAnimals.remove(obj);
			}
		}
		if(listener!=null) listener.removeObject(obj);
	}
	public void objectInteraction() {
		ListIterator<GameObject> iterator = objects.listIterator();
		while(iterator.hasNext()) {
			GameObject next = iterator.next();
			if(!next.getClass().getSimpleName().toString().equals("DynamicAnimal") && GameObject.isOver(next, hunter)) {
				switch(next.getType()) {
				case "rabbitMeat": hunter.setHunger(62);; break;
				case "trap" : trapIcon = true; check = false; break;
				case "water": hunter.setThirst(62); break;
				}
				HunterApp.setInteract(false);
				remove(next, false);
				iterator.remove();
			}
		}
		
		if(trapIcon && check) {
			ListIterator<GameObject> iterator2 = objects.listIterator();
			while(iterator2.hasNext()) {
				GameObject next = iterator2.next();
				if(next.getType().equals("trapHitBox")) {
					remove(next, false);
					iterator2.remove();
				}
			}
			initGameObject("trap", hunter.getX(),hunter.getY()+10);
			trap.getImageView().toBack();
			trapIcon = false;
			HunterApp.setInteract(false);
		} else check = true;
	}
	public boolean isTrapIcon() {
		return trapIcon;
	}	
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	public void setTrapIcon(boolean trapIcon) {
		this.trapIcon = trapIcon;
	}
	public void addListener(Listener listener) {
		this.listener = listener;
	}
	public void updateListener() {
		if(listener!=null) {
			listener.updateTrapIcon(trapIcon);
			listener.updateYears(years);
			listener.updateDays(days);
			listener.setStats(hunter);
		}
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
	public List<GameObject> getObjects(){
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
	public void increaseDays() {
		days++;
		if(days==365) {
			days = 0;
			years++;
		}
	}
}
