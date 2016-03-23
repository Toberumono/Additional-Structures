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
}
