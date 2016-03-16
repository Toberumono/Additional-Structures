package toberumono.structures.sexpressions.generic;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Generic implementation of a doubly-linked list, using a structure based on cons cells from Lisp.<br>
 * Each entry in the list contains two pointers and two types to allow for easier type checking.
 * 
 * @author Toberumono
 */
public class ConsCell implements Comparable<ConsCell>, Cloneable, Iterable<ConsCell> {
	protected Object car, cdr;
	protected ConsCell previous;
	
	/**
	 * Constructs a {@link ConsCell} based on the given source.
	 * 
	 * @param source
	 *            the source of the car/cdr values
	 * @param previous
	 *            the new {@link ConsCell GenericConsCell's} previous cell
	 */
	public ConsCell(ConsCell source, ConsCell previous) {
		this(source.car, source.cdr);
		this.previous = previous;
	}
	
	/**
	 * Constructs a {@link ConsCell} with the given car and cdr values.
	 * 
	 * @param car
	 *            the car value
	 * @param cdr
	 *            the cdr value
	 */
	public ConsCell(Object car, Object cdr) {
		setCar(car);
		setCdr(cdr);
		previous = null;
	}
	
	/**
	 * Constructs a {@link ConsCell} with the given car value and an empty cdr value.
	 * 
	 * @param car
	 *            the car value
	 */
	public ConsCell(Object car) {
		this(car, null);
	}
	
	/**
	 * Creates an empty {@link ConsCell}.
	 */
	public ConsCell() {
		this(null);
	}
	
	/**
	 * @return the car value of this {@link ConsCell}
	 */
	public Object getCar() {
		return car;
	}
	
	/**
	 * @return the cdr value of this {@link ConsCell}
	 */
	public Object getCdr() {
		return cdr;
	}
	
	@Override
	public String toString() {
		return ((car == null ? "" : car.toString() + " ") + (cdr == null ? "" : cdr.toString())).trim();
	}
	
	/* ************************************************ */
	/*                  CHECK FUNCTIONS                 */
	/* ************************************************ */
	
	/**
	 * Determines if the length of the current level of the s-expression starting with this {@link ConsCell} has a
	 * length of at least {@code length} (effectively, are there at least {@code length-1} {@link ConsCell
	 * GenericConsCells} after this {@link ConsCell}?).<br>
	 * If {@code length} is negative, it performs the same test, but scans backwards (effectively, are there at least
	 * {@code |length|-1} {@link ConsCell GenericConsCells} before this {@link ConsCell}?).
	 * 
	 * @param length
	 *            the length to test for
	 * @return {@code true} if there are at least {@code length-1} {@link ConsCell GenericConsCells} after this
	 *         {@link ConsCell} if {@code length} is at least 0 or if there are at least {@code |length|-1}
	 *         {@link ConsCell GenericConsCells} before this {@link ConsCell}, otherwise it returns
	 *         {@code false}
	 */
	public boolean hasLength(int length) {
		if (length == 0)
			return true;
		else if (length < 0) {
			if (++length == 0)
				return true;
			for (ConsCell cur = this; length < 0; cur = cur.getPreviousConsCell(), length++)
				if (cur == null)
					return false;
			return true;
		}
		else {
			if (--length == 0)
				return true;
			for (ConsCell cur = this; length > 0; cur = cur.getNextConsCell(), length--)
				if (cur == null)
					return false;
			return true;
		}
	}
	
	/**
	 * Determines if this {@link ConsCell} is the first one in its s-expression.
	 * 
	 * @return {@code true} if this {@link ConsCell GenericConsCell's} previous cell field is {@code null}.
	 */
	public boolean isFirstConsCell() {
		return previous == null;
	}
	
	/**
	 * A null {@link ConsCell} is defined as one whose carType and cdrType are both equal to the empty type.
	 * 
	 * @return {@code true} if this {@link ConsCell} is a null (or empty) {@link ConsCell}
	 */
	public boolean isNull() {
		return car == null && cdr == null;
	}
	
	/**
	 * Determines if this {@link ConsCell} is the last one in its s-expression.
	 * 
	 * @return {@code true} if this {@link ConsCell GenericConsCell's} cdr is not on instance of
	 *         {@link ConsCell}.
	 */
	public boolean isLastConsCell() {
		return !(cdr instanceof ConsCell);
	}
	
