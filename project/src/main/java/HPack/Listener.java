package HPack;


public interface Listener {
	public void updateTrapIcon(boolean trapIcon);
	public void setStats(Hunter hunter);
	public void updateDays(int days);
	public void updateYears(int years);
	public void addObject(GameObject obj);
	public void removeObject(GameObject obj);
}
