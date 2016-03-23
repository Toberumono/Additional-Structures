package toberumono.structures.sexpressions.generic;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Generic implementation of a doubly-linked list, using a structure based on cons cells from Lisp.<br>
 * Each entry in the list contains two pointers and two types to allow for easier type checking.
 * 
 * @author Toberumono
 * @param <Ty>
 *            the implementation of {@link GenericConsType} used by the extending implementation.
 * @param <To>
 *            the extending implementation
 */
@SuppressWarnings("unchecked")
public class GenericConsCell<Ty extends GenericConsType, To extends GenericConsCell<Ty, To>> implements Comparable<To>, Cloneable, Iterable<To> {
	protected Ty carType, cdrType;
	protected Object car, cdr;
	protected To previous;
	protected final Ty cellType, emptyType;
	
	/**
	 * Constructs a {@link GenericConsCell} based on the given source.
	 * 
	 * @param source
	 *            the source of the car/cdr values
	 * @param previous
	 *            the new {@link GenericConsCell GenericConsCell's} previous cell
	 * @param cellType
	 *            the type that indicates a cell
	 * @param emptyType
	 *            the type that indicates an empty car/cdr field.
	 */
	public GenericConsCell(To source, To previous, Ty cellType, Ty emptyType) {
		this(source.getCar(), source.getCarType(), source.getCdr(), source.getCdrType(), cellType, emptyType);
		this.previous = previous;
	}
	
	/**
	 * Constructs a {@link GenericConsCell} with the given car and cdr values.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 * @param cdr
	 *            the cdr value
	 * @param cdrType
	 *            the cdr type
	 * @param cellType
	 *            the type that indicates a cell
	 * @param emptyType
	 *            the type that indicates an empty car/cdr field.
	 */
	public GenericConsCell(Object car, Ty carType, Object cdr, Ty cdrType, Ty cellType, Ty emptyType) {
		this.cellType = cellType;
		this.emptyType = emptyType;
		setCar(car, carType);
		setCdr(cdr, cdrType);
		previous = null;
	}
	
	/**
	 * Constructs a {@link GenericConsCell} with the given car value and an empty cdr value.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 * @param cellType
	 *            the type that indicates a cell
	 * @param emptyType
	 *            the type that indicates an empty car/cdr field.
	 */
	public GenericConsCell(Object car, Ty carType, Ty cellType, Ty emptyType) {
		this(car, carType, null, emptyType, cellType, emptyType);
	}
	
	/**
	 * Creates an empty {@link GenericConsCell}
	 * 
	 * @param cellType
	 *            the {@link GenericConsType Type} of the {@link GenericConsCell} type that extends this one
	 * @param emptyType
	 *            the {@link GenericConsType Type} that represents an empty (or {@code null}) value in the
	 *            {@link GenericConsCell} type that extends this one
	 */
	public GenericConsCell(Ty cellType, Ty emptyType) {
		this(null, emptyType, cellType, emptyType);
	}
	
	/**
	 * @return the car value of this {@link GenericConsCell}
	 * @see #getCarType()
	 */
	public Object getCar() {
		return car;
	}
	
	/**
	 * @return the {@link GenericConsType} of the car value of this {@link GenericConsCell}
	 * @see #getCar()
	 */
	public Ty getCarType() {
		return carType;
	}
	
	/**
	 * @return the cdr value of this {@link GenericConsCell}
	 * @see #getCdrType()
	 */
	public Object getCdr() {
		return cdr;
	}
	
	/**
	 * @return the {@link GenericConsType} of the cdr value of this {@link GenericConsCell}
	 * @see #getCdr()
	 */
	public Ty getCdrType() {
		return cdrType;
	}
	
	@Override
	public String toString() {
		return ((getCar() != null ? getCarType().valueToString(getCar()) + " " : "") + (getCdr() != null ? getCdrType().valueToString(getCdr()) + " " : "")).trim();
	}
	
