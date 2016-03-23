package toberumono.structures.sexpressions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementation of a combination doubly-linked list and s-expression structure based on cons cells from Lisp.
 * 
 * @author Toberumono
 */
public class ConsCell implements Cloneable, Iterable<ConsCell> {
	private Object car, cdr;
	private ConsType carType, cdrType;
	private ConsCell previous;
	
	/**
	 * Constructs an empty {@link ConsCell}.
	 */
	public ConsCell() {
		this.car = null;
		this.carType = getEmptyType();
		this.cdr = null;
		this.cdrType = getEmptyType();
		this.previous = null;
	}
	
	/**
	 * Constructs a {@link ConsCell} with an empty {@code cdr} value.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link ConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 */
	public ConsCell(Object car, ConsType carType) {
		this.car = car;
		this.carType = carType == null ? getEmptyType() : carType;
		this.cdr = null;
		this.cdrType = getEmptyType();
		this.previous = null;
	}
	
	/**
	 * Constructs a {@link ConsCell} with a {@link ConsCell} as its {@code cdr} value.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link ConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 * @param cdr
	 *            the {@code cdr} value of the {@link ConsCell}
	 */
	public ConsCell(Object car, ConsType carType, ConsCell cdr) {
		this.car = car;
		this.carType = carType == null ? getEmptyType() : carType;
		this.cdr = cdr;
		this.cdrType = getConsCellType();
		cdr.setPrevious(this);
		this.previous = null;
	}
	
	/**
	 * Constructs a {@link ConsCell} with the given {@code car} and {@code cdr} values.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link ConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 * @param cdr
	 *            the {@code cdr} value of the {@link ConsCell}
	 * @param cdrType
	 *            the {@link ConsType type} of the {@code cdr} value
	 */
	public ConsCell(Object car, ConsType carType, Object cdr, ConsType cdrType) {
		this.car = car;
		this.carType = carType == null ? getEmptyType() : carType;
		this.cdr = cdr;
		this.cdrType = cdrType == null ? getEmptyType() : cdrType;
		if (cdr instanceof ConsCell)
			((ConsCell) cdr).setPrevious(this);
		this.previous = null;
	}
	
	/**
	 * @return the {@code car} value of the {@link ConsCell} (the value specified by the left pointer)
	 * @see #getCarType()
	 * @see #getCdr()
	 */
	public Object getCar() {
		return car;
	}
	
	/**
	 * @return the {@link ConsType type} of the {@code car} value of the {@link ConsCell} (the type of the value specified by
	 *         the left pointer)
	 */
	public ConsType getCarType() {
		return carType;
	}
	
	/**
	 * Sets the {@code car} value and type of the {@link ConsCell} to the provided value and type.
	 * 
	 * @param car
	 *            the new {@code car} value
	 * @param carType
	 *            the {@link ConsType type} of the new {@code car} value
	 * @return the {@link ConsCell} for chaining purposes
	 * @see #replaceCar(ConsCell)
	 * @see #setCdr(Object, ConsType)
	 */
	public ConsCell setCar(Object car, ConsType carType) {
		this.car = car;
		this.carType = carType;
		return this;
	}
	
	/**
	 * Replaces the {@code car} value and type of the {@link ConsCell} with those of the provided {@link ConsCell}.
	 * 
	 * @param container
	 *            the {@link ConsCell} containing the {@code car} value and type with which the {@link ConsCell ConsCell's}
	 *            {@code car} value and type will be replaced
	 * @return the {@link ConsCell} for chaining purposes
	 * @see #setCar(Object, ConsType)
	 * @see #replaceCdr(ConsCell)
	 */
	public ConsCell replaceCar(ConsCell container) {
		return setCar(container.getCar(), container.getCarType());
	}
	
	/**
	 * @return the {@code cdr} value of the {@link ConsCell} (the value specified by the right pointer)
	 * @see #getCdrType()
	 * @see #getCar()
	 */
	public Object getCdr() {
		return cdr;
	}
	
	/**
	 * @return the {@link ConsType type} of the {@code cdr} value of the {@link ConsCell} (the type of the value specified by
	 *         the right pointer)
	 */
	public ConsType getCdrType() {
		return cdrType;
	}
	
