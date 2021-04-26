package HPack;

import javafx.animation.AnimationTimer;

public class Movement extends AnimationTimer{
	private Game game;
	private double delta;
	private long lastFrame;
	public Movement(Game game){
		this.game = game;
	}
	@Override
	public void handle(long now) {
		delta = (now-lastFrame)/1e7;
		lastFrame = now;
		if(delta > 5) delta = 0;
		double speed = game.getHunter().getSpeed();
		double X = game.getHunter().getX();
		double Y = game.getHunter().getY();
		
		if(HunterController.isUp()) Y-=speed*delta;
		if(HunterController.isDown()) Y+=speed*delta;
		if(HunterController.isLeft()) X-=speed*delta; 
		if(HunterController.isRight()) X+=speed*delta;
		if(X+game.getHunter().getWidth()>=650 || X<=50) {
			X = game.getHunter().getX();
		}
		if(Y+game.getHunter().getHeight()>=600 || Y<=0) {
			Y = game.getHunter().getY();
		}
		if(X<game.getHunter().getX()) game.getHunter().setImage("hunterL");
		else if(X>game.getHunter().getX())  game.getHunter().setImage("hunterR");
		else if (Y<game.getHunter().getY())  game.getHunter().setImage("hunterU");
		else if(Y>game.getHunter().getY())  game.getHunter().setImage("hunterD");
		game.getHunter().setPosition(X, Y);
	}		
}
	
