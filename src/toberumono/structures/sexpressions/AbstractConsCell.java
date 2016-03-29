package toberumono.structures.sexpressions;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Abstract implementation of a combination doubly-linked list and s-expression structure based on cons cells from Lisp.<br>
 * In order for cloning to work, this package must have the "accessDeclaredMembers" {@link RuntimePermission} if a
 * {@link SecurityManager} is enabled.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link AbstractConsCell} being used
 * @param <T>
 *            the implementation of {@link ConsType} being used
 */
public abstract class AbstractConsCell<C extends AbstractConsCell<C, T>, T extends ConsType> implements GenericConsCell<C, T>, Cloneable {
	private Object car, cdr;
	private T carType, cdrType;
	private C previous;
	
	/**
	 * Constructs an empty {@link AbstractConsCell}.
	 */
	public AbstractConsCell() {
		this.car = null;
		this.carType = getEmptyType();
		this.cdr = null;
		this.cdrType = getEmptyType();
		this.previous = null;
	}
	
	/**
	 * Constructs a {@link AbstractConsCell} with an empty {@code cdr} value.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link AbstractConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 */
	public AbstractConsCell(Object car, T carType) {
		this.car = car;
		this.carType = carType == null ? getEmptyType() : carType;
		this.cdr = null;
		this.cdrType = getEmptyType();
		this.previous = null;
	}
	
	/**
	 * Constructs a {@link AbstractConsCell} with a {@link AbstractConsCell} as its {@code cdr} value.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link AbstractConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 * @param cdr
	 *            the {@code cdr} value of the {@link AbstractConsCell}
	 */
	@SuppressWarnings("unchecked")
	public AbstractConsCell(Object car, T carType, C cdr) {
		this.car = car;
		this.carType = carType == null ? getEmptyType() : carType;
		this.cdr = cdr;
		this.cdrType = getConsCellType();
		cdr.setPrevious((C) this);
		this.previous = null;
	}
	
	/**
	 * Constructs a {@link AbstractConsCell} with the given {@code car} and {@code cdr} values.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link AbstractConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 * @param cdr
	 *            the {@code cdr} value of the {@link AbstractConsCell}
	 * @param cdrType
	 *            the {@link ConsType type} of the {@code cdr} value
	 */
	@SuppressWarnings("unchecked")
	public AbstractConsCell(Object car, T carType, Object cdr, T cdrType) {
		this.car = car;
		this.carType = carType == null ? getEmptyType() : carType;
		this.cdr = cdr;
		this.cdrType = cdrType == null ? getEmptyType() : cdrType;
		if (cdr instanceof AbstractConsCell)
			((AbstractConsCell<C, T>) cdr).setPrevious((C) this);
		this.previous = null;
	}
	
	@Override
	public Object getCar() {
		return car;
	}
	
	@Override
	public T getCarType() {
		return carType;
	}
	
	@Override
	public void setCar(Object car, T carType) {
		this.car = car;
		this.carType = carType;
	}
	
	@Override
	public void replaceCar(C container) {
		setCar(container.getCar(), container.getCarType());
	}
	
	@Override
	public Object getCdr() {
		return cdr;
	}
	
