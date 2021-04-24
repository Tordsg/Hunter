package HPack;

import java.util.List;

public interface Listener {
	void updateTrapIcon(boolean trapIcon);
	void addObjects(List<GameObject> objects);
	void removeObjects(List<GameObject> objects);
	void setStats(Hunter hunter);
	void updateDays(int days);
	void updateYears(int years);
	public void addObject(GameObject obj);
	public void removeObject(GameObject obj);
}
