
public class ParkingSpace {

	private boolean outOfOrder;
	private boolean occupied;
	private int timeLimit;
	private int initTime;
	public ParkingSpace() {
		occupied = false;
		outOfOrder = false;
	}
	public int getInitTime() {
		return initTime;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	public boolean isOutOfOrder() {
		return outOfOrder;
	}
	
	public boolean passMinute() {
		if(occupied) {
			if((timeLimit = timeLimit - 1) < 0){
				return false;
			}
		}
		return true;
	}
	
	public void occupy(int timeLimit) {
		occupied = true;
		this.timeLimit = timeLimit;
		initTime = timeLimit;
	}
	
	public int getTimeRemaining() {
		return timeLimit;
	}
	
	public void vacate() {
		occupied = false;
	}
	
	public void deactivate() {
		outOfOrder = true;
	}
	
	public void activate() {
		outOfOrder = false;
	}
	
	public String toString() {
		return "space";
	}
}
