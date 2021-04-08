package HPack;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public interface GameObject {
	double getX();
	double getY();
	double getWidth();
	double getHeight();
	String getType();
	void setType(String type);
	void setX(double x);
	void setY(double y);
	ImageView getImageView();
	void setImageView(ImageView imageView, Image image);
	public static boolean isOver(GameObject obj1, GameObject obj2) {
		if(obj2.getX()<=obj1.getX() +obj1.getWidth() 
			&& obj2.getY()<=obj1.getY() +obj1.getHeight() 
			&& obj2.getX() + obj2.getWidth()>=obj1.getX() 
			&& obj2.getY() + obj2.getHeight()>= obj1.getY())
		{
			return true;
		}
		return false;
	}
}
