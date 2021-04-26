package HPack;

public class DynamicAnimal implements GameObject{
	private double X;
	private double Y;
	private double speed = 3;
	private double width = 28;
	private double height = 28;
	private String type;
	private String image;
	public double getX() {
		return X;
	}
	public double getY() {
		return Y;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
		
	}
	public void setPosition(double x, double y) {
		this.X = x;
		this.Y = y;
	}
	public void setX(double x) {
		if(speed>0) {
		this.X = x;
		}
		
	}
	public double getSpeed() {
		return speed;
	}
	public void setY(double y) {
		this.Y = y;
		
	}
	public void nextImage() {
		if(type.equals("bird")) {
			switch(image) {
			case "bird" : image = "birdU"; break;
			case "birdU" : image = "bird2"; break;
			case "bird2" : image = "birdD"; break;
			case "birdD" : image = "bird"; break;
			}
		}else {
			switch(image) {
			case "rabbit" : image = "rabbitU"; break;
			case "rabbitU" : image = "rabbitD"; break;
			case "rabbitD" : image = "rabbit"; break;
			}
		}
	}
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