	/**
	 * Sets the {@code cdr} value and type of the {@link ConsCell} to the provided value and type. If the original
	 * {@code cdr} value was a {@link ConsCell}, then the value of its {@link #getPrevious() previous} field will be set to
	 * null. If the new {@code cdr} value is a {@link ConsCell}, then the {@link #getCdr() cdr} value of the {@link ConsCell}
	 * in its {@link #getPrevious() previous} field will be set to null if its {@link #getPrevious() previous} field was
	 * non-null. The value of the {@link #getPrevious() previous} field of the new {@code cdr} value will be set to the
	 * {@link ConsCell} upon which this method was called if the new {@code cdr} value is a {@link ConsCell}.
	 * 
	 * @param cdr
	 *            the new {@code cdr} value
	 * @param cdrType
	 *            the {@link ConsType type} of the new {@code cdr} value
	 * @return the {@link ConsCell} for chaining purposes
	 * @see #replaceCdr(ConsCell)
	 * @see #setCar(Object, ConsType)
	 */
	public ConsCell setCdr(Object cdr, ConsType cdrType) {
		if (getCdr() == cdr) { //If the current cdr value is the same as the one being set, just update the type
			this.cdrType = cdrType;
			return this;
		}
		if (getCdr() instanceof ConsCell)
			((ConsCell) getCdr()).setPreviousInner(null);
		if (cdr instanceof ConsCell)
			((ConsCell) cdr).setPrevious(this); //Needs to be the full method because the new value could have already had a previous ConsCell
		setCdrInner(cdr, cdrType);
		return this;
	}
	
	/**
	 * This controls how the {@link #getCdr() cdr} and {@link #getCdrType() cdrType} fields are assigned. Overriding classes
	 * that are not using the provided cdr should override this method instead of {@link #setCdr(Object, ConsType)}.
	 * <b>Implementations of this method must <i>not</i> perform any processing beyond simple field assignment.</b>
	 * 
	 * @param cdr
	 *            the new {@code cdr} value
	 * @param cdrType
	 *            the {@link ConsType type} of the new {@code cdr} value
	 */
	protected void setCdrInner(Object cdr, ConsType cdrType) {
		this.cdr = cdr;
		this.cdrType = cdrType;
	}
	
	/**
	 * Replaces the {@code car} value and type of the {@link ConsCell} with those of the provided {@link ConsCell}. If the
	 * original {@code cdr} value was a {@link ConsCell}, then the value of its {@link #getPrevious() previous} field will be
	 * set to null. If the new {@code cdr} value is a {@link ConsCell}, then the {@link #getCdr() cdr} value of the
	 * {@link ConsCell} in its {@link #getPrevious() previous} field will be set to null if its {@link #getPrevious()
	 * previous} field was non-null. The value of the {@link #getPrevious() previous} field of the new {@code cdr} value will
	 * be set to the {@link ConsCell} upon which this method was called if the new {@code cdr} value is a {@link ConsCell}.
	 * 
	 * @param container
	 *            the {@link ConsCell} containing the {@code car} value and type with which the {@link ConsCell ConsCell's}
	 *            {@code car} value and type will be replaced
	 * @return the {@link ConsCell} for chaining purposes
	 * @see #setCdr(Object, ConsType)
	 * @see #replaceCar(ConsCell)
	 */
	public ConsCell replaceCdr(ConsCell container) {
		return setCdr(container.getCdr(), container.getCdrType());
	}
	
	/**
	 * The last {@link ConsCell} is defined as the {@link ConsCell} that has a {@code cdr} value that is not an instance of
	 * {@link ConsCell}.
	 * 
	 * @return the last {@link ConsCell} in the current level of the {@link ConsCell ConsCell's} tree
	 */
	public ConsCell getLast() {
		for (ConsCell current = this;; current = current.getNext())
			if (current.isLast())
				return current;
	}
	
	/**
	 * The next {@link ConsCell} is the {@code cdr} value of the {@link ConsCell} if the {@code cdr} value is an instance of
	 * {@link ConsCell}.
	 * 
	 * @return the next {@link ConsCell} in the tree if there is one, otherwise {@code null}
	 */
	public ConsCell getNext() {
		return getCdr() instanceof ConsCell ? (ConsCell) getCdr() : null;
	}
	
