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

		double speed = game.getHunter().getSpeed();
		double X = game.getHunter().getX();
		double Y = game.getHunter().getY();
		
		if(HunterApp.isUp()) Y-=speed*delta;
		if(HunterApp.isDown()) Y+=speed*delta;
		if(HunterApp.isLeft()) X-=speed*delta; 
		if(HunterApp.isRight()) X+=speed*delta;
		if(HunterApp.isInteract()) game.objectInteraction();
		if(X+game.getHunter().getWidth()>650 || X<50) {
			X = game.getHunter().getX();
		}
		if(Y+game.getHunter().getHeight()>600 || Y<0) {
			Y = game.getHunter().getY();
		}
		if(X<game.getHunter().getX()) game.getHunter().setImageView(game.getHunter().getImageView(),HunterController.getImages().get("hunterL"));
		else if(X>game.getHunter().getX())  game.getHunter().setImageView(game.getHunter().getImageView(),HunterController.getImages().get("hunterR"));
		else if (Y<game.getHunter().getY())  game.getHunter().setImageView(game.getHunter().getImageView(),HunterController.getImages().get("hunterU"));
		else if(Y>game.getHunter().getY())  game.getHunter().setImageView(game.getHunter().getImageView(),HunterController.getImages().get("hunterD"));
		game.getHunter().setPosition(X, Y);
	}		
}
	
