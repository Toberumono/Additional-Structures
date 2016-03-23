package toberumono.structures.sexpressions;

/**
 * Represents the basic constructor for an implementation of {@link ConsCell}.
 * 
 * @author Toberumono
 * @param <Ty>
 *            the implementation of {@link ConsType} in use
 * @param <To>
 *            the implementation of {@link ConsCell} in use
 */
@FunctionalInterface
public interface ConsCellConstructor<Ty extends ConsType, To extends ConsCell> {
	
	/**
	 * Constructs a {@link ConsCell} with the given {@code car} and {@code cdr} values.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link ConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 * @param cdr
	 *            the {@code cdr} value of the {@link ConsCell}
	 * @param cdrType
	 *            the {@link ConsType type} of the {@code cdr} value
	 * @return the constructed {@link ConsCell}
	 */
	public To construct(Object car, Ty carType, Object cdr, Ty cdrType);
	
	/**
	 * @return an empty {@link ConsCell}
	 */
	public default To construct() {
		return construct(null, null, null, null);
	}
}
