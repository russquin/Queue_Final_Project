package customer;

public class Customer {
	
	private int id;
	private double arriveTime;
	private double startServeTime;
	private Item[] items;
	
	public Customer() {
		id = 0;
		arriveTime = 0.0;
		startServeTime = 0.0;
		items = new Item[15];
	}
	
	public Customer(int id, double arriveTime, double startServeTime, Item[] items) {
		super();
		this.id = id;
		this.arriveTime = arriveTime;
		this.startServeTime = startServeTime;
		this.items = items;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
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
	
	public void setItems(Item[] items) {
		this.items = items;
	}
	
	public Item[] getItems() {
		return items;
	}
	
	public String toString() {
		return "Customer " + id + ", " + items + " items";
	}
}
