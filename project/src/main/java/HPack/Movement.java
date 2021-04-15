package HPack;

import javafx.animation.AnimationTimer;

public class Movement extends AnimationTimer{
	Game game;
	double delta;
	long lastFrame;
	Movement(Game game){
		this.game = game;
	}
	@Override
	public void handle(long now) {
		delta = (now-lastFrame)/1e7;
		lastFrame = now;

		double speed = game.getHunter().getSpeed();
		double X = game.getHunter().getX();
		double Y = game.getHunter().getY();
		
		if(HunterApp.up) Y-=speed*delta;
		if(HunterApp.down) Y+=speed*delta;
		if(HunterApp.left) X-=speed*delta; 
		if(HunterApp.right) X+=speed*delta;
		if(HunterApp.interact) {
			game.objectInteraction();
		}
		if(X+game.getHunter().getWidth()>650 || X<50) {
			X = game.getHunter().getX();
		}
		if(Y+game.getHunter().getHeight()>600 || Y<0) {
			Y = game.getHunter().getY();
		}
		if(X<game.getHunter().getX()) game.getHunter().setImageView(game.getHunter().getImageView(),HunterController.images.get("hunterL"));
		else if(X>game.getHunter().getX())  game.getHunter().setImageView(game.getHunter().getImageView(),HunterController.images.get("hunterR"));
		else if (Y<game.getHunter().getY())  game.getHunter().setImageView(game.getHunter().getImageView(),HunterController.images.get("hunterU"));
		else if(Y>game.getHunter().getY())  game.getHunter().setImageView(game.getHunter().getImageView(),HunterController.images.get("hunterD"));
		game.getHunter().setPosition(X, Y);
	}		
}
	