	/* ************************************************ */
	/*                  CHECK FUNCTIONS                 */
	/* ************************************************ */
	
	/**
	 * Determines if the length of the current level of the s-expression starting with this {@link GenericConsCell} has a
	 * length of at least {@code length} (effectively, are there at least {@code length-1} {@link GenericConsCell
	 * GenericConsCells} after this {@link GenericConsCell}?).<br>
	 * If {@code length} is negative, it performs the same test, but scans backwards (effectively, are there at least
	 * {@code |length|-1} {@link GenericConsCell GenericConsCells} before this {@link GenericConsCell}?).
	 * 
	 * @param length
	 *            the length to test for
	 * @return {@code true} if there are at least {@code length-1} {@link GenericConsCell GenericConsCells} after this
	 *         {@link GenericConsCell} if {@code length} is at least 0 or if there are at least {@code |length|-1}
	 *         {@link GenericConsCell GenericConsCells} before this {@link GenericConsCell}, otherwise it returns
	 *         {@code false}
	 */
	public boolean hasLength(int length) {
		if (length == 0)
			return true;
		else if (length < 0) {
			if (++length == 0)
				return true;
			for (GenericConsCell<Ty, To> cur = this; length < 0; cur = cur.getPreviousConsCell(), length++)
				if (cur == null)
					return false;
			return true;
		}
		else {
			if (--length == 0)
				return true;
			for (GenericConsCell<Ty, To> cur = this; length > 0; cur = cur.getNextConsCell(), length--)
				if (cur == null)
					return false;
			return true;
		}
	}
	
	/**
	 * Determines if this {@link GenericConsCell} is the first one in its s-expression.
	 * 
	 * @return {@code true} if this {@link GenericConsCell GenericConsCell's} previous cell field is {@code null}.
	 */
	public boolean isFirstConsCell() {
		return previous == null;
	}
	
	/**
	 * A null {@link GenericConsCell} is defined as one whose carType and cdrType are both equal to the empty type.
	 * 
	 * @return {@code true} if this {@link GenericConsCell} is a null (or empty) {@link GenericConsCell}
	 */
	public boolean isNull() {
		return getCar() == null && getCarType() == emptyType && getCdr() == null && getCdrType() == emptyType;
	}
	
	/**
	 * Determines if this {@link GenericConsCell} is the last one in its s-expression.
	 * 
	 * @return {@code true} if this {@link GenericConsCell GenericConsCell's} cdr is not on instance of
	 *         {@link GenericConsCell}.
	 */
	public boolean isLastConsCell() {
		return !(getCdr() instanceof GenericConsCell);
	}
	
	/* ************************************************ */
	/*              MODIFICATION FUNCTIONS              */
	/* ************************************************ */
	
	/**
	 * Sets the {@link #car} and {@link #carType} of this cell to the given values.<br>
	 * This method returns the cell it was called on for chaining purposes.
	 * 
	 * @param car
	 *            the new {@link #car} value
	 * @param carType
	 *            the {@link GenericConsType} of the new {@link #car} value
	 * @return {@code this}
	 * @see #getCar()
	 * @see #getCarType()
	 * @see #setCdr(Object, GenericConsType)
	 */
	public To setCar(Object car, Ty carType) {
		setCarInternal(car, carType);
		return (To) this;
	}
	
	protected void setCarInternal(Object car, Ty carType) {
		this.car = car;
		this.carType = carType == null ? emptyType : carType;
	}
	
