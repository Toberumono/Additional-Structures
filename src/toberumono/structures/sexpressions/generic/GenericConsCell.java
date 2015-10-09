package toberumono.structures.sexpressions.generic;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import toberumono.structures.sexpressions.ConsCellConstructor;

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
	protected final ConsCellConstructor<Ty, To> constructor;
	protected final Ty cellType, emptyType;
	
	/**
	 * Constructs a {@link GenericConsCell Cell} based on the given source.
	 * 
	 * @param source
	 *            the source of the car/cdr values
	 * @param previous
	 *            the new {@link GenericConsCell GenericCell's} previous cell
	 * @param constructor
	 *            the constructor to use
	 * @param cellType
	 *            the type that indicates a cell
	 * @param emptyType
	 *            the type that indicates an empty car/cdr field.
	 */
	public GenericConsCell(To source, To previous, ConsCellConstructor<Ty, To> constructor, Ty cellType, Ty emptyType) {
		this(source.car, source.carType, source.cdr, source.cdrType, constructor, cellType, emptyType);
		this.previous = previous;
	}
	
	/**
	 * Constructs a {@link GenericConsCell Cell} with the given car and cdr values.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 * @param cdr
	 *            the cdr value
	 * @param cdrType
	 *            the cdr type
	 * @param constructor
	 *            the constructor to use
	 * @param cellType
	 *            the type that indicates a cell
	 * @param emptyType
	 *            the type that indicates an empty car/cdr field.
	 */
	public GenericConsCell(Object car, Ty carType, Object cdr, Ty cdrType, ConsCellConstructor<Ty, To> constructor, Ty cellType, Ty emptyType) {
		this.carType = carType == null ? emptyType : carType;
		this.car = car;
		if (car instanceof GenericConsCell)
			((To) car).previous = null;
		this.cdrType = cdrType == null ? emptyType : cdrType;
		this.cdr = cdr;
		if (cdr instanceof GenericConsCell)
			((To) cdr).previous = (To) this;
		previous = null;
		this.constructor = constructor;
		this.cellType = cellType;
		this.emptyType = emptyType;
	}
	
	/**
	 * Constructs a {@link GenericConsCell Cell} with the given car value and an empty cdr value.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 * @param constructor
	 *            the constructor to use
	 * @param cellType
	 *            the type that indicates a cell
	 * @param emptyType
	 *            the type that indicates an empty car/cdr field.
	 */
	public GenericConsCell(Object car, Ty carType, ConsCellConstructor<Ty, To> constructor, Ty cellType, Ty emptyType) {
		this(car, carType, null, emptyType, constructor, cellType, emptyType);
	}
	
	/**
	 * Creates an empty {@link GenericConsCell Cell}
	 * 
	 * @param constructor
	 *            the constructor for the <tt>Cell</tt> type that extends this one
	 * @param cellType
	 *            the <tt>GenericType</tt> that represents <tt>Cell</tt> type that extends this one
	 * @param emptyType
	 *            the <tt>GenericType</tt> that represents an empty (or null) value in the <tt>Cell</tt> type that extends
	 *            this one
	 */
	public GenericConsCell(ConsCellConstructor<Ty, To> constructor, Ty cellType, Ty emptyType) {
		this(null, emptyType, constructor, cellType, emptyType);
	}
	
	/**
	 * @return the car value of this {@link GenericConsCell Cell}
	 * @see #getCarType()
	 */
	public Object getCar() {
		return car;
	}
	
	/**
	 * @return the {@link GenericConsType GenericType} of the car value of this {@link GenericConsCell Cell}
	 * @see #getCar()
	 */
	public Ty getCarType() {
		return carType;
	}
	
	/**
	 * @return the cdr value of this {@link GenericConsCell Cell}
	 * @see #getCdrType()
	 */
	public Object getCdr() {
		return cdr;
	}
	
	/**
	 * @return the {@link GenericConsType GenericType} of the cdr value of this {@link GenericConsCell Cell}
	 * @see #getCdr()
	 */
	public Ty getCdrType() {
		return cdrType;
	}
	
	@Override
	public String toString() {
		return ((car != null ? carType.valueToString(car) + " " : "") + (cdr != null ? cdrType.valueToString(cdr) + " " : "")).trim();
	}
	
	/* ************************************************ */
	/*                  CHECK FUNCTIONS                 */
	/* ************************************************ */
	
	/**
	 * Determines if the length of the current level of the s-expression starting with this {@link GenericConsCell Cell} has
	 * a length of at least <tt>length</tt> (effectively, are there at least <tt>length</tt>-1 {@link GenericConsCell Cells}
	 * after this {@link GenericConsCell Cell}?).<br>
	 * If <tt>length</tt> is negative, it performs the same test, but scans backwards (effectively, are there at least |
	 * <tt>length</tt>|-1 {@link GenericConsCell Cells} before this {@link GenericConsCell Cell}?).
	 * 
	 * @param length
	 *            the length to test for
	 * @return {@code true} if there are at least <tt>length</tt>-1 {@link GenericConsCell Cells} after this
	 *         {@link GenericConsCell Cell} if <tt>length</tt> is at least 0 or if there are at least |<tt>length</tt>|-1
	 *         {@link GenericConsCell Cells} before this {@link GenericConsCell Cell}, otherwise it returns {@code false}
	 */
	public boolean hasLength(int length) {
		if (length < 0) {
			if (++length == 0)
				return true;
			for (GenericConsCell<Ty, To> cur = this; length < 0; cur = cur.getPreviousConsCell(), length++)
				if (cur == null)
					return false;
			return true;
		}
		if (--length == 0)
			return true;
		for (GenericConsCell<Ty, To> cur = this; length > 0; cur = cur.getNextConsCell(), length--)
			if (cur == null)
				return false;
		return true;
	}
	
	/**
	 * Determines if this {@link GenericConsCell Cell} is the first one in its s-expression.
	 * 
	 * @return <tt>true</tt> if this {@link GenericConsCell Cell}'s previous cell field is null.
	 */
	public boolean isFirstConsCell() {
		return previous == null;
	}
	
	/**
	 * A null {@link GenericConsCell Cell} is defined as one whose carType and cdrType are both equal to the empty type.
	 * 
	 * @return {@code true} if this {@link GenericConsCell Cell} is a null (or empty) {@link GenericConsCell Cell}
	 */
	public boolean isNull() {
		return carType.equals(emptyType) && cdrType.equals(emptyType);
	}
	
	/**
	 * Determines if this {@link GenericConsCell Cell} is the last one in its s-expression.
	 * 
	 * @return <tt>true</tt> if this {@link GenericConsCell Cell}'s cdr is not on instance of {@link GenericConsCell Cell}.
	 */
	public boolean isLastConsCell() {
		return !(cdr instanceof GenericConsCell);
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
	 *            the {@link GenericConsType GenericType} of the new {@link #car} value
	 * @return {@code this}
	 * @see #getCar()
	 * @see #getCarType()
	 * @see #setCdr(Object, GenericConsType)
	 */
	public To setCar(Object car, Ty carType) {
		this.car = car;
		this.carType = carType == null ? emptyType : carType;
		return (To) this;
	}
	
	/**
	 * Sets the {@link #cdr} and {@link #cdrType} of this cell to the given values. If the {@link #cdr} being replaced is a
	 * cell, then the value of that cell's {@link #previous} field is set to {@code null}.<br>
	 * This method returns the cell it was called on for chaining purposes.
	 * 
	 * @param cdr
	 *            the new {@link #cdr} value
	 * @param cdrType
	 *            the {@link GenericConsType GenericType} new {@link #cdr} value
	 * @return {@code this}
	 * @see #getCdr()
	 * @see #getCdrType()
	 * @see #setCar(Object, GenericConsType)
	 */
	public To setCdr(Object cdr, Ty cdrType) {
		if (this.cdrType == cellType)
			((To) this.cdr).previous = null;
		this.cdr = cdr;
		this.cdrType = cdrType == null ? emptyType : cdrType;
		return (To) this;
	}
	
	/**
	 * This method appends the given cell or cells to this one, and, if this cell is null as defined in {@link #isNull()},
	 * overwrites this the contents of this cell with the first cell to be appended.<br>
	 * If this cell's cdr is also a cell, then {@link #insert(GenericConsCell)} is recursively called on the last cell in the
	 * inserted s-expression's root level (found via a call to {@link #getLastConsCell()}) with this cell's cdr as the argument.
	 * If this cell's cdr is not empty and is not a cell, then the cdr of the last cell in the given cell or cells' cdr
	 * is set to this cell's cdr.
	 * 
	 * @param next
	 *            the cell to insert
	 * @return <tt>this</tt> if {@code this.isNull()} was {@code true}, otherwise <tt>next</tt>
	 * @see #append(GenericConsCell)
	 */
	public To insert(To next) {
		if (isNull()) {
			next.setPrevious(null);
			car = next.car;
			carType = next.carType;
			cdr = next.cdr;
			cdrType = next.cdrType;
			if (next.cdrType == next.cellType)
				((To) next.cdr).setPrevious((To) this);
			return (To) this;
		}
		next.setPrevious((To) this);
		if (cdrType == emptyType) {
			//If this was a terminal cell, just set the cdr to point to next
			cdr = next;
			cdrType = next.cellType;
			return next;
		}
		To out = next.getLastConsCell();
		if (cdrType == cellType)
			out = out.insert((To) cdr);
		else
			out.setCdr(cdr, cdrType);
		cdr = next;
		cdrType = next.cellType;
		return next;
	}
	
	/**
	 * Appends the given cell to this cell.<br>
	 * Roughly equivalent in function to {@link #insert(GenericConsCell)} - the only difference is that this returns the last
	 * {@link GenericConsCell Cell} in the equivalent level of the resulting tree.
	 * 
	 * @param next
	 *            the cell to insert
	 * @return the last cell in the equivalent level of the resulting cell tree. Equivalent to
	 *         {@code cell.insert(next).getLastConsCell()}.
	 * @see #insert(GenericConsCell)
	 */
	public To append(To next) {
		To out = next.getLastConsCell();
		if (isNull()) {
			next.setPrevious(null);
			car = next.car;
			carType = next.carType;
			cdr = next.cdr;
			cdrType = next.cdrType;
			if (next.cdrType == next.cellType)
				((To) next.cdr).setPrevious((To) this);
			return (To) this;
		}
		next.setPrevious((To) this);
		if (cdrType == emptyType) {
			//If this was a terminal cell, just set the cdr to point to next
			cdr = next;
			cdrType = next.cellType;
			return out;
		}
		if (cdrType == cellType)
			out = out.append((To) cdr).getLastConsCell();
		else
			out.setCdr(cdr, cdrType);
		cdr = next;
		cdrType = next.cellType;
		return next;
	}
	
	/**
	 * Splits the cell tree on this {@link GenericConsCell Cell}. After the split, the original tree will end at the
	 * {@link GenericConsCell Cell} before this one, and this {@link GenericConsCell Cell} will be the first {@link GenericConsCell
	 * Cell} in the new tree.
	 * 
	 * @return {@code this}
	 */
	public To split() {
		setPrevious(null);
		return (To) this;
	}
	
	protected void setPrevious(To previous) {
		if (this.previous != null)
			this.previous.setCdr(null, null);
		this.previous = previous;
	}
	
	/* ************************************************ */
	/*                MOVEMENT FUNCTIONS                */
	/* ************************************************ */
	
	/**
	 * @return the next {@link GenericConsCell Cell} in this {@link GenericConsCell Cell}'s s-expression or a new, empty
	 *         {@link GenericConsCell Cell} if there is not one.
	 * @see #getPreviousConsCell()
	 * @see #getLastConsCell()
	 */
	public To getNextConsCell() {
		return cdr instanceof GenericConsCell ? (To) cdr : constructor.construct(null, emptyType, null, emptyType);
	}
	
	/**
	 * Gets the nth {@link GenericConsCell Cell} after this {@link GenericConsCell Cell} (e.g. {@code getNextCell(1)} is
	 * equivalent to {@code getNextConsCell()}).<br>
	 * If <tt>n</tt> is negative, this is equivalent to {@link #getPreviousConsCell(int)} with <tt>n</tt> being positive.
	 * 
	 * @param n
	 *            the distance between this {@link GenericConsCell Cell} and the desired {@link GenericConsCell Cell}
	 * @return the nth {@link GenericConsCell Cell} after this {@link GenericConsCell Cell} in the current level of the tree
	 *         structure or an empty {@link GenericConsCell Cell} if there is no such {@link GenericConsCell Cell}
	 */
	public To getNextConsCell(int n) {
		if (n < 0)
			return getPreviousConsCell(-n);
		To cur = (To) this;
		for (; n > 0 && !cur.isNull(); cur = cur.getNextConsCell(), n--);
		return cur;
	}
	
	/**
	 * @return the last {@link GenericConsCell Cell} in its s-expression. If this {@link GenericConsCell Cell} is the last one,
	 *         it returns itself.
	 * @see #getNextConsCell()
	 */
	public To getLastConsCell() {
		To current = (To) this;
		while (current.cdrType == cellType)
			current = (To) current.cdr;
		return current;
	}
	
	/**
	 * @return the previous {@link GenericConsCell Cell} in this {@link GenericConsCell Cell}'s s-expression or a new, empty
	 *         {@link GenericConsCell Cell} if there is not one.
	 * @see #getNextConsCell()
	 * @see #getFirstConsCell()
	 */
	public To getPreviousConsCell() {
		return previous == null ? constructor.construct(null, emptyType, null, emptyType) : previous;
	}
	
	/**
	 * Gets the nth {@link GenericConsCell Cell} before this {@link GenericConsCell Cell} (e.g. {@code getPreviousCell(1)} is
	 * equivalent to {@code getPreviousConsCell()}).<br>
	 * If <tt>n</tt> is negative, this is equivalent to {@link #getNextConsCell(int)} with <tt>n</tt> being positive.
	 * 
	 * @param n
	 *            the distance between this {@link GenericConsCell Cell} and the desired {@link GenericConsCell Cell}
	 * @return the nth {@link GenericConsCell Cell} before this {@link GenericConsCell Cell} in the current level of the tree
	 *         structure or an empty {@link GenericConsCell Cell} if there is no such {@link GenericConsCell Cell}
	 */
	public To getPreviousConsCell(int n) {
		if (n < 0)
			return getNextConsCell(-n);
		To cur = (To) this;
		for (; n > 0 && cur != null; cur = cur.getPreviousConsCell(), n--);
		return cur;
	}
	
	/**
	 * @return the first {@link GenericConsCell Cell} in its s-expression. If this {@link GenericConsCell Cell} is the first
	 *         one, it returns itself.
	 * @see #getPreviousConsCell()
	 */
	public To getFirstConsCell() {
		To current = (To) this;
		while (current.previous != null)
			current = current.previous;
		return current;
	}
	
	/**
	 * Returns a shallow copy of this {@link GenericConsCell Cell} with only the car and carType.<br>
	 * This effectively creates a {@link GenericConsCell Cell} with a pointer to the same car value of this <tt>Cell</tt> but
	 * separate from the list.
	 * 
	 * @return a shallow copy of this {@link GenericConsCell Cell} that is separate from the list
	 */
	public To singular() {
		return constructor.construct(car, carType, null, emptyType);
	}
	
	/**
	 * Replaces the car value of this {@link GenericConsCell Cell} with that of the given cell.
	 * 
	 * @param cell
	 *            the cell whose car value is to be written to this {@link GenericConsCell Cell}
	 */
	public void replaceCar(To cell) {
		car = cell.car;
		carType = cell.carType;
	}
	
	@Override
	public int compareTo(To o) {
		int result = carType.compareValues(car, o.car);
		if (result != 0)
			return result;
		return cdrType.compareValues(cdr, o.cdr);
	}
	
	/**
	 * Creates a clone of this {@link GenericConsCell GenericCell's} s-expression, where non-cell values are not cloned.
	 * 
	 * @return a clone of this {@link GenericConsCell Cell}
	 */
	@Override
	public To clone() {
		return clone(null);
	}
	
	/**
	 * Creates a clone of this {@link GenericConsCell GenericCell's} s-expression, where non-cell values are not cloned.
	 * 
	 * @param previous
	 *            the {@link GenericConsCell Cell} that should be set as the cloned <tt>Cell</tt>'s previous value
	 * @return a clone of this {@link GenericConsCell Cell}
	 */
	protected To clone(To previous) {
		To clone = constructor.construct(car instanceof GenericConsCell ? ((To) car).clone((To) this) : car, carType, cdr instanceof GenericConsCell ? ((To) cdr).clone((To) this) : cdr, cdrType);
		clone.previous = previous;
		return clone;
	}
	
	/**
	 * @return the the number of {@link GenericConsCell AbstractCells} in the level of the s-expression that this
	 *         {@link GenericConsCell Cell} is on, starting from this {@link GenericConsCell Cell}.
	 */
	public int length() {
		if (isNull())
			return 0;
		GenericConsCell<Ty, To> cell = this;
		int length = 1;
		while (!(cell = cell.getNextConsCell()).isNull())
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
		if (this.previous != null) {
			previous.cdr = cdr;
			previous.cdrType = cdrType;
		}
		To next = getNextConsCell();
		next.previous = previous;
		previous = null;
		cdr = null;
		cdrType = cellType;
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
		GenericConsCell<Ty, To> current = this;
		do {
			if (current.car instanceof GenericConsCell)
				output = output + current.carType.getOpen() + ((GenericConsCell<?, ?>) current.car).structureString() + current.carType.getClose() + " ";
			else
				output = output + current.carType.valueToString(current.car);
			output = output + ": " + current.carType.toString() + ", ";
			current = (GenericConsCell<Ty, To>) current.cdr;
		} while (current instanceof GenericConsCell);
		if (output.length() > 0)
			output = output.substring(0, output.length() - 2);
		return output;
	}
	
	/**
	 * Generates the hash by calling {@link java.util.Objects#hash(Object...) Objects.hash(Object...)} on the car, carType,
	 * cdr, and cdrType of this {@link GenericConsCell Cell}.<br>
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(car, carType, cdr, cdrType);
	}
	
	/**
	 * {@inheritDoc}<br>
	 * <i>The first call to {@link java.util.Iterator#next() next()} returns this {@link GenericConsCell cell}</i>
	 * 
	 * @return an {@link java.util.Iterator#next() next()} that iterates through the s-expression at the
	 *         level of the {@link GenericConsCell cell} that it was created on.
	 */
	@Override
	public Iterator<To> iterator() {
		return new Iterator<To>() {
			private To last = constructor.construct(null, emptyType, GenericConsCell.this, cellType);
			
			@Override
			public boolean hasNext() {
				return last.cdr instanceof GenericConsCell;
			}
			
			@Override
			public To next() {
				if (!(last.cdr instanceof GenericConsCell))
					throw new NoSuchElementException();
				return (last = (To) last.cdr).singular();
			}
			
		};
	}
}
