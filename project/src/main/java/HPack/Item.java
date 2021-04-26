package HPack;

public class Item implements GameObject{
	private double X;
	private double Y;
	private double height = 24;
	private double width = 24;
	private String type;
	private String image;
		public void setHeight(double height) {
		this.height = height;
	}
	public void setWidth(double width) {
		this.width = width;
	}
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

		this.X = X;
	}
	public void setY(double Y) {
		this.Y = Y;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	

}