	/**
	 * Sets the {@link #cdr} and {@link #cdrType} of this cell to the given values. If the {@link #cdr} being replaced is a
	 * cell, then the value of that cell's {@link #previous} field is set to {@code null}.<br>
	 * This method returns the cell it was called on for chaining purposes.
	 * 
	 * @param cdr
	 *            the new {@link #cdr} value
	 * @param cdrType
	 *            the {@link GenericConsType} for the new {@link #cdr} value
	 * @return {@code this}
	 * @see #getCdr()
	 * @see #getCdrType()
	 * @see #setCar(Object, GenericConsType)
	 */
	public To setCdr(Object cdr, Ty cdrType) {
		if (getCdr() instanceof GenericConsCell)
			((To) getCdr()).previous = null;
		setCdrInternal(cdr, cdrType);
		if (getCdr() instanceof GenericConsCell)
			((To) getCdr()).setPrevious((To) this);
		return (To) this;
	}
	
	protected void setCdrInternal(Object cdr, Ty cdrType) {
		this.cdr = cdr;
		this.cdrType = cdrType == null ? emptyType : cdrType;
	}
	
	/**
	 * This method appends the given cell or cells to this one, and, if this cell is null as defined in {@link #isNull()},
	 * overwrites this the contents of this cell with the first cell to be appended.<br>
	 * If this cell's cdr is also a cell, then {@link #insert(GenericConsCell)} is recursively called on the last cell in the
	 * inserted s-expression's root level (found via a call to {@link #getLastConsCell()}) with this cell's cdr as the
	 * argument. If this cell's cdr is not empty and is not a cell, then the cdr of the last cell in the given cell or cells'
	 * cdr is set to this cell's cdr.
	 * 
	 * @param next
	 *            the cell to insert
	 * @return {@code this} if {@code this.isNull()} was {@code true}, otherwise {@code next}
	 * @see #append(GenericConsCell)
	 */
	public To insert(To next) {
		if (isNull()) {
			next.setPrevious(null);
			setCar(next.getCar(), next.getCarType());
			setCdr(next.getCdr(), next.getCdrType());
			return (To) this;
		}
		Object cdr = getCdr();
		Ty type = getCdrType();
		setCdr(next, next.cellType);
		if (cdr != null || type != emptyType) { //If this cell has a cdr value
			To last = next.getLastConsCell();
			last.setCdr(cdr, type);
		}
		return next;
	}
	
	/**
	 * Appends the given cell to this cell.<br>
	 * Roughly equivalent in function to {@link #insert(GenericConsCell)} - the only difference is that this returns the last
	 * {@link GenericConsCell} in the equivalent level of the resulting tree.
	 * 
	 * @param next
	 *            the cell to insert
	 * @return the last cell in the equivalent level of the resulting cell tree. Equivalent to
	 *         {@code cell.insert(next).getLastConsCell()}.
	 * @see #insert(GenericConsCell)
	 */
	public To append(To next) {
		To last = next.getLastConsCell();
		if (isNull()) {
			next.setPrevious(null);
			setCar(next.getCar(), next.getCarType());
			setCdr(next.getCdr(), next.getCdrType());
			return last;
		}
		Object cdr = getCdr();
		Ty type = getCdrType();
		setCdr(next, next.cellType);
		if (cdr != null || type != emptyType) { //If this cell has a cdr value
			last.setCdr(cdr, type);
			last = last.getLastConsCell(); //Steps forward to the actual last cell if needed.
		}
		return last;
	}
	
	/**
	 * Splits the cell tree on this {@link GenericConsCell}. After the split, the original tree will end at the
	 * {@link GenericConsCell} before this one, and this {@link GenericConsCell} will be the first {@link GenericConsCell} in
	 * the new tree.
	 * 
	 * @return {@code this}
	 */
	public To split() {
		setPrevious(null);
		return (To) this;
	}
	
	protected void setPrevious(To previous) {
		if (this.getPreviousConsCell() == previous)
			if (this.getPreviousConsCell() != null)
				this.getPreviousConsCell().setCdr(null, null);
		this.previous = previous;
	}
	
	/* ************************************************ */
	/*                MOVEMENT FUNCTIONS                */
	/* ************************************************ */
	
