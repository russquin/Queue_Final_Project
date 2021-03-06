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
	
	public E get(int position) {
		if (position >= size()) {
			return null;
		}
		return values.get(position);
	}

	public E remove() {
		return values.remove(0);
	}
	
	public E removeAtPosition(int position) {
		if (values.get(position) != null) {
			return values.remove(position);
			
		}
		return null;
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	public int size() {
		return values.size();
	}
	
	public void clear() {
		values.clear();
	}
	
	public String toString() {
		String output = "";
		output = output + "[";
		if (values.size() == 0) {
			return "[ No customers in queue ]";
		}
		output = output + values.get(0);
		for (int i = 1; i < values.size(); i++) {
			output = output + " | " + values.get(i);
		}
		return output + "]";
	}
}
