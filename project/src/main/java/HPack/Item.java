package HPack;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item implements GameObject{
	private double X;
	private double Y;
	private double height = 24;
	private double width = 24;
	public void setHeight(double height) {
		this.height = height;
	}
	public void setWidth(double width) {
		this.width = width;
	}
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
	public void setX(double X) {
		imageView.setX(X);
		this.X = X;
	}
	public void setY(double Y) {
		imageView.setY(Y);
		this.Y = Y;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ImageView getImageView() {
		return imageView;
	}
	public void setImageView(ImageView imageView, Image image) {
		imageView.setImage(image);
		imageView.setX(X);
		imageView.setY(Y);
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		this.imageView = imageView;	
	}

}
