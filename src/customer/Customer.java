package customer;

public class Customer {
	
	private String id;
	private int items;
	private double arriveTime;
	private double startServeTime;
	
	public Customer() {
		id = "";
		items = 0;
		arriveTime = 0.0;
		startServeTime = 0.0;
	}
	
	public Customer(String id, int items, double arriveTime, double startServeTime) {
		super();
		this.id = id;
		this.items = items;
		this.arriveTime = arriveTime;
		this.startServeTime = startServeTime;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setItems(int items) {
		this.items = items;
	}
	
	public int getItems() {
		return items;
	}
	
	public void setArriveTime(double arriveTime) {
		this.arriveTime = arriveTime;
	}
	
	public double getArriveTime() {
		return arriveTime;
	}
	
	public void setStartServeTime(double startServeTime) {
		this.startServeTime = startServeTime;
	}
	
	public double getStartServeTime() {
		return startServeTime;
	}
}