	/**
	 * @return the next {@link GenericConsCell} in this {@link GenericConsCell GenericConsCell's} s-expression or a new,
	 *         empty {@link GenericConsCell} if there is not one.
	 * @see #getPreviousConsCell()
	 * @see #getLastConsCell()
	 */
	public To getNextConsCell() {
		return getCdr() instanceof GenericConsCell ? (To) getCdr() : null;
	}
	
	/**
	 * Gets the nth {@link GenericConsCell} after this {@link GenericConsCell} (e.g. {@code getNextCell(1)} is equivalent to
	 * {@code getNextConsCell()}).<br>
	 * If {@code n} is negative, this is equivalent to {@link #getPreviousConsCell(int)} with {@code n} being positive.
	 * 
	 * @param n
	 *            the distance between this {@link GenericConsCell} and the desired {@link GenericConsCell}
	 * @return the nth {@link GenericConsCell} after this {@link GenericConsCell} in the current level of the tree structure
	 *         or {@code null} if there is no such {@link GenericConsCell}
	 */
	public To getNextConsCell(int n) {
		if (n < 0)
			return getPreviousConsCell(-n);
		To cur = (To) this;
		for (; n > 0 && cur != null; cur = cur.getNextConsCell(), n--);
		return cur;
	}
	
	/**
	 * @return the last {@link GenericConsCell} in its s-expression. If this {@link GenericConsCell} is the last one, it
	 *         returns itself.
	 * @see #getNextConsCell()
	 */
	public To getLastConsCell() {
		To current = (To) this;
		while (current.getCdr() instanceof GenericConsCell)
			current = (To) current.getCdr();
		return current;
	}
	
	/**
	 * @return the previous {@link GenericConsCell} in this {@link GenericConsCell GenericConsCell's} s-expression or a new,
	 *         empty {@link GenericConsCell} if there is not one.
	 * @see #getNextConsCell()
	 * @see #getFirstConsCell()
	 */
	public To getPreviousConsCell() {
		return previous;
	}
	
	/**
	 * Gets the nth {@link GenericConsCell} before this {@link GenericConsCell} (e.g. {@code getPreviousCell(1)} is
	 * equivalent to {@code getPreviousConsCell()}).<br>
	 * If {@code n} is negative, this is equivalent to {@link #getNextConsCell(int)} with {@code n} being positive.
	 * 
	 * @param n
	 *            the distance between this {@link GenericConsCell} and the desired {@link GenericConsCell}
	 * @return the nth {@link GenericConsCell} before this {@link GenericConsCell} in the current level of the tree structure
	 *         or {@code null} if there is no such {@link GenericConsCell}
	 */
	public To getPreviousConsCell(int n) {
		if (n < 0)
			return getNextConsCell(-n);
		To cur = (To) this;
		for (; n > 0 && cur != null; cur = cur.getPreviousConsCell(), n--);
		return cur;
	}
	
	/**
	 * @return the first {@link GenericConsCell} in its s-expression. If this {@link GenericConsCell} is the first one, it
	 *         returns itself.
	 * @see #getPreviousConsCell()
	 */
	public To getFirstConsCell() {
		To current = (To) this;
		while (current.getPreviousConsCell() != null)
			current = current.getPreviousConsCell();
		return current;
	}
	
	/**
	 * Returns a shallow copy of this {@link GenericConsCell} with only the car and carType.<br>
	 * This effectively creates a {@link GenericConsCell} with a pointer to the same car value of this
	 * {@link GenericConsCell} but separate from the list.
	 * 
	 * @return a shallow copy of the {@link GenericConsCell} that is separate from the list
	 */
	public To singular() {
		try {
			To clone = (To) super.clone();
			clone.previous = null;
			clone.setCdr(null, emptyType);
			return clone;
		}
		catch (CloneNotSupportedException e) {/* This won't occur */}
		return null;
	}
	
	/**
	 * Replaces the car value of this {@link GenericConsCell} with that of the given cell.
	 * 
	 * @param cell
	 *            the cell whose car value is to be written to this {@link GenericConsCell}
	 */
	public void replaceCar(To cell) {
		car = cell.getCar();
		carType = cell.getCarType();
	}
	
