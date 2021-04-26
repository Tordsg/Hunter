package HPack;

import java.util.ListIterator;
import javafx.animation.AnimationTimer;

public class Timer extends AnimationTimer{
	private double time = 0, last = System.nanoTime();
	private Game game;
	private HunterController controller;
	private boolean damaged = false, healed = false;
	private IncrementInit birdI = new IncrementInit(1000), waterI = new IncrementInit(5000), scoreI = new IncrementInit(1000), birdAnimationI = new IncrementInit(200), rabbitAnimationI = new IncrementInit(100), damage = new IncrementInit(1000), heal = new IncrementInit(3500);
	public Timer(Game game, HunterController controller){
		this.game = game;
		this.controller = controller;
	}
	@Override
	public void handle(long now) {
		double delta = (now-last)/1e6;	
		if(delta>100) delta = 0;
		last = now;
		game.moveAnimal(delta/5);
		time+=delta/1e5;
		if(!game.isTrapIcon()) {
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
			if(game.getHunter().getThirst()<=0 || game.getHunter().getHunger()<=0) {
				game.getHunter().setHealth(game.getHunter().getHealth()-62*0.1);
				damaged = true;
				HunterController.getSounds().get("hit").play();
			} else {
				for(DynamicAnimal animal : game.getDynamicAnimals()) {
					if(GameObject.isOver(animal, game.getHunter())) {
						game.getHunter().setHealth(game.getHunter().getHealth()-62*0.1);
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
			controller.gameOver();
			game.gameOver();
			controller.updateTrapIcon(game.isTrapIcon());
		}
		if(game.getHunter().getHunger()>0) game.getHunter().setHunger(game.getHunter().getHunger()-delta/4e2);
		if(game.getHunter().getThirst()>0) game.getHunter().setThirst(game.getHunter().getThirst()-delta/2e2);
		if(game.getHunter().getHunger()<0) game.getHunter().setHunger(0);
		if(game.getHunter().getThirst()<0) game.getHunter().setThirst(0);
		if(game.getHunter().getThirst() > 45 && game.getHunter().getHunger() > 45 && game.getHunter().getHealth() < 62) {
			game.getHunter().setHealth(game.getHunter().getHealth()+delta/4e2);
			if(!healed) {
				HunterController.getSounds().get("heal").play();
				healed = true;
			}
		}
		if(healed) {
			if(heal.update(delta)) {
				HunterController.getSounds().get("heal").stop();
				healed = false;
			}
		}
		updateIncrementInits(delta);
		game.updateListener();
		birdI.setIncrement((1000+time)/(1+time*time));
		controller.update();
	}
	
	
	public void updateIncrementInits(double delta) {
		if(waterI.update(delta)) game.initGameObject("water", Math.random()*(570)+50, Math.random()*(570));
		if(birdAnimationI.update(delta)) nextImage("bird");
		if(rabbitAnimationI.update(delta)) nextImage("rabbit");
		if(birdI.update(delta)) game.initGameObject("bird", 22, Math.random()*(570));
		if(scoreI.update(delta)) { 
			game.increaseDays();
			game.initGameObject("rabbit",650, Math.random()*(570));
		}
	}
	public void nextImage(String type) {
		game.getDynamicAnimals().forEach(a -> {
			if(a.getType().equals(type)) a.nextImage();
		});
	}
	public IncrementInit getBirdInit() {
		return birdI;
	}
	public void setBirdInit(IncrementInit birdInit) {
		this.birdI = birdInit;
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
}
