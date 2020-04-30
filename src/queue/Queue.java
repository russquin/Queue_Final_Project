package queue;

import java.util.LinkedList;
import java.util.List;

/**
 * A Queue using a LinkedList. Position 0 is front.
 */
public class Queue<E> implements QueueADT<E> {

	List<E> values = new LinkedList<>();

	public void add(E value) {
		values.add(value);
	}

	public E peek() {
		if (values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}

	public E remove() {
		return values.remove(0);
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	public void clear() {
		values.clear();
	}
	
	public String toString() {
		String output = "";
		output = output + "[";
		if (values.size() == 0) {
			return "[]";
		}
		output = output + values.get(0);
		for (int i = 1; i < values.size(); i++) {
			output = output + "," + values.get(i);
		}
		return output + "]";
	}
}