	/**
	 * The next {@link ConsCell} is the {@code cdr} value of the {@link ConsCell} if the {@code cdr} value is an instance of
	 * {@link ConsCell}.
	 * 
	 * @param n
	 *            the number of {@link ConsCell ConsCells} by which to step forward
	 * @return the nth-next {@link ConsCell} in the tree if there is one, otherwise {@code null}
	 */
	public ConsCell getNext(int n) {
		if (n < 0) //If n < 0, this is equivalent to getting the -nth previous ConsCell
			return getPrevious(-n);
		ConsCell out = this;
		for (; n > 0 && out != null; n--)
			out = out.getNext();
		return out;
	}
	
	/**
	 * Sets the next {@link ConsCell} in the {@link ConsCell ConsCell's} tree. This forwards to
	 * {@link #setNext(ConsCell, ConsType)} with the {@link ConsType} from {@link #getConsCellType()}.<br>
	 * <b>Note:</b> the next {@link ConsCell} is still stored in the {@link #getCdr() cdr} value.
	 * 
	 * @param next
	 *            the new next {@link ConsCell}
	 * @return the current {@link ConsCell} (for chaining purposes)
	 * @see #setNext(ConsCell, ConsType)
	 * @see #setCdr(Object, ConsType)
	 */
	public ConsCell setNext(ConsCell next) {
		return setNext(next, getConsCellType());
	}
	
	/**
	 * Sets the next {@link ConsCell} in the {@link ConsCell ConsCell's} tree. This forwards to
	 * {@link #setCdr(Object, ConsType)}.<br>
	 * <b>Note:</b> the next {@link ConsCell} is still stored in the {@link #getCdr() cdr} value.
	 * 
	 * @param next
	 *            the new next {@link ConsCell}
	 * @param type
	 *            the {@link ConsType type} of {@code next}
	 * @return the current {@link ConsCell} (for chaining purposes)
	 */
	public ConsCell setNext(ConsCell next, ConsType type) {
		return setCdr(next, type);
	}
	
	/**
	 * The first {@link ConsCell} is defined as the {@link ConsCell} that has a {@code null} {@link #getPrevious() previous}
	 * field.
	 * 
	 * @return the first {@link ConsCell} in the current level of the {@link ConsCell ConsCell's} tree
	 */
	public ConsCell getFirst() {
		for (ConsCell current = this;; current = current.getPrevious())
			if (current.isFirst())
				return current;
	}
	
	/**
	 * @return the previous (or parent) {@link ConsCell} in the tree
	 */
	public ConsCell getPrevious() {
		return previous;
	}
	
	/**
	 * @param n
	 *            the number of {@link ConsCell ConsCells} by which to step backward
	 * @return the nth-previous (or parent) {@link ConsCell} in the tree
	 */
	public ConsCell getPrevious(int n) {
		if (n < 0) //If n < 0, this is equivalent to getting the -nth next ConsCell
			return getNext(-n);
		ConsCell out = this;
		for (; n > 0 && out != null; n--)
			out = out.getPrevious();
		return out;
	}
	
	protected ConsCell setPrevious(ConsCell previous) {
		if (getPrevious() == previous)
			return this;
		if (getPrevious() != null)
			getPrevious().setCdrInner(null, null);
		setPreviousInner(previous);
		return this;
	}
	
	/**
	 * This controls how the {@link #getPrevious() previous} field is assigned. Overriding classes that are not using the
	 * provided previous field should override this method instead of {@link #setPrevious(ConsCell)}.<br>
	 * <b>Implementations of this method must <i>not</i> perform any processing beyond simple field assignment.</b>
	 * 
	 * @param previous
	 *            the new previous {@link ConsCell}
	 */
	protected void setPreviousInner(ConsCell previous) {
		this.previous = previous;
	}
	
	/**
	 * @return {@code true} iff the {@link ConsCell} does not have a {@link #getPrevious() previous} {@link ConsCell} (that
	 *         is, {@link #getPrevious()} returns {@code null})
	 */
	public boolean isFirst() {
		return getPrevious() == null;
	}
	