	/* ************************************************ */
	/*              MODIFICATION FUNCTIONS              */
	/* ************************************************ */
	
	/**
	 * Sets the {@link #car} of this cell to the given values.<br>
	 * This method returns the cell it was called on for chaining purposes.
	 * 
	 * @param car
	 *            the new {@link #car} value
	 * @return {@code this}
	 * @see #getCar()
	 * @see #setCdr(Object)
	 */
	public ConsCell setCar(Object car) {
		this.car = car;
		return this;
	}
	
	/**
	 * Sets the {@link #cdr} of this cell to the given values. If the {@link #cdr} being replaced is a cell, then the value
	 * of that cell's {@link #previous} field is set to {@code null}.<br>
	 * This method returns the cell it was called on for chaining purposes.
	 * 
	 * @param cdr
	 *            the new {@link #cdr} value
	 * @return {@code this}
	 * @see #getCdr()
	 * @see #setCar(Object)
	 */
	public ConsCell setCdr(Object cdr) {
		if (cdr instanceof ConsCell)
			((ConsCell) this.cdr).previous = null;
		this.cdr = cdr;
		return this;
	}
	
	/**
	 * This method appends the given cell or cells to this one, and, if this cell is null as defined in {@link #isNull()},
	 * overwrites this the contents of this cell with the first cell to be appended.<br>
	 * If this cell's cdr is also a cell, then {@link #insert(ConsCell)} is recursively called on the last cell in the
	 * inserted s-expression's root level (found via a call to {@link #getLastConsCell()}) with this cell's cdr as the
	 * argument. If this cell's cdr is not empty and is not a cell, then the cdr of the last cell in the given cell or cells'
	 * cdr is set to this cell's cdr.
	 * 
	 * @param next
	 *            the cell to insert
	 * @return {@code this} if {@code this.isNull()} was {@code true}, otherwise {@code next}
	 * @see #append(ConsCell)
	 */
	public ConsCell insert(ConsCell next) {
		if (isNull()) {
			next.setPrevious(null);
			car = next.car;
			cdr = next.cdr;
			if (next.cdr instanceof ConsCell)
				((ConsCell) next.cdr).setPrevious(this);
			return this;
		}
		next.setPrevious(this);
		if (cdr == null) {
			//If this was a terminal cell, just set the cdr to pointer to next
			cdr = next;
			return next;
		}
		ConsCell out = next.getLastConsCell();
		if (cdr instanceof ConsCell)
			out = out.insert((ConsCell) cdr);
		else
			out.setCdr(cdr);
		cdr = next;
		return next;
	}
	
	/**
	 * Appends the given cell to this cell.<br>
	 * Roughly equivalent in function to {@link #insert(ConsCell)} - the only difference is that this returns the last
	 * {@link ConsCell} in the equivalent level of the resulting tree.
	 * 
	 * @param next
	 *            the cell to insert
	 * @return the last cell in the equivalent level of the resulting cell tree. Equivalent to
	 *         {@code cell.insert(next).getLastConsCell()}.
	 * @see #insert(ConsCell)
	 */
	public ConsCell append(ConsCell next) {
		ConsCell out = next.getLastConsCell();
		if (isNull()) {
			next.setPrevious(null);
			car = next.car;
			cdr = next.cdr;
			if (next.cdr instanceof ConsCell)
				((ConsCell) next.cdr).setPrevious(this);
			return this;
		}
		next.setPrevious(this);
		if (cdr == null) {
			//If this was a terminal cell, just set the cdr to point to next
			cdr = next;
			return out;
		}
		if (cdr instanceof ConsCell)
			out = out.append((ConsCell) cdr).getLastConsCell();
		else
			out.setCdr(cdr);
		cdr = next;
		return next;
	}
	
	/**
	 * Splits the cell tree on this {@link ConsCell}. After the split, the original tree will end at the
	 * {@link ConsCell} before this one, and this {@link ConsCell} will be the first {@link ConsCell} in
	 * the new tree.
	 * 
	 * @return {@code this}
	 */
	public ConsCell split() {
		setPrevious(null);
		return this;
	}
	
	protected void setPrevious(ConsCell previous) {
		if (this.previous != null)
			this.previous.setCdr(null);
		this.previous = previous;
	}
	
