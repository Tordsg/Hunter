package HPack;

public class Hunter implements GameObject{
	private double X;
	private double Y;
	private double speed = 3;
	public double health = 62;
	private double hunger = 62;
	private double thirst = 62;
	private double width = 22;
	private double height = 35;
	private String type = "hunter";
	private String image = "hunterD";
	
	public Hunter(double X, double Y){
		this.X = X;
		this.Y = Y;
	}
	public double getHealth() {
		return health;
	}
	public void setHealth(double health) {
		this.health = health;
	}
	public double getHunger() {
		return hunger;
	}
	public void setHunger(double hunger) {
		this.hunger = hunger;
	}
	public double getThirst() {
		return thirst;
	}
	public void setThirst(double thirst) {
		this.thirst = thirst;
	}
	
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getX() {
		return X;
	}
	public void setX(double X) {
		this.X = X;
	}
	public double getY() {
		return Y;
	}
	public void setY(double Y) {
		this.Y=Y;
	}
	public void setPosition(double X,double Y) {
		this.X = X;
		this.Y = Y;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
