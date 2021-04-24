package HPack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.image.ImageView;

public class ToFile implements ToFileInterface{
	List<GameObject> objects = new ArrayList<GameObject>();
	Hunter hunter;
	private String path = "src/main/resources/gameState.txt";
	
	public void makeFile() {
		File file = new File(path);
		try {
			file.createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(List<GameObject> objs, Hunter hunter, double time, int days, int years) {
		try {
			 makeFile();
			 FileWriter writer = new FileWriter(path);
			 for(GameObject obj: objs) {
				 if(obj.getType().equals("trapHitBox")) continue;
				 String c = obj.getClass().getSimpleName().toString();
				 String type = obj.getType();
				 String x = Double.toString(obj.getX());
				 String y = Double.toString(obj.getY());
				 String image = "";
				 image = obj.getImageView().getImage().getUrl().substring(75,obj.getImageView().getImage().getUrl().length()-4);
				 String objInfo = c + "," + type + "," + x + "," + y+","+image+"\n";
				 writer.write(objInfo);
			 }
			 writer.write(hunter.getClass().getSimpleName().toString() + "," + 
					 hunter.getX() + "," + 
					 hunter.getY() + "," + 
					 hunter.getImageView().getImage().getUrl().substring(75,hunter.getImageView().getImage().getUrl().length()-4) + "," + 
					 days + "," + 
					 years + "," + 
					 hunter.getHealth() + "," + 
					 hunter.getHunger() + "," + 
					 hunter.getThirst() + "," + 
					 time);
			 writer.close();
			 System.out.println("Fil stored");
			 
		} catch (IOException e) {
		      System.out.println("Could not write to file.");
		      e.printStackTrace();
		    }
	}
	public List<GameObject> readObjects() {
		try {
			objects.clear();
			makeFile();
		    Scanner scanner = new Scanner(new File(path));
			while(scanner.hasNextLine()) {
				 String objInfo = scanner.nextLine();
				 String[] line = objInfo.split(",");
				
				 switch(line[0]) {
					 case "Item": Item item = new Item();
							 item.setType(line[1]); 
							 item.setImageView(new ImageView(), HunterController.getImages().get(line[1]));
							 item.setX(Double.parseDouble(line[2]));
							 item.setY(Double.parseDouble(line[3]));
							 objects.add(item);
					 		 break;
					 case "DynamicAnimal": DynamicAnimal dynamicAnimal = new DynamicAnimal();
					 		 dynamicAnimal.setType(line[1]);
					 		 dynamicAnimal.setImageView(new ImageView(), HunterController.getImages().get(line[4]));
					 		 dynamicAnimal.setPosition(Double.parseDouble(line[2]), Double.parseDouble(line[3]));
					 		 objects.add(dynamicAnimal);
					 		 break;
					 }
			 }
			 scanner.close();
			 System.out.println("Objects read");
		} catch (FileNotFoundException e) {
		      System.out.println("Could not load file.");
		      e.printStackTrace();
		}
		return objects;
	}
	public Hunter readHunter() {
		String[] line = fetchLastLine();
		Hunter hunter = new Hunter(Double.parseDouble(line[1]),Double.parseDouble(line[2]), new ImageView(), HunterController.getImages().get(line[3]));
		hunter.setHealth(Double.parseDouble(line[6]));
		hunter.setHunger(Double.parseDouble(line[7]));
		hunter.setThirst(Double.parseDouble(line[8]));
		return hunter;
	}
	public int readDays() {
		String[] line = fetchLastLine();
		int days = Integer.parseInt(line[4]);
		return days;
	}
	public int readYears() {
		String[] line = fetchLastLine();
		int days = Integer.parseInt(line[5]);
		return days;
	}
	public double readTime() {
		String[] line = fetchLastLine();
		double time = Double.parseDouble(line[9]);
		return time;
	}
	private String[] fetchLastLine() {
		try {
			 makeFile();
			 Scanner scanner = new Scanner(new File(path));
			 while(scanner.hasNextLine()) {
				 String objInfo = scanner.nextLine();
				 String[] line = objInfo.split(",");
				 if(!scanner.hasNextLine()) {
					 return line;
				 }
					 
			}
			} catch(IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
