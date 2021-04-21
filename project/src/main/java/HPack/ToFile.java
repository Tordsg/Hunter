package HPack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.scene.image.ImageView;

public class ToFile implements ToFileInterface{
	private Game game;
	ToFile(Game game) {
		this.game = game;
	}
	
	String path = "src/main/resources/gameState.txt";
	

	File file;
	public void makeFile() {
		File file = new File(path);
		try {
			file.createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void write() {
		try {
			 makeFile();
			 FileWriter writer = new FileWriter(path);
			 for(GameObject obj: game.getObjects()) {
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
			 writer.write(game.getHunter().getClass().getSimpleName().toString() + "," + 
					 game.getHunter().getX() + "," + 
					 game.getHunter().getY() + "," + 
					 game.getHunter().getImageView().getImage().getUrl().substring(75,game.getHunter().getImageView().getImage().getUrl().length()-4) + "," + 
					 game.getController().getDays().getText().toString() + "," + 
					 game.getController().getYears().getText().toString() + "," + 
					 game.getHunter().getHealth() + "," + 
					 game.getHunter().getHunger() + "," + 
					 game.getHunter().getThirst() + "," + 
					 game.getTimer().getTime());
			 writer.close();
			 System.out.println("Fil stored");
			 
		} catch (IOException e) {
		      System.out.println("Could not write to file.");
		      e.printStackTrace();
		    }
	}
	public void read() {
		try {
			 makeFile();
			 Scanner scanner = new Scanner(new File(path));
			 game.getObjects().forEach(o -> game.getController().getGamePane().getChildren().remove(o.getImageView()));
			 game.getController().getGamePane().getChildren().remove(game.getHunter().getImageView());
			 game.getObjects().clear();
			 game.getDynamicAnimals().clear();
			 while(scanner.hasNextLine()) {
				 String objInfo = scanner.nextLine();
				 String[] line = objInfo.split(",");
				
					 switch(line[0]) {
					 case "Hunter": Hunter hunter = new Hunter(Double.parseDouble(line[1]), Double.parseDouble(line[2]), new ImageView(), HunterController.getImages().get(line[3])); 
					 		 game.getController().setDays(line[4]);
					 		 game.getController().setYears(line[5]);
					 		 game.getTimer().setDays(Integer.parseInt(line[4]));
					 		 game.getTimer().setYears(Integer.parseInt(line[5]));
					 		 hunter.setHealth(Double.parseDouble(line[6])); game.getController().setHealth(Double.parseDouble(line[6]));
							 hunter.setHunger(Double.parseDouble(line[7])); game.getController().setHunger(Double.parseDouble(line[7]));
							 hunter.setThirst(Double.parseDouble(line[8])); game.getController().setThirst(Double.parseDouble(line[8]));
							 game.getTimer().setTime(Double.parseDouble(line[9]));
							 game.setHunter(hunter); 
							 break;
					 case "Item": game.initGameObject(line[1], Double.parseDouble(line[2]), Double.parseDouble(line[3]));
					 		 break;
					 case "DynamicAnimal": DynamicAnimal dynamicAnimal = new DynamicAnimal();
					 		 dynamicAnimal.setType(line[1]);
					 		 dynamicAnimal.setImageView(new ImageView(), HunterController.getImages().get(line[4]));
					 		 dynamicAnimal.setPosition(Double.parseDouble(line[2]), Double.parseDouble(line[3]));
					 		 game.getObjects().add(dynamicAnimal); 
					 		 game.getDynamicAnimals().add(dynamicAnimal); 
					 		 break;
					 }
			 }
			 scanner.close();
			 System.out.println("File loaded");
		} catch (FileNotFoundException e) {
			  
		      System.out.println("Could not load file.");
		      e.printStackTrace();
		}
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
