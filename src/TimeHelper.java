public class TimeHelper {
	private long previousTime;

	public TimeHelper() {
		this.previousTime = System.currentTimeMillis();
	}

	public boolean hasReached(double time) {
		return currentTime() >= time;
	}

	public float currentTime() {
		return (float) (System.currentTimeMillis() - this.previousTime);
	}

	public void reset() {
		this.previousTime = System.currentTimeMillis();
	}
}