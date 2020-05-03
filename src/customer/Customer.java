package customer;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	
	private int id;
	private double arriveTime;
	private double waitTime;
	private double startServeTime;
	private double serveTime;
	private double customerTT; //Customer total time
	private List<Item> items;
	
	public Customer() {
		id = 0;
		arriveTime = 0.0;
		waitTime = 0.0;
		startServeTime = 0.0;
		serveTime = 0.0;
		items = new ArrayList<>();
	}
	
	public Customer(int id, double arriveTime, List<Item> items) {
		super();
		this.id = id;
		this.arriveTime = arriveTime;
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
	
	public void setWaitTime(double waitTime) {
		this.waitTime = waitTime;
	}
	
	public double getWaitTime() {
		return waitTime;
	}
	
	public void setStartServeTime(double startServeTime) {
		this.startServeTime = startServeTime;
	}
	
	public double getStartServeTime() {
		return startServeTime;
	}
	
	public void setServeTime(double serveTime) {
		this.serveTime = serveTime;
	}
	
	public double getServeTime() {
		return serveTime;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}

	public double getCustomerTT(){
		this.customerTT = (this.waitTime + this.serveTime) - this.arriveTime;
		return this.customerTT;

	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public String toString() {
		return "Customer " + id + ", " + items.size() + " items";
	}
}
