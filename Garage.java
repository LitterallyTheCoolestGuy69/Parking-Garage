import java.io.IOException;
import java.util.ArrayList;

public class Garage {

	private ArrayList<Floor> floors;
	public Garage() {
		floors = new ArrayList<Floor>();
		ArrayList<ParkingSpace> ps = new ArrayList<ParkingSpace>();
		for(int i = 0; i < 8; i++) {
			ps.add(new ParkingSpace());
		}
		floors.add(new Floor(ps));
	}
	
	public void addFloor() {
		ArrayList<ParkingSpace> ps = new ArrayList<ParkingSpace>();
		for(int i = 0; i < floors.get(floors.size() - 1).getSpaces().size(); i++) {
			ps.add(new ParkingSpace());
		}
		floors.add(new Floor(ps));
	}
	
	public void addFloors(int amt) {
		for(int i = 0; i < amt; i++) {
			addFloor();
		}
	}
	
	public boolean removeFloor(int index) {
		try {
			floors.remove(index);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public int floorCount() {
		return floors.size();
	}
	
	public ArrayList<Floor> getFloors(){
		return floors;
	}
	/*
	public String assignAvailableSpace() throws IOException {
		
	}
	*/
}
