package toberumono.structures.sexpressions;

/**
 * Represents the basic constructor for an implementation of {@link ConsCell}.
 * 
 * @author Toberumono
 * @param <C>
 *            the implementation of {@link GenericConsCell} in use
 * @param <T>
 *            the implementation of {@link ConsType} in use
 */
@FunctionalInterface
public interface ConsCellConstructor<C extends GenericConsCell<C, T>, T extends ConsType> {
	
	/**
	 * Constructs a {@link GenericConsCell} with the given {@code car} and {@code cdr} values.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link GenericConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 * @param cdr
	 *            the {@code cdr} value of the {@link GenericConsCell}
	 * @param cdrType
	 *            the {@link ConsType type} of the {@code cdr} value
	 * @return the constructed {@link ConsCell}
	 */
	public C construct(Object car, T carType, Object cdr, T cdrType);
	
	/**
	 * @return an empty {@link ConsCell}
	 */
	public default C construct() {
		return construct(null, null, null, null);
	}
}
