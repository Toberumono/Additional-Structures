package toberumono.structures.stacks;

import java.util.Stack;

/**
 * Links two stacks such that when an element is popped from one stack, it is pushed onto the other.
 * 
 * @author Toberumono
 * @param <T>
 *            the type of element contained in this <tt>PairedStack</tt>
 */
public class PairedStack<T> {
	private final Stack<T> left, right;
	
	/**
	 * Initializes a new, empty <tt>PairedStack</tt>
	 */
	public PairedStack() {
		left = new Stack<>();
		right = new Stack<>();
	}
	
	/**
	 * Initializes a new <tt>PairedStack</tt> with the items in basis
	 * 
	 * @param basis
	 *            the <tt>PairedStack</tt> to get the initial items from
	 */
	public PairedStack(PairedStack<T> basis) {
		this();
		for (T item : basis.left)
			left.push(item);
		for (T item : basis.right)
			right.push(item);
	}
	
	/**
	 * Takes the topmost item on the left stack and moves it to the top of the right stack.
	 * 
	 * @return the item that was just moved
	 */
	public T shiftRight() {
		T item = left.pop();
		right.push(item);
		return item;
	}
	
	/**
	 * Takes the topmost item on the right stack and moves it to the top of the left stack.
	 * 
	 * @return the item that was just moved
	 */
	public T shiftLeft() {
		T item = right.pop();
		left.push(item);
		return item;
	}
	
	/**
	 * This does not modify the position of any item in either stack.
	 * 
	 * @return the topmost item on the right stack
	 */
	public T peekRight() {
		return right.peek();
	}
	
	/**
	 * This does not modify the position of any item in either stack.
	 * 
	 * @return the topmost item on the left stack
	 */
	public T peekLeft() {
		return left.peek();
	}
	
	/**
	 * Adds a new item to the top of the right stack
	 * 
	 * @param item
	 *            the item to add
	 */
	public void pushRight(T item) {
		right.push(item);
	}
	
	/**
	 * Adds a new item to the top of the left stack
	 * 
	 * @param item
	 *            the item to add
	 */
	public void pushLeft(T item) {
		left.push(item);
	}
	
	/**
	 * Removes the topmost item from the right stack
	 * 
	 * @return the removed item
	 */
	public T popRight() {
		return right.pop();
	}
	
	/**
	 * Removes the topmost item from the right stack
	 * 
	 * @return the removed item
	 */
	public T popLeft() {
		return left.pop();
	}
	
	/**
	 * @return the size of the right stack
	 */
	public int getSizeRight() {
		return right.size();
	}
	
	/**
	 * @return the size of the left stack
	 */
	public int getSizeLeft() {
		return left.size();
	}
	
	/**
	 * @return the right stack
	 */
	public Stack<T> getRight() {
		return right;
	}
	
	/**
	 * @return the left stack
	 */
	public Stack<T> getLeft() {
		return left;
	}
}
