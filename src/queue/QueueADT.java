package queue;

/**
 * A first-in first-out list
 */
public interface QueueADT<E> {

	void add(E value);

	E peek();

	E remove();

	boolean isEmpty();

	void clear();
}
