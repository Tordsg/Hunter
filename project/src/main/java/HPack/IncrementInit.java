package HPack;

public class IncrementInit {
	private double time = 0;
	private double lastInit = 0;
	private double increment;
	public IncrementInit(int increment){
		this.increment = increment;
	}
	public boolean update(double delta) {
		time += delta;
		if(time > lastInit + increment) {
			lastInit = time;
			return true;
		}
		return false;
	}
	public double getTime() {
		return time;
	}
	public void time(double time) {
		this.time = time;
	}
	public double getIncrement() {
		return increment;
	}
	public void setIncrement(double increment) {
		this.increment = increment;
	}
	
}
