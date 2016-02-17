package toberumono.structures.interfaces;

/**
 * A convenience interface for use when only the {@code ID} of an {@link Object} matters.
 * 
 * @author Toberumono
 * @param <T>
 *            the type of the {@code ID}
 */
@FunctionalInterface
public interface IDable<T> {
	
	/**
	 * @return the {@code ID} of the object
	 */
	public T getID();
}
