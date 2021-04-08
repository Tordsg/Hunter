package HPack;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.scene.image.ImageView;

public class ToFile{
	Game game;
	ToFile(Game game) {
		this.game = game;

	}
	File file = new File("pausedFile.txt");
	public void write() {
		try {
			 FileWriter writer = new FileWriter(file);
			 for(GameObject obj: game.getObjects()) {
				 String type = obj.getType();
				 String x = Double.toString(obj.getX());
				 String y = Double.toString(obj.getY());
				 String URL = "hunterD";
				 switch(obj.getImageView().getImage().getUrl().length()) {
				 case 94: URL = obj.getImageView().getImage().getUrl().substring(83, 94-4);break;
				 case 93: URL = obj.getImageView().getImage().getUrl().substring(83, 93-4);break;
				 case 92: URL = obj.getImageView().getImage().getUrl().substring(83, 92-4);break;
				 case 91: URL = obj.getImageView().getImage().getUrl().substring(83, 91-4);break;
				 }
					 
		
				 String objInfo = type + "," + x + "," + y+","+URL+"\n";
				 writer.write(objInfo);
			 }
			 double time = game.timer.getTime();
			 writer.write(Double.toString(time));
			 writer.close();
			 System.out.println("Fil stored");
			 
		} catch (IOException e) {
		      System.out.println("Could not write to file.");
		      e.printStackTrace();
		    }
	}
	public void read() {
		try {
			 Scanner scanner = new Scanner(file);
			 game.getObjects().clear();
			 game.getDynamicAnimals().clear();
			 game.controller.gamePane.getChildren().clear();
			 while(scanner.hasNextLine()) {
				 String objInfo = scanner.nextLine();
				 String[] line = objInfo.split(",");
				
				 if(!scanner.hasNextLine()) {
					 double time = Double.parseDouble(objInfo);
					 game.timer.setTime(time);
				 }
					 switch(line[0]) {
					 case "hunter": Hunter hunter = new Hunter(Double.parseDouble(line[1]), Double.parseDouble(line[2]), new ImageView(), game.controller.images.get(line[3])); game.getObjects().add(hunter); break;
					 case "water": Item item = new Item(); item.setImageView(new ImageView(), game.controller.images.get(line[0])); item.setX(Double.parseDouble(line[1])); item.setY(Double.parseDouble(line[2])); item.getImageView().toBack(); item.setType(line[0]); game.getObjects().add(item); break;
					 case "bird": DynamicAnimal dynamicAnimal = new DynamicAnimal(); dynamicAnimal.setImageView(new ImageView(), game.controller.images.get(line[3]));dynamicAnimal.setPosition(Double.parseDouble(line[1]), Double.parseDouble(line[2])); dynamicAnimal.setType(line[0]);game.getObjects().add(dynamicAnimal); game.getDynamicAnimals().add(dynamicAnimal); break;
					 case "trap": Item trap = new Item(); trap.setImageView(new ImageView(), game.controller.images.get(line[0]));trap.setX(Double.parseDouble(line[1])); trap.setY(Double.parseDouble(line[2])); trap.setType(line[0]); trap.getImageView().toBack(); game.getObjects().add(trap); break;
					 }
				  
			 }
			 scanner.close();
			 System.out.println("File read");
		} catch (IOException e) {
		      System.out.println("Could not read file.");
		      e.printStackTrace();
		}
	}
}