	/* ************************************************ */
	/*                MOVEMENT FUNCTIONS                */
	/* ************************************************ */
	
	/**
	 * @return the next {@link ConsCell} in this {@link ConsCell GenericConsCell's} s-expression or a new,
	 *         empty {@link ConsCell} if there is not one.
	 * @see #getPreviousConsCell()
	 * @see #getLastConsCell()
	 */
	public ConsCell getNextConsCell() {
		return cdr instanceof ConsCell ? (ConsCell) cdr : null;
	}
	
	/**
	 * Gets the nth {@link ConsCell} after this {@link ConsCell} (e.g. {@code getNextCell(1)} is equivalent to
	 * {@code getNextConsCell()}).<br>
	 * If {@code n} is negative, this is equivalent to {@link #getPreviousConsCell(int)} with {@code n} being positive.
	 * 
	 * @param n
	 *            the distance between this {@link ConsCell} and the desired {@link ConsCell}
	 * @return the nth {@link ConsCell} after this {@link ConsCell} in the current level of the tree structure
	 *         or {@code null} if there is no such {@link ConsCell}
	 */
	public ConsCell getNextConsCell(int n) {
		if (n < 0)
			return getPreviousConsCell(-n);
		ConsCell cur = this;
		for (; n > 0 && cur != null; cur = cur.getNextConsCell(), n--);
		return cur;
	}
	
	/**
	 * @return the last {@link ConsCell} in its s-expression. If this {@link ConsCell} is the last one, it
	 *         returns itself.
	 * @see #getNextConsCell()
	 */
	public ConsCell getLastConsCell() {
		ConsCell current = this;
		while (current.cdr instanceof ConsCell)
			current = (ConsCell) current.cdr;
		return current;
	}
	
	/**
	 * @return the previous {@link ConsCell} in this {@link ConsCell GenericConsCell's} s-expression or a new,
	 *         empty {@link ConsCell} if there is not one.
	 * @see #getNextConsCell()
	 * @see #getFirstConsCell()
	 */
	public ConsCell getPreviousConsCell() {
		return previous;
	}
	
	/**
	 * Gets the nth {@link ConsCell} before this {@link ConsCell} (e.g. {@code getPreviousCell(1)} is
	 * equivalent to {@code getPreviousConsCell()}).<br>
	 * If {@code n} is negative, this is equivalent to {@link #getNextConsCell(int)} with {@code n} being positive.
	 * 
	 * @param n
	 *            the distance between this {@link ConsCell} and the desired {@link ConsCell}
	 * @return the nth {@link ConsCell} before this {@link ConsCell} in the current level of the tree structure
	 *         or {@code null} if there is no such {@link ConsCell}
	 */
	public ConsCell getPreviousConsCell(int n) {
		if (n < 0)
			return getNextConsCell(-n);
		ConsCell cur = this;
		for (; n > 0 && cur != null; cur = cur.getPreviousConsCell(), n--);
		return cur;
	}
	
	/**
	 * @return the first {@link ConsCell} in its s-expression. If this {@link ConsCell} is the first one, it
	 *         returns itself.
	 * @see #getPreviousConsCell()
	 */
	public ConsCell getFirstConsCell() {
		ConsCell current = this;
		while (current.previous != null)
			current = current.previous;
		return current;
	}
	
	/**
	 * Returns a shallow copy of this {@link ConsCell} with only the car and carType.<br>
	 * This effectively creates a {@link ConsCell} with a pointer to the same car value of this
	 * {@link ConsCell} but separate from the list.
	 * 
	 * @return a shallow copy of the {@link ConsCell} that is separate from the list
	 */
	public ConsCell singular() {
		try {
			ConsCell clone = (ConsCell) super.clone();
			clone.previous = null;
			clone.cdr = null;
			return clone;
		}
		catch (CloneNotSupportedException e) {/* This won't occur */}
		return null;
	}
	
