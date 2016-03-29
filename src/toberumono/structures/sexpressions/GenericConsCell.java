package toberumono.structures.sexpressions;

import java.util.Iterator;

/**
 * Interface for an implementation of a combination doubly-linked list and s-expression structure based on cons cells from
 * Lisp.<br>
 * In order for cloning to work, this package must have the "accessDeclaredMembers" {@link RuntimePermission} if a
 * {@link SecurityManager} is enabled.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link GenericConsCell} being used
 * @param <T>
 *            the implementation of {@link ConsType} being used
 */
public interface GenericConsCell<C extends GenericConsCell<C, T>, T extends ConsType> extends Iterable<C> {
	
	/**
	 * @return the {@code car} value of the {@link GenericConsCell} (the value specified by the left pointer)
	 * @see #getCarType()
	 * @see #getCdr()
	 */
	public Object getCar();
	
	/**
	 * @return the {@link ConsType type} of the {@code car} value of the {@link GenericConsCell} (the type of the value
	 *         specified by the left pointer)
	 */
	public T getCarType();
	
	/**
	 * Sets the {@code car} value and type of the {@link GenericConsCell} to the provided value and type.
	 * 
	 * @param car
	 *            the new {@code car} value
	 * @param carType
	 *            the {@link ConsType type} of the new {@code car} value
	 * @see #replaceCar(GenericConsCell)
	 * @see #setCdr(Object, ConsType)
	 */
	public void setCar(Object car, T carType);
	
	/**
	 * Replaces the {@code car} value and type of the {@link GenericConsCell} with those of the provided
	 * {@link GenericConsCell}.
	 * 
	 * @param container
	 *            the {@link GenericConsCell} containing the {@code car} value and type with which the {@link GenericConsCell
	 *            GenericConsCell's} {@code car} value and type will be replaced
	 * @see #setCar(Object, ConsType)
	 * @see #replaceCdr(GenericConsCell)
	 */
	public void replaceCar(C container);
	
	/**
	 * @return the {@code cdr} value of the {@link GenericConsCell} (the value specified by the right pointer)
	 * @see #getCdrType()
	 * @see #getCar()
	 */
	public Object getCdr();
	
	/**
	 * @return the {@link ConsType type} of the {@code cdr} value of the {@link GenericConsCell} (the type of the value
	 *         specified by the right pointer)
	 */
	public T getCdrType();
	
	/**
	 * Sets the {@code cdr} value and type of the {@link GenericConsCell} to the provided value and type. If the original
	 * {@code cdr} value was a {@link GenericConsCell}, then the value of its {@link #getPrevious() previous} field will be
	 * set to null. If the new {@code cdr} value is a {@link GenericConsCell}, then the {@link #getCdr() cdr} value of the
	 * {@link GenericConsCell} in its {@link #getPrevious() previous} field will be set to {@code null} and marked as empty
	 * if its {@link #getPrevious() previous} field was non-null. The value of the {@link #getPrevious() previous} field of
	 * the new {@code cdr} value will be set to the {@link GenericConsCell} upon which this method was called if the new
	 * {@code cdr} value is a {@link GenericConsCell}.
	 * 
	 * @param cdr
	 *            the new {@code cdr} value
	 * @param cdrType
	 *            the {@link ConsType type} of the new {@code cdr} value
	 * @see #replaceCdr(GenericConsCell)
	 * @see #setCar(Object, ConsType)
	 */
	public void setCdr(Object cdr, T cdrType);
	
