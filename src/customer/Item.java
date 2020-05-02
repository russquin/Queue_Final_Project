package customer;

public class Item {
	
	private int id;
	private double processTime;
	
	public Item() {
		id = 0;
		processTime = 0;
	}
	
	public Item(int id, double processTime) {
		super();
		this.id = id;
		this.processTime = processTime;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setProcessTime(double processTime) {
		this.processTime = processTime;
	}
	
	public double getProcessTime() {
		return processTime;
	}
	
	public String toString() {
		return "Item " + id + ", Process Time: " + processTime + " seconds";
	}

}
