package HPack;

public class IncrementInit {
	private double time = 0;
	private double lastInit = 0;
	private double increment;
	public IncrementInit(int increment){
		this.increment = increment;
	}
	public boolean update(double delta) {
		if(delta>=0) {
		time += delta;
		}
		if(time >= lastInit + increment) {
			lastInit = time;
			return true;
		}
		return false;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		if(time>0) {
		this.time = time;
		}
	}
	public double getIncrement() {
		return increment;
	}
	public void setIncrement(double increment) {
		if(increment>0) {
		this.increment = increment;
		}
	}
}
