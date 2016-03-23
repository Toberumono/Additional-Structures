package toberumono.structures.sexpressions;

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
		return getOpen() == null;
	}
	
	/**
	 * Uses the {@link String#hashCode() hashCode} of the name of the {@link ConsType}. In essence,
	 * {@code hashCode() = getName().hashCode()}<br>
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode();
}
