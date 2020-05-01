package process;

import queue.Queue;

public class Server<E> extends Queue<E> {
	
	private boolean status;
	
	private int entered;
	private int served;
	private int balked;
	private int reneged;
	private int jockeyed;
	
	public Server() {
		status = true;
		served = 0;
		balked = 0;
		reneged = 0;
		jockeyed = 0;
	}
	
	public Server(boolean status) {
		super();
		this.status = status;
	}
	
	public void closeServer() {
		status = false;
	}
	
	public void openServer() {
		status = true;
	}
	
	public boolean isOpen() {
		return status;
	}
	
	public void incEntered() {
		entered++;
	}
	
	public int getEntered() {
		return entered;
	}
	
	public void incServed() {
		served++;
	}
	
	public int getServed() {
		return served;
	}
	
	public void incBalked() {
		balked++;
	}
	
	public int getBalked() {
		return balked;
	}
	
	public void incReneged() {
		reneged++;
	}
	
	public int getReneged() {
		return reneged;
	}
	
	public void incJockeyed() {
		jockeyed++;
	}
	
	public int getJockeyed() {
		return jockeyed;
	}
}
