package HPack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ToFile implements ToFileInterface{
	private List<GameObject> objects = new ArrayList<GameObject>();
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
			 objs.forEach(o -> {
				 if(!o.getType().equals("trapHitBox"))
					try {
						writer.write(
							o.getClass().getSimpleName() + "," + 
							o.getType() + "," + 
							o.getImage() + "," + 
							o.getX() + "," + 
							o.getY() + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
			 });
			 writer.write(
					 hunter.getClass().getSimpleName() + "," + 
					 hunter.getType() + "," +
					 hunter.getImage() + "," + 
					 hunter.getX() + "," + 
					 hunter.getY() + "," + 
					 days + "," + 
					 years + "," + 
					 hunter.getHealth() + "," + 
					 hunter.getHunger() + "," + 
					 hunter.getThirst() + "," + 
					 time);
			 writer.close();
		} catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	public List<GameObject> readObjects() {
		try {
			boolean trap = false;
			objects.clear();
			makeFile();
		    Scanner scanner = new Scanner(new File(path));
			while(scanner.hasNextLine()) {
				 String objInfo = scanner.nextLine();
				 String[] line = objInfo.split(",");
				
				 switch(line[0]) {
					 case "Item": Item item = new Item();
							 item.setType(line[1]); 
							 item.setImage(line[2]);
							 item.setX(Double.parseDouble(line[3]));
							 item.setY(Double.parseDouble(line[4]));
							 if(trap!=false && line[1].equals("trap")) break; 
							 if(line[1].equals("trap")) trap = true;
							 objects.add(item);
					 		 break;
					 case "DynamicAnimal": DynamicAnimal dynamicAnimal = new DynamicAnimal();
					 		 dynamicAnimal.setType(line[1]);
					 		 dynamicAnimal.setImage(line[2]);
					 		 dynamicAnimal.setPosition(Double.parseDouble(line[3]), Double.parseDouble(line[4]));
					 		 objects.add(dynamicAnimal);
					 		 break;
					 }
			 }
			 scanner.close();
		} catch (FileNotFoundException e) {
		      e.printStackTrace();
		}
		return objects;
	}
	public Hunter readHunter() {
		String[] line = fetchLastLine();
		Hunter hunter = new Hunter(Double.parseDouble(line[3]),Double.parseDouble(line[4]));
		hunter.setHealth(Double.parseDouble(line[7]));
		hunter.setHunger(Double.parseDouble(line[8]));
		hunter.setThirst(Double.parseDouble(line[9]));
		return hunter;
	}
	public int readDays() {
		String[] line = fetchLastLine();
		int days = Integer.parseInt(line[5]);
		return days;
	}
	public int readYears() {
		String[] line = fetchLastLine();
		int days = Integer.parseInt(line[6]);
		return days;
	}
	public double readTime() {
		String[] line = fetchLastLine();
		double time = Double.parseDouble(line[10]);
		return time;
	}
	public boolean isLegalFile() {
		try {
			 makeFile();
			 Scanner scanner = new Scanner(new File(path));
			 if(!scanner.hasNextLine()) return false;
			 while(scanner.hasNextLine()) {
				 String objInfo = scanner.nextLine();
				 String[] line = objInfo.split(",");
				 if(!islegalLine(line)) return false;
					 
			}
			} catch(IOException e) {
				e.printStackTrace();
			}
		if(fetchLastLine() == null) return false;
		if(!fetchLastLine()[0].equals("Hunter")) return false;
		return true;
	}
	private boolean islegalLine(String[] line) {
		if(line.length<5) return false;
		String type = line[1];
		String image = line[2];
		double x,y,health,hunger,thirst,time;
		int days,years;
		try {
		 x = Double.parseDouble(line[3]);
		 y = Double.parseDouble(line[4]);
		}catch(NumberFormatException e) {
			return false;
		}
		switch(line[0]) {
		case "Hunter": {
			if(line.length<11) return false;
			if(!line[1].equals("hunter")) return false;
			if(!image.equals("hunterD") && !image.equals("hunterU") && !image.equals("hunterL") && !image.equals("hunterR")) return false;
			if(x<50 || x>628) return false;
			if(y<0 || y>565) return false;
			try {
				 days = Integer.parseInt(line[5]);
				 years = Integer.parseInt(line[6]);
				 health = Double.parseDouble(line[7]);
				 hunger = Double.parseDouble(line[8]);
				 thirst = Double.parseDouble(line[9]);
				 time = Double.parseDouble(line[10]);
			} catch(NumberFormatException e) {
			return false;
			}
			if(days<0 || days>364) return false;
			if(years<0) return false;
			if(health<0 || health>62) return false;
			if(hunger<0 || hunger>62) return false;
			if(thirst<0 || thirst>62) return false;
			if(time<0) return false;
			break;
		}
		case "Item": {
			if(!type.equals(image) || (!type.equals("water") && !type.equals("trap") && !type.equals("rabbitMeat"))) return false;
			if(x<50 || x>626) return false;
			if(y<0 || y>576) return false;
			break;
		}
		case "DynamicAnimal":{
			if(type.equals("bird")) {
				if(!image.equals("bird") && !image.equals("birdU") && !image.equals("bird2") && !image.equals("birdD")) return false;
			} else if(type.equals("rabbit")) {
				if(!image.equals("rabbit") && !image.equals("rabbitU") && !image.equals("rabbitD")) return false;
			} else return false;
			if(x<22 || x>650) return false;
			if(y<0 || y>572) return false;
			break;
		}
		default: return false;
		}
		return true;
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
