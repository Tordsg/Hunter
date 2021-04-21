package HPack;

import java.util.ListIterator;
import javafx.animation.AnimationTimer;
import javafx.scene.media.MediaPlayer;

public class Timer extends AnimationTimer{
	private double time = 0, last = System.nanoTime();
	private int days, years;
	private Game game;
	private boolean damaged, healed, resumed = false;
	private IncrementInit birdInit = new IncrementInit(1000), iI = new IncrementInit(5000), scoreCount = new IncrementInit(1000), iI2 = new IncrementInit(200), iI3 = new IncrementInit(100), damage = new IncrementInit(1000), heal = new IncrementInit(3000);
	Timer(Game game){
		this.game = game;
	}
	@Override
	public void handle(long now) {
		if(resumed) {
			last = now;
			resumed = false;
		}
		double delta = (now-last)/1e6;	
		last = now;
		game.moveAnimal(delta/5);
		time+=delta/1e5;
		if(!game.getController().getTrapIcon().isVisible()) {
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
			if(game.getController().getThirst().getWidth()<=0 || game.getController().getHunger().getWidth()<=0) {
				game.getController().setHealth(game.getController().getHealth().getWidth()-62*0.1);
				damaged = true;
				HunterController.getSounds().get("hit").play();
			} else {
				for(DynamicAnimal animal : game.getDynamicAnimals()) {
					if(GameObject.isOver(animal, game.getHunter())) {
						game.getController().setHealth(game.getController().getHealth().getWidth()-62*0.1);
						damaged = true;
						HunterController.getSounds().get("hit").play();
					}
				}
			}
		}
		if(damaged && damage.update(delta)) {
			damaged = false;
			HunterController.getSounds().get("hit").stop();
		}
		if(game.getHunter().getHealth()<=0) {
			HunterController.getSounds().get("gameOver").play(); 
			game.gameOver();
			game.getController().gameOver();
		}
		if(game.getHunter().getHunger()>0) game.getController().setHunger(game.getHunter().getHunger()-delta/4e2);
		if(game.getHunter().getThirst()>0) game.getController().setThirst(game.getHunter().getThirst()-delta/2e2);
		if(game.getHunter().getThirst() > 45 && game.getHunter().getHunger() > 45 && game.getHunter().getHealth() < 62) {
			game.getController().setHealth(game.getHunter().getHealth()+delta/4e2);
			if(heal.update(delta)) healed = true;
		}
		if(healed) {
			MediaPlayer heal = new MediaPlayer(HunterController.getSounds().get("heal").getMedia());
			heal.setVolume(game.getController().getVolume());
			heal.play();
			healed = false;
		}
		if(iI.update(delta)) game.initGameObject("water", Math.random()*(570)+50, Math.random()*(570));
		if(iI2.update(delta)) game.getController().nextImages("bird");

		if(iI3.update(delta)) game.getController().nextImages("rabbit");
		
		if(birdInit.update(delta)) game.initGameObject("bird", 0, Math.random()*(570));
		birdInit.setIncrement((1000+time)/(1+time*time));
		
		if(scoreCount.update(delta)) { 
			days++;
			if(days == 365) {
				days = 0;
				years++;
				game.getController().setYears(Integer.toString(years));
			}
			game.getController().setDays(Integer.toString(days));
			game.initGameObject("rabbit",650, Math.random()*(570));
		}
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getYears() {
		return years;
	}
	public void setYears(int years) {
		this.years = years;
	}
	public IncrementInit getBirdInit() {
		return birdInit;
	}
	public void setBirdInit(IncrementInit birdInit) {
		this.birdInit = birdInit;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public void pause() {
		this.stop();
	}
	public void resume() {
		this.start();
		resumed = true;
	}
	public void initWater() {
		game.initGameObject("water", Math.random()*(570)+50, Math.random()*(570));
	}
	
}