	/**
	 * Replaces the {@code car} value and type of the {@link GenericConsCell} with those of the provided
	 * {@link GenericConsCell}. If the original {@code cdr} value was a {@link GenericConsCell}, then the value of its
	 * {@link #getPrevious() previous} field will be set to null. If the new {@code cdr} value is a {@link GenericConsCell},
	 * then the {@link #getCdr() cdr} value of the {@link GenericConsCell} in its {@link #getPrevious() previous} field will
	 * be set to null if its {@link #getPrevious() previous} field was non-null. The value of the {@link #getPrevious()
	 * previous} field of the new {@code cdr} value will be set to the {@link GenericConsCell} upon which this method was
	 * called if the new {@code cdr} value is a {@link GenericConsCell}.
	 * 
	 * @param container
	 *            the {@link GenericConsCell} containing the {@code car} value and type with which the {@link GenericConsCell
	 *            GenericConsCell's} {@code car} value and type will be replaced
	 * @see #setCdr(Object, ConsType)
	 * @see #replaceCar(GenericConsCell)
	 */
	public void replaceCdr(C container);
	
	/**
	 * The last {@link GenericConsCell} is defined as the {@link GenericConsCell} that has a {@code cdr} value that is not an
	 * instance of {@link GenericConsCell}.
	 * 
	 * @return the last {@link GenericConsCell} in the current level of the {@link GenericConsCell GenericConsCell's} tree
	 */
	public C getLast();
	
	/**
	 * The next {@link GenericConsCell} is the {@code cdr} value of the {@link GenericConsCell} if the {@code cdr} value is
	 * an instance of {@link GenericConsCell}.
	 * 
	 * @return the next {@link GenericConsCell} in the tree if there is one, otherwise {@code null}
	 */
	public C getNext();
	
	/**
	 * The next {@link GenericConsCell} is the {@code cdr} value of the {@link GenericConsCell} if the {@code cdr} value is
	 * an instance of {@link GenericConsCell}.
	 * 
	 * @param n
	 *            the number of {@link GenericConsCell GenericConsCells} by which to step forward
	 * @return the nth-next {@link GenericConsCell} in the tree if there is one, otherwise {@code null}
	 */
	public C getNext(int n);
	
	/**
	 * Sets the next {@link GenericConsCell} in the {@link GenericConsCell GenericConsCell's} tree. This forwards to
	 * {@link #setNext(GenericConsCell, ConsType)} with the {@link ConsType} being used to represent {@link GenericConsCell
	 * GenericConsCells}.<br>
	 * <b>Note:</b> the next {@link GenericConsCell} is still stored in the {@link #getCdr() cdr} value.
	 * 
	 * @param next
	 *            the new next {@link GenericConsCell}
	 * @see #setNext(GenericConsCell, ConsType)
	 * @see #setCdr(Object, ConsType)
	 */
	public void setNext(C next);
	
	/**
	 * Sets the next {@link GenericConsCell} in the {@link GenericConsCell GenericConsCell's} tree. This forwards to
	 * {@link #setCdr(Object, ConsType)}.<br>
	 * <b>Note:</b> the next {@link GenericConsCell} is still stored in the {@link #getCdr() cdr} value.
	 * 
	 * @param next
	 *            the new next {@link GenericConsCell}
	 * @param type
	 *            the {@link ConsType type} of {@code next}
	 */
	public void setNext(C next, T type);
	
	/**
	 * The first {@link GenericConsCell} is defined as the {@link GenericConsCell} that has a {@code null}
	 * {@link #getPrevious() previous} field.
	 * 
	 * @return the first {@link GenericConsCell} in the current level of the {@link GenericConsCell GenericConsCell's} tree
	 */
	public C getFirst();
	
	/**
	 * @return the previous (or parent) {@link GenericConsCell} in the tree
	 */
	public C getPrevious();
	
	/**
	 * @param n
	 *            the number of {@link GenericConsCell GenericConsCells} by which to step backward
	 * @return the nth-previous (or parent) {@link GenericConsCell} in the tree
	 */
	public C getPrevious(int n);
	
	/**
	 * @return {@code true} iff the {@link GenericConsCell} does not have a {@link #getPrevious() previous}
	 *         {@link GenericConsCell} (that is, {@link #getPrevious()} returns {@code null})
	 */
	public boolean isFirst();
	