	/**
	 * Replaces the car value of this {@link ConsCell} with that of the given cell.
	 * 
	 * @param cell
	 *            the cell whose car value is to be written to this {@link ConsCell}
	 */
	public void replaceCar(ConsCell cell) {
		car = cell.car;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(ConsCell o) {
		if (getCar().getClass().isInstance(o.getCar()) && getCar() instanceof Comparable && o.getCar() instanceof Comparable)
			return ((Comparable<Object>) getCar()).compareTo(o.getCar());
		else if (getCdr().getClass().isInstance(o.getCdr()) && getCdr() instanceof Comparable && o.getCdr() instanceof Comparable)
			return ((Comparable<Object>) getCdr()).compareTo(o.getCdr());
		else
			return 0;
	}
	
	/**
	 * Creates a clone of this {@link ConsCell GenericConsCell's} s-expression, where non-cell values are not cloned.
	 * 
	 * @return a clone of this {@link ConsCell}
	 */
	@Override
	public ConsCell clone() {
		try {
			ConsCell clone = (ConsCell) super.clone();
			if (clone.car instanceof ConsCell)
				clone.car = ((ConsCell) clone.car).clone();
			if (clone.cdr instanceof ConsCell) {
				clone.cdr = ((ConsCell) clone.cdr).clone();
				((ConsCell) clone.cdr).previous = clone;
			}
			return clone;
		}
		catch (CloneNotSupportedException e) {/* This won't occur */}
		return null;
	}
	
	/**
	 * Creates a clone of this {@link ConsCell GenericConsCell's} s-expression, where non-cell values are not cloned
	 * and sets the clone's previous {@link ConsCell} to {@code previous}.
	 * 
	 * @param previous
	 *            the clone's previous {@link ConsCell}
	 * @return a clone of this {@link ConsCell}
	 */
	public ConsCell clone(ConsCell previous) {
		ConsCell clone = (ConsCell) clone();
		clone.previous = previous;
		return clone;
	}
	
	/**
	 * @return the the number of {@link ConsCell GenericConsCells} in the level of the s-expression that this
	 *         {@link ConsCell} is on, starting from this {@link ConsCell}.
	 */
	public int length() {
		if (isNull())
			return 0;
		ConsCell cell = this;
		int length = 1;
		while ((cell = cell.getNextConsCell()) != null)
			length++;
		return length;
	}
	
	/**
	 * Removes this cell from the list and returns the one after it.
	 * 
	 * @return the next cell in the list as determined by {@link #getNextConsCell()}
	 * @see #getNextConsCell()
	 */
	public ConsCell remove() {
		if (this.previous != null) {
			previous.cdr = cdr;
		}
		ConsCell next = getNextConsCell();
		if (next != null) {
			next.previous = previous;
			previous = null;
			cdr = null;
		}
		return next;
	}
	
	/**
	 * Generates a {@link String} that shows the structure of this s-expression.<br>
	 * This is primarily a debugging function.
	 * 
	 * @return a {@link String} describing this s-expression's structure
	 */
	public String structureString() {
		String output = "";
		ConsCell current = this;
		while (current != null) {
			if (current.car != null)
				output = output + current.car;
			output = output + ", ";
			if (!(current.cdr instanceof ConsCell))
			current = (ConsCell) current.cdr;
		}
		if (output.length() > 0)
			output = output.substring(0, output.length() - 2);
		return output;
	}
	
	/**
	 * Generates the hash by calling {@link java.util.Objects#hash(Object...) Objects.hash(Object...)} on the car, carType,
	 * cdr, and cdrType of this {@link ConsCell}.<br>
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(car, cdr);
	}
	
	/**
	 * Returns an {@link Iterator} over the elements in the s-expression at the level of the {@link ConsCell} on which
	 * {@link #iterator()} was called.<br>
	 * <i>The first call to {@link Iterator#next() next()} returns the {@link ConsCell} on which {@link #iterator()}
	 * was called.</i>
	 * 
	 * @return an {@link Iterator}
	 */
	@Override
	public Iterator<ConsCell> iterator() {
		return new Itr();
	}
	
	private class Itr implements Iterator<ConsCell> {
		private ConsCell current = null, next = (ConsCell) ConsCell.this;
		
		@Override
		public boolean hasNext() {
			return next != null;
		}
		
		@Override
		public ConsCell next() {
			if (next == null)
				throw new NoSuchElementException();
			current = next;
			next = next.getNextConsCell();
			return current;
		}
		
		@Override
		public void remove() throws IllegalStateException {
			if (current == null)
				throw new IllegalStateException();
			current.remove();
		}
	}
}
