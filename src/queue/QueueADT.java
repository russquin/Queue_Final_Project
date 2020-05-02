package queue;

/**
 * A first-in first-out list
 */
public interface QueueADT<E> {

	/**
	 * Put the given value at the end of the queue
	 */
	void add(E value);

	/**
	 * @return the value at the front of the queue, or null if empty
	 */
	E peek();
	
	/**
	 * @return the value at the given position
	 */
	E get(int position);

	/**
	 * Remove the value at the front of the queue
	 * 
	 * @precondition queue is not empty
	 * @return the removed value
	 */
	E remove();
	
	/**
	 * Remove the value at the given position
	 * 
	 * @return the removed value
	 */
	E removeAtPosition(int position);

	/**
	 * @return true iff this queue is empty
	 */
	boolean isEmpty();

	/**
	 * @return the size of this queue
	 */
	int size();

	/**
	 * Clear this queue
	 */
	void clear();
}
