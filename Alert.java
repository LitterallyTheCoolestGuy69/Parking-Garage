
public class Alert {

	private int min;
	private String space;
	private String alert;
	public Alert(int minutes, String space) {
		min = minutes;
		this.space = space;
		alert = "Car parked in space " + space + " has overparked by " + minutes + " minutes";
	}
	
	public String getAlert() {
		return alert;
	}
	public int getTime() {
		return min;
	}
}