	@Override
	public T getCdrType() {
		return cdrType;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void setCdr(Object cdr, T cdrType) {
		if (getCdr() == cdr) { //If the current cdr value is the same as the one being set, just update the type
			this.cdrType = cdrType;
			return;
		}
		if (getCdr() instanceof AbstractConsCell)
			((AbstractConsCell<?, ?>) getCdr()).setPreviousInner(null);
		if (cdr instanceof AbstractConsCell)
			((AbstractConsCell<C, T>) cdr).setPrevious((C) this); //Needs to be the full method because the new value could have already had a previous ConsCell
		setCdrInner(cdr, cdrType);
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
	protected void setCdrInner(Object cdr, T cdrType) {
		this.cdr = cdr;
		this.cdrType = cdrType;
	}
	
	@Override
	public void replaceCdr(C container) {
		setCdr(container.getCdr(), container.getCdrType());
	}
	
	@Override
	public C getLast() {
		for (@SuppressWarnings("unchecked") C current = (C) this;; current = current.getNext())
			if (current.isLast())
				return current;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public C getNext() {
		return getClass().isInstance(getCdr()) ? (C) getCdr() : null;
	}
	
	@Override
	public C getNext(int n) {
		if (n < 0) //If n < 0, this is equivalent to getting the -nth previous ConsCell
			return getPrevious(-n);
		@SuppressWarnings("unchecked") C out = (C) this;
		for (; n > 0 && out != null; n--)
			out = out.getNext();
		return out;
	}
	
	@Override
	public void setNext(C next) {
		setNext(next, getConsCellType());
	}
	
	@Override
	public void setNext(C next, T type) {
		setCdr(next, type);
	}
	
	@Override
	public C getFirst() {
		for (@SuppressWarnings("unchecked") C current = (C) this;; current = current.getPrevious())
			if (current.isFirst())
				return current;
	}
	
	@Override
	public C getPrevious() {
		return previous;
	}
	
	@Override
	public C getPrevious(int n) {
		if (n < 0) //If n < 0, this is equivalent to getting the -nth next ConsCell
			return getNext(-n);
		@SuppressWarnings("unchecked") C out = (C) this;
		for (; n > 0 && out != null; n--)
			out = out.getPrevious();
		return out;
	}
	
	protected void setPrevious(C previous) {
		if (getPrevious() == previous)
			return;
		if (getPrevious() != null)
			getPrevious().setCdrInner(null, null);
		setPreviousInner(previous);
		return;
	}
	
	/**
	 * This controls how the {@link #getPrevious() previous} field is assigned. Overriding classes that are not using the
	 * provided previous field should override this method instead of {@link #setPrevious(AbstractConsCell)}.<br>
	 * <b>Implementations of this method must <i>not</i> perform any processing beyond simple field assignment.</b>
	 * 
	 * @param previous
	 *            the new previous {@link AbstractConsCell}
	 */
	protected void setPreviousInner(C previous) {
		this.previous = previous;
	}
	
	@Override
	public boolean isFirst() {
		return getPrevious() == null;
	}
	
	@Override
	public boolean isLast() {
		return getClass().isInstance(getCdr());
	}
	
	@Override
	public boolean isEmpty() {
		return getCarType() == getEmptyType() && getCdrType() == getEmptyType();
	}
	
	@Override
	public boolean hasLength(int n) {
		if (n == 0) //If n == 0, then the answer is always true
			return true;
		else if (n > 0) {
			if (--n != 0) //If n == 1, we are effectively checking whether the current ConsCell exists
				for (AbstractConsCell<C, T> current = this; n > 0 && current != null; n--)
					if ((current = current.getNext()) == null)
						return false;
			return true;
		}
		else { //If n < 0, look backwards
			if (++n != 0) //If n == -1, we are effectively checking whether the current ConsCell exists
				for (AbstractConsCell<C, T> current = this; n < 0 && current != null; n++)
					if ((current = current.getPrevious()) == null)
						return false;
			return true;
		}
	}
	
	@Override
	public int length() {
		int l = 0;
		for (AbstractConsCell<C, T> current = this; current != null; current = current.getNext())
			l++;
		return l;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getCar(), getCarType(), getCdr(), getCdrType());
	}
	
	@Override
	public boolean equals(Object o) {
		if (!getClass().isInstance(o))
			return false;
		AbstractConsCell<?, ?> c = (AbstractConsCell<?, ?>) o;
		return (getCarType() == null ? c.getCarType() == null : getCarType().equals(c.getCarType())) && (getCar() == null ? c.getCar() == null : getCar().equals(c.getCar())) && (getCdrType() == null
				? c.getCdrType() == null : getCdrType().equals(c.getCdrType())) && (getCdr() == null ? c.getCdr() == null : getCdr().equals(c.getCdr()));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public C insert(C next) {
		if (isEmpty()) {
			replaceCar(next);
			replaceCdr(next);
			return (C) this;
		}
		Object cdr = getCdr();
		T type = getCdrType();
		setCdr(next, getConsCellType());
		next.getLast().setCdr(cdr, type);
		return next;
	}
	
	@Override
	public C append(C next) {
		if (isEmpty()) {
			replaceCar(next);
			replaceCdr(next);
			return getLast();
		}
		if (getClass().isInstance(getCdr()))
			getClass().cast(getCdr()).setPrevious(null);
		setCdr(next, getConsCellType());
		return next.getLast();
	}
	
	@Override
	public C remove() {
		C next = getNext();
		if (getPrevious() != null)
			getPrevious().setCdr(getCdr(), getCdrType());
		else if (next != null)
			next.setPreviousInner(null);
		setCdrInner(null, null);
		return next;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public C split() {
		if (getPrevious() != null)
			getPrevious().setCdr(null, getEmptyType());
		return (C) this;
	}
	
	@Override
	public C singular() {
		try {
			@SuppressWarnings("unchecked") C clone = (C) super.clone();
			clone.setPreviousInner(null);
			clone.setCdrInner(null, clone.getEmptyType());
			return clone;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e); //This shouldn't happen because we are Cloneable
		}
	}
	
	@Override
	public C clone() {
		return clone(null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public C clone(C previous) {
		try {
			C clone = (C) super.clone();
			clone.setPreviousInner(previous);
			if (previous != null)
				previous.setCdr(clone, previous.getConsCellType());
			clone.setCar(clone.getCarType().tryClone(clone.getCar()), clone.getCarType());
			clone.setCdrInner(clone.getCdrType().tryClone(clone.getCdr()), clone.getCdrType());
			if (AbstractConsCell.class.isInstance(clone.getCdr()))
				((AbstractConsCell<C, T>) clone.getCdr()).setPreviousInner(clone);
			return clone;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e); //This shouldn't happen because we are Cloneable
		}
	}
	
	@Override
	public C structuralClone() {
		return structuralClone(null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public C structuralClone(C previous) {
		try {
			C clone = (C) super.clone();
			clone.setPreviousInner(previous);
			if (previous != null)
				previous.setCdr(clone, previous.getConsCellType());
			if (GenericConsCell.class.isInstance(clone.getCar()))
				clone.setCar(((GenericConsCell<?, ?>) clone.getCar()).structuralClone(), clone.getCarType());
			if (AbstractConsCell.class.isInstance(clone.getCdr())) {
				clone.setCdrInner(((GenericConsCell<?, ?>) clone.getCdr()).structuralClone(), clone.getCdrType());
				((AbstractConsCell<C, T>) clone.getCdr()).setPreviousInner(clone);
			}
			return clone;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e); //This shouldn't happen because we are Cloneable
		}
	}
	
	@Override
	public String toString() {
		return toString(new StringBuilder()).toString();
	}
	
	@Override
	public StringBuilder toString(StringBuilder sb) {
		getCarType().valueToString(getCar(), sb);
		if (getCdrType() != getEmptyType()) {
			sb.append(" ");
			getCdrType().valueToString(getCdr(), sb);
		}
		return sb;
	}
	
	@Override
	public String structureString() {
		StringBuilder sb = new StringBuilder();
		for (AbstractConsCell<C, T> current = this; current != null; current = current.getNext()) {
			if (current.getCarType() != current.getEmptyType())
				structureStringAppend(current.getCar(), current.getCarType(), sb);
			if (current.getCdrType() != current.getEmptyType() && current.isLast())
				structureStringAppend(current.getCdr(), current.getCdrType(), sb);
		}
		if (sb.length() > 2)
			sb.delete(sb.length() - 2, sb.length());
		return sb.toString().trim();
	}
	
	private void structureStringAppend(Object value, T type, StringBuilder sb) {
		if (type.marksDescender())
			sb.append(type.getOpen()).append(GenericConsCell.class.isInstance(value) ? GenericConsCell.class.cast(value).structureString() : (value == null ? "" : value.toString()))
					.append(type.getClose());
		else
			sb.append(GenericConsCell.class.isInstance(value) ? GenericConsCell.class.cast(value).structureString() : String.valueOf(value));
		sb.append(": ").append(type).append(", ");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Iterator<C> iterator() {
		return new Itr((C) this);
	}
	
	protected class Itr implements Iterator<C> {
		private C current, next;
		
		public Itr(C start) {
			next = start;
			current = null;
		}
		
		@Override
		public boolean hasNext() {
			return next != null;
		}
		
		@Override
		public C next() {
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
	
	/**
	 * @return the {@link ConsType} used to indicate that a field is empty
	 */
	protected abstract T getEmptyType();
	
	/**
	 * @return the {@link ConsType} used to indicate that a field is a {@link AbstractConsCell}
	 */
	protected abstract T getConsCellType();
}
