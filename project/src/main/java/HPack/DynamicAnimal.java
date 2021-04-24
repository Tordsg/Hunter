package HPack;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DynamicAnimal implements GameObject{
	private double X;
	private double Y;
	private double speed = 3;
	private double width = 28;
	private double height = 28;
	private String type;
	private ImageView imageView;
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
		if(imageView!=null) {
		imageView.setY(y);
		imageView.setX(x);
		}
		this.X = x;
		this.Y = y;
	}
	@Override
	public void setX(double x) {
		if(imageView!=null) {
			imageView.setX(x);
		}
		this.X = x;
		
	}
	public double getSpeed() {
		return speed;
	}
	@Override
	public void setY(double y) {
		if(imageView!=null) {
			imageView.setY(y);
		}
		this.Y = y;
		
	}
	public ImageView getImageView() {
		return imageView;
	}
	public void setImageView(ImageView imageView, Image image) {
		if(imageView!=null) {
		imageView.setImage(image);
		imageView.setX(X);
		imageView.setY(Y);
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		this.imageView = imageView;	
		}
	}
}