	/**
	 * @return {@code true} iff the {@link GenericConsCell} does not have a {@link #getNext() next} {@link GenericConsCell}
	 *         (that is, {@link #getNext()} returns {@code null})
	 */
	public boolean isLast();
	
	/**
	 * @return {@code true} iff the types {@link GenericConsCell GenericConsCell's} {@code car} and {@code cdr} types are
	 *         both the {@link ConsType type} being used to represent empty values
	 */
	public boolean isEmpty();
	
	/**
	 * Determines whether there are at least {@code n} {@link GenericConsCell GenericConsCells} including the one on which
	 * the method was called in the current level of the tree with the {@link GenericConsCell} on which this method was
	 * called as its root. If {@code n} is negative, it looks backwards instead of forwards. (So, if {@code n} is
	 * {@code -1, 0, 1}, the method will always return {@code true})
	 * 
	 * @param n
	 *            the number of {@link GenericConsCell GenericConsCells} to test for
	 * @return {@code true} iff there are at least {@code n} {@link GenericConsCell GenericConsCells} in the direction being
	 *         checked
	 */
	public boolean hasLength(int n);
	
	/**
	 * @return the number of {@link GenericConsCell GenericConsCells} in the first level tree with the
	 *         {@link GenericConsCell} on which this method was called as its root
	 */
	public int length();
	
	/**
	 * This method appends the given {@link GenericConsCell} or {@link GenericConsCell GenericConsCells} to this one, and, if
	 * this cell is empty as defined in {@link #isEmpty()}, overwrites this the contents of this {@link GenericConsCell} with
	 * the first {@link GenericConsCell} to be inserted.<br>
	 * If this {@link GenericConsCell GenericConsCell's} {@code cdr} value is also a {@link GenericConsCell}, then
	 * {@link #insert(GenericConsCell)} is recursively called on the last cell in the inserted tree's root level (found via a
	 * call to {@link #getLast()}) with this {@link GenericConsCell GenericConsCell's} {@code cdr} value and type as the
	 * arguments. If this {@link GenericConsCell GenericConsCell's} {@code cdr} value is not empty and is not a
	 * {@link GenericConsCell}, then the {@code cdr} value of the last {@link GenericConsCell} in the given
	 * {@link GenericConsCell GenericConsCells'} {@code cdr} value is set to this {@link GenericConsCell GenericConsCell's}
	 * {@code cdr} value.
	 * 
	 * @param next
	 *            the {@link GenericConsCell} to insert
	 * @return {@code this} if {@code this.isEmpty()} was {@code true}, otherwise {@code next}
	 * @see #append(GenericConsCell)
	 */
	public C insert(C next);
	
	/**
	 * Appends the given {@link GenericConsCell} to the {@link GenericConsCell} on which this method was called.<br>
	 * Roughly equivalent in function to {@link #insert(GenericConsCell)} - the only difference is that this returns the last
	 * {@link GenericConsCell} in the equivalent level of the resulting tree.
	 * 
	 * @param next
	 *            the cell to append
	 * @return the last cell in the equivalent level of the resulting cell tree. Equivalent to
	 *         {@code cell.insert(next).getLastConsCell()}.
	 * @see #insert(GenericConsCell)
	 */
	public C append(C next);
	
	/**
	 * Removes the {@link GenericConsCell} from the tree and returns the one after it.
	 * 
	 * @return the next {@link GenericConsCell} in the tree as determined by {@link #getNext()}
	 * @see #getNext()
	 */
	public C remove();
	
	/**
	 * Splits the cell tree on this {@link GenericConsCell}. After the split, the original tree will end at the this
	 * {@link GenericConsCell GenericConsCell's} {@link #getPrevious() previous} {@link GenericConsCell}, and this
	 * {@link GenericConsCell} will be the root of the new tree.
	 * 
	 * @return {@code this}
	 */
	public C split();
	