	/**
	 * @return {@code true} iff the {@link ConsCell} does not have a {@link #getNext() next} {@link ConsCell} (that is,
	 *         {@link #getNext()} returns {@code null})
	 */
	public boolean isLast() {
		return getNext() == null;
	}
	
	/**
	 * @return {@code true} iff the types {@link ConsCell ConsCell's} {@code car} and {@code cdr} values are both the
	 *         {@link ConsType type} returned by {@link #getEmptyType()}.
	 */
	public boolean isEmpty() {
		return getCarType() == getEmptyType() && getCdrType() == getEmptyType();
	}
	
	/**
	 * Determines whether there are at least {@code n} {@link ConsCell ConsCells} including the one on which the method was
	 * called in the current level of the tree with the {@link ConsCell} on which this method was called as its root. If
	 * {@code n} is negative, it looks backwards instead of forwards. (So, if {@code n} is {@code -1, 0, 1}, the method will
	 * always return {@code true})
	 * 
	 * @param n
	 *            the number of {@link ConsCell ConsCells} to test for
	 * @return {@code true} iff there are at least {@code n} {@link ConsCell ConsCells} in the direction being checked
	 */
	public boolean hasLength(int n) {
		if (n == 0) //If n == 0, then the answer is always true
			return true;
		else if (n > 0) {
			if (--n != 0) //If n == 1, we are effectively checking whether the current ConsCell exists
				for (ConsCell current = this; n > 0 && current != null; n--)
					if ((current = current.getNext()) == null)
						return false;
			return true;
		}
		else { //If n < 0, look backwards
			if (++n != 0) //If n == -1, we are effectively checking whether the current ConsCell exists
				for (ConsCell current = this; n < 0 && current != null; n++)
					if ((current = current.getPrevious()) == null)
						return false;
			return true;
		}
	}
	
