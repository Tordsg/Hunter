package HPack;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javafx.animation.AnimationTimer;

public class Timer extends AnimationTimer{
	double time = 0, last = System.nanoTime(), delta, amount;
	int lastMod, lastMod2, lastMod3, lastMod4, lastMod5, lastDamaged, delayCheck, days, years;
	List<IncrementInit> inits = new ArrayList<IncrementInit>();
	Game game;
	boolean damaged;
	IncrementInit iI = new IncrementInit(5000); 
	IncrementInit scoreCount = new IncrementInit(1000);
	IncrementInit iI2 = new IncrementInit(200);
	IncrementInit iI3 = new IncrementInit(100);
	IncrementInit damage = new IncrementInit(1000);
	Timer(Game game){
		this.game = game;
	}
	@Override
	public void handle(long now) {
		delta = (now-last)/1e6;	
		last = now;
			game.moveAnimal(delta/5);
			time+=delta/1e9;
		if(!game.controller.getTrapIcon().isVisible()) {
			ListIterator<DynamicAnimal> iterator = game.getDynamicAnimals().listIterator();
			while(iterator.hasNext()) {
				DynamicAnimal animal = iterator.next();
				if(animal.getType().equals("rabbit") &&  GameObject.isOver(animal, game.getTrapHitBox())) {
					game.initGameObject("rabbitMeat", animal.getX(), animal.getY());
					game.remove(animal,true);
					iterator.remove();
					break;
				}
			}
		}
	
		if(!damaged) {
			if(game.controller.getThirst().getWidth()==0 || game.controller.getHunger().getWidth()==0) {
				game.controller.setHealth(game.controller.getHealth().getWidth()-62*0.1);
				damaged = true;
				game.controller.hit.play();
			} else {
				for(DynamicAnimal animal : game.getDynamicAnimals()) {
					if(GameObject.isOver(animal, game.getHunter())) {
						game.controller.setHealth(game.controller.getHealth().getWidth()-62*0.1);
						damaged = true;
						game.controller.hit.play();
					}
				}
			}
		}
		if(damaged && damage.update(delta)) {
			damaged = false;
			game.controller.hit.stop();
		}	
		if(game.getHunter().getHunger()>0) game.controller.setHunger(game.getHunter().getHunger()-delta/4e2);
		if(game.getHunter().getThirst()>0) game.controller.setThirst(game.getHunter().getThirst()-delta/2e2);
		System.out.println(game.controller.getHunger().getWidth());
		if(iI.update(delta)) game.initGameObject("water", Math.random()*(570)+50, Math.random()*(570));
		if(iI2.update(delta)) game.controller.nextImages("bird");
		if(iI3.update(delta)) game.controller.nextImages("rabbit");
		if(scoreCount.update(delta)) {
			days++;
			if(days == 365) {
				days = 0;
				years++;
				game.controller.setYears(Integer.toString(years));
			}
			game.controller.setDays(Integer.toString(days));
			game.initGameObject("bird", 0, Math.random()*(570));
			game.initGameObject("rabbit",650, Math.random()*(570));
		}
		
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public void initWater() {
		game.initGameObject("water", Math.random()*(570)+50, Math.random()*(570));
	}
	
}
