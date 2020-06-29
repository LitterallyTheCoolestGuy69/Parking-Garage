import java.util.ArrayList;

public class Floor {
	
	private ArrayList<ParkingSpace> spaces;
	public Floor() {
		spaces = new ArrayList<ParkingSpace>();
	}

	public Floor(ArrayList<ParkingSpace> spaces) {
		this.spaces = spaces;
	}
	
	public void addSpace() {
		spaces.add(new ParkingSpace());
	}
	
	public void addSpaces(int amt) {
		for(int i = 0; i < amt; i++) {
			addSpace();
		}
	}
	
	public void removeSpace(int space) {
		spaces.remove(space);
	}
	
	public int spaceCount() {
		return spaces.size();
	}
	
	public ArrayList<ParkingSpace> getSpaces(){
		return spaces;
	}
	
	public String toString() {
		return spaces.toString();
	}
}
