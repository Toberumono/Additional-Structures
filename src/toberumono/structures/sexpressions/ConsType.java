package toberumono.structures.sexpressions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base interface for type flags used in {@link ConsCell} and its subclasses.<br>
 * <b>All implementations of {@link ConsType} <i>must</i> be immutable.</b>
 * 
 * @author Toberumono
 */
public interface ConsType {
	
	/**
	 * @return the open symbol for the {@link ConsType} if it indicates a descender, otherwise null
	 */
	public String getOpen();
	
	/**
	 * @return the close symbol for the {@link ConsType} if it indicates a descender, otherwise null
	 */
	public String getClose();
	
	/**
	 * @return the name of the {@link ConsType}
	 */
	public String getName();
	
	/**
	 * @return whether the {@link ConsType} indicates a descender (its associated field is a subclass of {@link ConsCell})
	 */
	public default boolean marksDescender() {
		return getOpen() != null;
	}
	
	/**
	 * Uses the {@link String#hashCode() hashCode} of the name of the {@link ConsType}. In essence,
	 * {@code hashCode() = getName().hashCode()}<br>
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode();
	
	/**
	 * Used by {@link ConsCell#toString()} to produce type-specific {@link String} representations of values.<br>
	 * The default method just brackets the value with the {@link #getOpen() open} and {@link #getClose() close} symbols if
	 * applicable. However, this can be used to generate more detailed output if desired.
	 * 
	 * @param value
	 *            the {@link Object} to convert to a {@link String}
	 * @param sb
	 *            the {@link StringBuilder} into which the representation should be written
	 * @return the {@link String} representation of {@code value} as a function of its {@link ConsType type}
	 */
	public default StringBuilder valueToString(Object value, StringBuilder sb) {
		if (marksDescender())
			if (value instanceof ConsCell) {
				sb.append(getOpen());
				((ConsCell) value).toString(sb);
				return sb.append(getClose());
			}
			else
				return sb.append(getOpen()).append(value).append(getClose());
		else if (value instanceof ConsCell)
			return ((ConsCell) value).toString(sb);
		else
			return sb.append(value);
	}
	
	/**
	 * Used by {@link ConsCell#toString()} to produce type-specific {@link String} representations of values.<br>
	 * This method forwards to {@link #valueToString(Object, StringBuilder)} and it should be left that way.
	 * 
	 * @param value
	 *            the {@link Object} to convert to a {@link String}
	 * @return the {@link String} representation of {@code value} as a function of its {@link ConsType type}
	 */
	public default String valueToString(Object value) {
		return valueToString(value, new StringBuilder()).toString();
	}
	
	/**
	 * Used by {@link ConsCell#clone()} and {@link ConsCell#structuralClone()} to attempt to clone an object based on its
	 * {@link ConsType type}. Overriding this method allows for {@link ConsType type}-specific cloning logic.<br>
	 * By default, this attempts to clone the object via its clone method directly if it implements {@link ConsCell}, via its
	 * clone method through reflection if it implements {@link Cloneable}, and, if both of those fail, via its copy
	 * constructor (if one exists) through reflection.
	 * 
	 * @param obj
	 *            the {@link Object} to try to clone
	 * @return the clone of {@code obj} if one was created successfully, otherwise {@code obj}
	 */
	public default Object tryClone(Object obj) {
		if (obj == null)
			return obj;
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
}
