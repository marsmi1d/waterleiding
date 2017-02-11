package nl.groningen;

public class GkbTransaction {
	
	private String thingID;
	private String userID;
	private String someProperty;
	private String afnemerID;
	private int bedrag;
	
	public String getThingID() {
		return thingID;
	}
	
	public void setThingID(String thingID) {
		this.thingID = thingID;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getSomeProperty() {
		return someProperty;
	}
	
	public void setSomeProperty(String someProperty) {
		this.someProperty = someProperty;
	}
	
	public String getAfnemerID() {
		return afnemerID;
	}
	
	public void setAfnemerID(String afnemerID) {
		this.afnemerID = afnemerID;
	}
	
	public int getBedrag() {
		return bedrag;
	}
	
	public void setBedrag(int bedrag) {
		this.bedrag = bedrag;
	}
	
}