	/**
	 * Returns a shallow copy of this {@link GenericConsCell} with only the {@code car} value and {@link ConsType type}.<br>
	 * This effectively creates a {@link GenericConsCell} with a pointer to the same car value as the {@link GenericConsCell}
	 * on which this method was called but separate from its tree.
	 * 
	 * @return a shallow copy of the {@link GenericConsCell} that is separate from the tree
	 */
	public C singular();
	
	/**
	 * This method will attempt to clone the {@link #getCar() car} and {@link #getCdr() cdr} values via the
	 * {@link ConsType#tryClone(Object)} methods from the {@link #getCarType() carType} and {@link #getCdrType() cdrType}
	 * values.<br>
	 * 
	 * @return an independent clone of the {@link GenericConsCell} with the {@link GenericConsCell} on which the method was
	 *         called as the root of the new tree
	 */
	public C clone();
	
	/**
	 * This method will attempt to clone the {@link #getCar() car} and {@link #getCdr() cdr} values via the
	 * {@link ConsType#tryClone(Object)} methods from the {@link #getCarType() carType} and {@link #getCdrType() cdrType}
	 * values.
	 * 
	 * @param previous
	 *            the previous {@link GenericConsCell} in the tree (effectively the cloned {@link GenericConsCell
	 *            GenericConsCell's} parent)
	 * @return an independent clone of the {@link GenericConsCell} with the given {@code previous} {@link GenericConsCell} as
	 *         the parent of the new tree
	 */
	public C clone(C previous);
	
	/**
	 * Clones the structural elements of the {@link GenericConsCell GenericConsCell's} tree <i>only</i>. That is, only car
	 * and cdr elements that implement {@link GenericConsCell} are cloned. All cloning is performed via recursive calls to
	 * {@link #structuralClone()} or {@link #structuralClone(GenericConsCell)} as needed.
	 * 
	 * @return a structurally independent clone of the {@link GenericConsCell GenericConsCell's} tree with the
	 *         {@link GenericConsCell} on which the method was called as the root
	 */
	public C structuralClone();
	
	/**
	 * Clones the structural elements of the {@link GenericConsCell GenericConsCell's} tree only. That is, only car and cdr
	 * elements that implement {@link GenericConsCell} are cloned. All cloning is performed via recursive calls to
	 * {@link #structuralClone()} or {@link #structuralClone(GenericConsCell)} as needed.
	 * 
	 * @param previous
	 *            the previous {@link GenericConsCell} in the tree (effectively the cloned {@link GenericConsCell
	 *            GenericConsCell's} parent)
	 * @return a structurally independent clone of the {@link GenericConsCell GenericConsCell's} tree with the given
	 *         {@code previous} {@link GenericConsCell} as the parent of the new tree
	 */
	public C structuralClone(C previous);
	
	/**
	 * Returns an {@link Iterator} over the elements in the s-expression at the level of the {@link GenericConsCell} on which
	 * {@link #iterator()} was called.<br>
	 * <i>The first call to {@link Iterator#next() next()} returns the {@link GenericConsCell} on which {@link #iterator()}
	 * was called.</i>
	 * 
	 * @return an {@link Iterator}
	 */
	@Override
	public Iterator<C> iterator();
	
	@Override
	public String toString();
	
	/**
	 * An implementation of {@link #toString()} that writes the {@link String} representation of the {@link GenericConsCell}
	 * into the given {@link StringBuilder}.
	 * 
	 * @param sb
	 *            the {@link StringBuilder} into which the {@link String} representation of the {@link GenericConsCell}
	 *            should be written
	 * @return {@code sb} for chaining purposes
	 */
	public StringBuilder toString(StringBuilder sb);
	
	/**
	 * Generates a {@link String} that shows the structure of the {@link GenericConsCell GenericConsCell's} s-expression.<br>
	 * This is primarily a debugging function.
	 * 
	 * @return a {@link String} describing the {@link GenericConsCell GenericConsCell's} s-expression's structure
	 */
	public String structureString();
}