	/**
	 * @return the number of {@link ConsCell ConsCells} in the first level tree with the {@link ConsCell} on which this
	 *         method was called as its root
	 */
	public int length() {
		int l = 0;
		for (ConsCell current = this; current != null; current = current.getNext())
			l++;
		return l;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getCar(), getCarType(), getCdr(), getCdrType());
	}
	
	/**
	 * This method appends the given {@link ConsCell} or {@link ConsCell ConsCells} to this one, and, if this cell is null as
	 * defined in {@link #isEmpty()}, overwrites this the contents of this {@link ConsCell} with the first {@link ConsCell}
	 * to be inserted.<br>
	 * If this {@link ConsCell ConsCell's} {@code cdr} value is also a {@link ConsCell}, then {@link #insert(ConsCell)} is
	 * recursively called on the last cell in the inserted tree's root level (found via a call to {@link #getLast()}) with
	 * this {@link ConsCell ConsCell's} {@code cdr} value and type as the arguments. If this {@link ConsCell ConsCell's}
	 * {@code cdr} value is not empty and is not a {@link ConsCell}, then the {@code cdr} value of the last {@link ConsCell}
	 * in the given {@link ConsCell ConsCell's} or {@link ConsCell ConsCells'} {@code cdr} value is set to this
	 * {@link ConsCell ConsCell's} {@code cdr} value.
	 * 
	 * @param next
	 *            the {@link ConsCell} to insert
	 * @return {@code this} if {@code this.isEmpty()} was {@code true}, otherwise {@code next}
	 * @see #append(ConsCell)
	 */
	public ConsCell insert(ConsCell next) {
		if (isEmpty()) {
			replaceCar(next);
			replaceCdr(next);
			return this;
		}
		Object cdr = getCdr();
		ConsType type = getCdrType();
		setNext(next);
		next.getLast().setCdr(cdr, type);
		return next;
	}
	
	/**
	 * Appends the given {@link ConsCell} to the {@link ConsCell} on which this method was called.<br>
	 * Roughly equivalent in function to {@link #insert(ConsCell)} - the only difference is that this returns the last
	 * {@link ConsCell} in the equivalent level of the resulting tree.
	 * 
	 * @param next
	 *            the cell to append
	 * @return the last cell in the equivalent level of the resulting cell tree. Equivalent to
	 *         {@code cell.insert(next).getLastConsCell()}.
	 * @see #insert(ConsCell)
	 */
	public ConsCell append(ConsCell next) {
		if (isEmpty()) {
			replaceCar(next);
			replaceCdr(next);
			return getLast();
		}
		if (getCdr() instanceof ConsCell)
			((ConsCell) getCdr()).setPrevious(null);
		setNext(next);
		return next.getLast();
	}
	
	/**
	 * Removes the {@link ConsCell} from the tree and returns the one after it.
	 * 
	 * @return the next {@link ConsCell} in the tree as determined by {@link #getNext()}
	 * @see #getNext()
	 */
	public ConsCell remove() {
		ConsCell next = getNext();
		if (getPrevious() != null)
			getPrevious().setCdr(getCdr(), getCdrType());
		else if (next != null)
			next.setPreviousInner(null);
		setCdrInner(null, null);
		return next;
	}
	
	/**
	 * Splits the cell tree on this {@link ConsCell}. After the split, the original tree will end at the {@link ConsCell}
	 * before the one on which this method was called, and the {@link ConsCell} on which this method was called will be the
	 * root {@link ConsCell} of the new tree.
	 * 
	 * @return {@code this}
	 */
	public ConsCell split() {
		if (getPrevious() != null)
			getPrevious().setCdr(null, getEmptyType());
		return this;
	}
	
	/**
	 * Returns a shallow copy of this {@link ConsCell} with only the {@code car} value and {@link ConsType type}.<br>
	 * This effectively creates a {@link ConsCell} with a pointer to the same car value as the {@link ConsCell} on which this
	 * method was called but separate from its tree.
	 * 
	 * @return a shallow copy of the {@link ConsCell} that is separate from the tree
	 */
	public ConsCell singular() {
		try {
			ConsCell clone = (ConsCell) super.clone();
			clone.setPreviousInner(null);
			clone.setCdrInner(null, clone.getEmptyType());
			return clone;
		}
		catch (CloneNotSupportedException e) { //This cannot occur
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This method will attempt to clone the car and cdr values via a public clone method (first) and a public copy
	 * constructor (second). If neither the method nor the constructor are available, it will use the existing value.<br>
	 * {@inheritDoc}
	 * 
	 * @return an independent clone of the {@link ConsCell} with the {@link ConsCell} on which the method was called as the
	 *         root of the new tree
	 */
	@Override
	public ConsCell clone() {
		return clone(null);
	}
	
	/**
	 * Clones the structural elements of the {@link ConsCell ConsCell's} tree only. That is, only car and cdr elements that
	 * implement {@link ConsCell} are cloned. All cloning is performed via recursive calls to {@link #structuralClone()} or
	 * {@link #structuralClone(ConsCell)} as needed.
	 * 
	 * @param previous
	 *            the previous {@link ConsCell} in the tree (effectively the cloned {@link ConsCell ConsCell's} parent)
	 * @return an independent clone of the {@link ConsCell} with the given {@code previous} {@link ConsCell} as the parent of
	 *         the new tree
	 */
	public ConsCell clone(ConsCell previous) {
		try {
			ConsCell clone = (ConsCell) super.clone();
			clone.setPreviousInner(previous);
			if (previous != null)
				previous.setCdr(clone, previous.getConsCellType());
			clone.setCar(tryClone(clone.getCar()), clone.getCarType());
			clone.setCdrInner(tryClone(clone.getCdr()), clone.getCdrType());
			if (clone.getCdr() instanceof ConsCell)
				((ConsCell) clone.getCdr()).setPreviousInner(clone);
			return clone;
		}
		catch (CloneNotSupportedException e) { //This cannot occur
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Clones the structural elements of the {@link ConsCell ConsCell's} tree <i>only</i>. That is, only car and cdr elements
	 * that implement {@link ConsCell} are cloned. All cloning is performed via recursive calls to {@link #structuralClone()}
	 * or {@link #structuralClone(ConsCell)} as needed.
	 * 
	 * @return a structurally independent clone of the {@link ConsCell ConsCell's} tree with the {@link ConsCell} on which
	 *         the method was called as the root
	 */
	public ConsCell structuralClone() {
		return structuralClone(null);
	}
	
	/**
	 * Clones the structural elements of the {@link ConsCell ConsCell's} tree only. That is, only car and cdr elements that
	 * implement {@link ConsCell} are cloned. All cloning is performed via recursive calls to {@link #structuralClone()} or
	 * {@link #structuralClone(ConsCell)} as needed.
	 * 
	 * @param previous
	 *            the previous {@link ConsCell} in the tree (effectively the cloned {@link ConsCell ConsCell's} parent)
	 * @return a structurally independent clone of the {@link ConsCell ConsCell's} tree with the given {@code previous}
	 *         {@link ConsCell} as the parent of the new tree
	 */
	public ConsCell structuralClone(ConsCell previous) {
		try {
			ConsCell clone = (ConsCell) super.clone();
			clone.setPreviousInner(previous);
			if (previous != null)
				previous.setCdr(clone, previous.getConsCellType());
			if (clone.getCar() instanceof ConsCell)
				clone.setCar(((ConsCell) clone.getCar()).structuralClone(), clone.getCarType());
			if (clone.getCdr() instanceof ConsCell) {
				clone.setCdrInner(((ConsCell) clone.getCdr()).structuralClone(), clone.getCdrType());
				((ConsCell) clone.getCdr()).setPreviousInner(clone);
			}
			return clone;
		}
		catch (CloneNotSupportedException e) { //This cannot occur
			e.printStackTrace();
			return null;
		}
	}
	
	private Object tryClone(Object obj) {
		if (obj instanceof ConsCell)
			return ((ConsCell) obj).clone();
		try {
			if (obj instanceof Cloneable) { //If it is cloneable, try to call the clone method
				try {
					Method clone = obj.getClass().getMethod("clone");
					clone.setAccessible(true);
					return clone.invoke(obj);
				}
				catch (NoSuchMethodException e) {/* This is not going to print anything because if the clone method isn't found, we just won't call it. */}
			}
			try {
				Constructor<?> copy = obj.getClass().getConstructor(obj.getClass()); //Otherwise, check if it has a public copy constructor
				copy.setAccessible(true);
				return copy.newInstance(obj);
			}
			catch (NoSuchMethodException e) {/* This is not going to print anything because if the copy constructor isn't found, we just won't call it. */}
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			e.printStackTrace();
		}
		return obj; //If it cannot be cloned or copied, return the object itself
	}
	
	/**
	 * <b>Default:</b> {@link CoreConsType#EMPTY}
	 * 
	 * @return the {@link ConsType} used to indicate that a field is empty
	 */
	protected ConsType getEmptyType() {
		return CoreConsType.EMPTY;
	}
	
	/**
	 * <b>Default:</b> {@link CoreConsType#CONS_CELL}
	 * 
	 * @return the {@link ConsType} used to indicate that a field is a {@link ConsCell}
	 */
	protected ConsType getConsCellType() {
		return CoreConsType.CONS_CELL;
	}
	
	/**
	 * Returns an {@link Iterator} over the elements in the s-expression at the level of the {@link ConsCell} on which
	 * {@link #iterator()} was called.<br>
	 * <i>The first call to {@link Iterator#next() next()} returns the {@link ConsCell} on which {@link #iterator()} was
	 * called.</i>
	 * 
	 * @return an {@link Iterator}
	 */
	@Override
	public Iterator<ConsCell> iterator() {
		return new Itr();
	}
	
	private class Itr implements Iterator<ConsCell> {
		private ConsCell current = null, next = ConsCell.this;
		
		@Override
		public boolean hasNext() {
			return next != null;
		}
		
		@Override
		public ConsCell next() {
			if (next == null)
				throw new NoSuchElementException();
			current = next;
			next = next.getNext();
			return current;
		}
		
		@Override
		public void remove() throws IllegalStateException {
			if (current == null)
				throw new IllegalStateException();
			current.remove();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ConsCell))
			return false;
		ConsCell c = (ConsCell) o;
		return (getCarType() == null ? c.getCarType() == null : getCarType().equals(c.getCarType())) && (getCar() == null ? c.getCar() == null : getCar().equals(c.getCar())) && (getCdrType() == null
				? c.getCdrType() == null : getCdrType().equals(c.getCdrType())) && (getCdr() == null ? c.getCdr() == null : getCdr().equals(c.getCdr()));
	}
}
