package HPack;

import java.util.List;

public interface ToFileInterface {
	public void write(List<GameObject> objs, Hunter hunter, double time, int days, int years);
	public List<GameObject> readObjects();
	public Hunter readHunter();
	public int readDays();
	public int readYears();
	public double readTime();
	public boolean isLegalFile();
}
