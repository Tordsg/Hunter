package HPack;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
	private Listener listener;
	private boolean trapIcon = true;
	
	public void start() {
		HunterController.getSounds().get("gameOver").stop();
		days = 0;
		years = 0;
		if(timer!=null)timer.start();
		movement.start();
	}
	public void save() {
		toFile.write(objects, hunter, timer.getTime(),days,years);
	}
	public void load() {
		if(toFile.isLegalFile())	{
			System.out.println("Loaded from file");
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
				if(listener!=null) listener.addObject(o);
			});
			if(objects.contains(trap)) {
				remove(trap);
				initGameObject("trap", trap.getX(), trap.getY());
			}
		} else {
			System.out.println("Could not read file");
		}
	}
	public void gameOver() {
		if(timer!=null) timer.stop();
		movement.stop();
		hunter.setHunger(62);
		hunter.setHealth(62);
		hunter.setThirst(62);
		HunterController.setUp(false);
		HunterController.setDown(false);
		HunterController.setLeft(false);
		HunterController.setRight(false);
		trapIcon = true;
		objects.clear();
		dynamicAnimals.clear();
		spawnPlayer();
	}
	public void spawnPlayer() {
		Hunter hunter = new Hunter(338,270);
		this.hunter = hunter;
		if(listener!=null) listener.addObject(hunter);
	}
	public void moveAnimal(double amount) {
		ListIterator<DynamicAnimal> iterator = dynamicAnimals.listIterator();
		while(iterator.hasNext()) {
			DynamicAnimal animal = iterator.next();
			if(animal.getType().equals("bird")) {
			animal.setX(animal.getX()+amount);
				if(animal.getX()>650) {
					remove(animal, true);
					iterator.remove();
				}
			} else if (animal.getType().equals("rabbit")){
				animal.setX(animal.getX()-amount);
				if(animal.getX()<22){
					remove(animal, true);
					iterator.remove();
				}
			}
		}
	}
	public void initGameObject(String type, double x, double y) {
		switch(type) {
		case "rabbit": DynamicAnimal r = new DynamicAnimal(); initClassObject(r,x,y,type); break;
		case "bird": DynamicAnimal b = new DynamicAnimal(); initClassObject(b,x,y,type); break;
		case "water": Item w = new Item(); initClassObject(w,x,y,type); break;
		case "rabbitMeat": Item rM = new Item(); initClassObject(rM,x,y,type); rM.setHeight(15); break;
		case "trap": Item t = new Item(); initClassObject(t,x,y,type); this.trap = t; initGameObject("trapHitBox", x + t.getWidth()/2, y+t.getHeight()/2); break;
		case "trapHitBox": Item tH = new Item(); initClassObject(tH,x,y,type); tH.setWidth(0.01); tH.setHeight(0.01); this.trapHitBox = tH; break;
		}
	}
	private void initClassObject(GameObject obj, double x, double y, String type) {
		obj.setImage(type);
		obj.setType(type);
		obj.setX(x);
		obj.setY(y);
		add(obj);
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
	public void trap() {
		if(!trapIcon) {
			if(GameObject.isOver(trap, hunter)){
				remove(trapHitBox);
				remove(trap);
				trap = null;
				trapHitBox = null;
				trapIcon = true;
			} 
		}else {
			initGameObject("trap", hunter.getX(),hunter.getY()+10);
			trapIcon = false;
		}
	}
	public void consume() {
		ListIterator<GameObject> iterator = objects.listIterator();
		while(iterator.hasNext()) {
			GameObject next = iterator.next();
			if((next.getType().equals("water") || next.getType().equals("rabbitMeat")) && GameObject.isOver(next, hunter)) {
				switch(next.getType()) {
				case "rabbitMeat": hunter.setHunger(62); break;
				case "water": hunter.setThirst(62); break;
				}
				remove(next, false);
				iterator.remove();
			}
		}
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
	public double getTime() {
		return time;
	}
	public void increaseDays() {
		days++;
		if(days==365) {
			days = 0;
			years++;
		}
	}
}