	@Override
	public int compareTo(To o) {
		int result = getCarType().compareValues(getCar(), o.getCar());
		if (result == 0)
			result = getCdrType().compareValues(getCdr(), o.getCdr());
		return result;
	}
	
	/**
	 * Creates a clone of this {@link GenericConsCell GenericConsCell's} s-expression, where non-cell values are not cloned.
	 * 
	 * @return a clone of this {@link GenericConsCell}
	 */
	@Override
	public To clone() {
		try {
			To clone = (To) super.clone();
			if (clone.car instanceof GenericConsCell)
				clone.car = ((GenericConsCell<?, ?>) clone.car).clone();
			if (clone.cdr instanceof GenericConsCell)
				clone.cdr = ((To) clone.cdr).clone(clone);
			return clone;
		}
		catch (CloneNotSupportedException e) {/* This won't occur */}
		return null;
	}
	
	/**
	 * Creates a clone of this {@link GenericConsCell GenericConsCell's} s-expression, where non-cell values are not cloned
	 * and sets the clone's previous {@link GenericConsCell} to {@code previous}.
	 * 
	 * @param previous
	 *            the clone's previous {@link GenericConsCell}
	 * @return a clone of this {@link GenericConsCell}
	 */
	public To clone(To previous) {
		To clone = clone();
		clone.previous = previous;
		return clone;
	}
	
	/**
	 * @return the the number of {@link GenericConsCell GenericConsCells} in the level of the s-expression that this
	 *         {@link GenericConsCell} is on, starting from this {@link GenericConsCell}.
	 */
	public int length() {
		if (isNull())
			return 0;
		GenericConsCell<Ty, To> cell = this;
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
	public To remove() {
		To next = getNextConsCell();
		if (getPreviousConsCell() != null)
			getPreviousConsCell().setCdr(getCdr(), getCdrType());
		setCdrInternal(null, emptyType);
		return next;
	}
	
	/**
	 * Generates a {@link String} that shows the structure of this s-expression.<br>
	 * This is primarily a debugging function.
	 * 
	 * @return a {@link String} describing this s-expression's structure
	 */
	public String structureString() {
		StringBuilder output = new StringBuilder();
		GenericConsCell<Ty, To> current = this;
		do {
			if (current.getCar() instanceof GenericConsCell)
				output.append(current.getCarType().getOpen()).append(((GenericConsCell<?, ?>) current.getCar()).structureString()).append(current.getCarType().getClose());
			else
				output.append(current.getCarType().valueToString(current.getCar()));
			output.append(": ").append(current.getCarType().toString()).append(", ");
		} while ((current = current.getNextConsCell()) != null);
		if (output.length() > 0)
			output.delete(output.length() - 2, output.length());
		return output.toString();
	}
	
	/**
	 * Generates the hash by calling {@link Objects#hash(Object...)} on the {@link #getCar() car}, {@link #getCarType()
	 * carType}, {@link #getCdr() cdr}, and {@link #getCdrType() cdrType} of the {@link GenericConsCell}.<br>
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getCar(), getCarType(), getCdr(), getCdrType());
	}
	
	/**
	 * Returns an {@link Iterator} over the elements in the s-expression at the level of the {@link GenericConsCell} on which
	 * {@link #iterator()} was called.<br>
	 * <i>The first call to {@link Iterator#next() next()} returns the {@link GenericConsCell} on which {@link #iterator()}
	 * was called.</i>
	 * 
	 * @return an {@link Iterator}
	 */
	@Override
	public Iterator<To> iterator() {
		return new Itr();
	}
	
	private class Itr implements Iterator<To> {
		private To current = null, next = (To) GenericConsCell.this;
		
		@Override
		public boolean hasNext() {
			return next != null;
		}
		
		@Override
		public To next() {
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
