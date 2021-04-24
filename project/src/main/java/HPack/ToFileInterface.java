package HPack;

import java.util.List;

public interface ToFileInterface {
	void write(List<GameObject> objs, Hunter hunter, double time, int days, int years);
	List<GameObject> readObjects();
	Hunter readHunter();
	int readDays();
	int readYears();
	double readTime();
}
