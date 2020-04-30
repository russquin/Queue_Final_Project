package process;

import queue.Queue;

public class Server<E> extends Queue<E> {
	
	private boolean status;
	
	public Server() {
		status = true;
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
}
