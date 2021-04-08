package HPack;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Timer extends AnimationTimer{
	double time = 0;
	double last;
	double delta;
	double amount;
	int lastMod, lastMod2, lastMod3, lastMod4, lastMod5;
	int days;
	int years;
	Game game;
	Timer(Game game){
		this.game = game;
	}
	@Override
	public void handle(long now) {
		delta = now-last;
		if(last==0) {
			time -=now/1e9;
		}
		if(delta>5e7) delta = 0;
		amount = 3*delta/1e7;
		//Continuously moves the birds to the right
		if(amount<40) {
			game.moveAnimal(amount);
			time+=delta/1e9;
		}
		if(!game.controller.getTrapIcon().isVisible()) {
			ListIterator<DynamicAnimal> iterator = game.getDynamicAnimals().listIterator();
			while(iterator.hasNext()) {
				DynamicAnimal animal = iterator.next();
				if(animal.getType().equals("rabbit") &&  GameObject.isOver(animal, game.getTrapHitBox())) {
					System.out.println("Passed");
					game.initGameObject("rabbitMeat", animal.getX(), animal.getY());
					game.remove(animal,true);
					iterator.remove();
					break;
	
				}
			}
		}
		game.controller.setHunger(game.getHunter().getHunger()-delta/3e8);
		game.controller.setThirst(game.getHunter().getThirst()-delta/2e8);
		last = now;
		
		// initiates a water every 5 seconds
		if((int)time%5==0 && lastMod != (int)time && game.nrOfType("water")<10) {
			lastMod = (int)time;
			game.initGameObject("water", Math.random()*(570)+50, Math.random()*(570));
		}
		// initiates a bird every three seconds
		if((int)time%1==0 && lastMod2 != (int)time && game.nrOfType("bird")<7) {
			lastMod2 = (int)time;
//			updates the score
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
		// changes the sprite of the bird every 200ms.
		if((int)(time*10)%2== 0 && lastMod3 != (int)(time*10)) {
			lastMod3 = (int)(time*10);
			game.controller.nextImages("bird");
		}
		if((int)(time*10)%1== 0 && lastMod4 != (int)(time*10)) {
			lastMod4 = (int)(time*10);
			game.controller.nextImages("rabbit");
		}
		if((int)time%10==0 && lastMod5 != (int)time) {
			lastMod5 = (int)time;
			
			for(GameObject obj:game.getObjects()) {
				System.out.println(obj.getType());
			}
		}
		
		
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	
}
