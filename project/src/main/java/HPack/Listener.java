package HPack;


public interface Listener {
	void updateTrapIcon(boolean trapIcon);
	void setStats(Hunter hunter);
	void updateDays(int days);
	void updateYears(int years);
	public void addObject(GameObject obj);
	public void removeObject(GameObject obj);
}
