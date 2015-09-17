package toberumono.structures.sexps;

/**
 * Represents the basic constructor for an implementation of {@link GenericConsCell}.
 * 
 * @author Toberumono
 * @param <Ty>
 *            the implementation of {@link GenericConsType} in use
 * @param <To>
 *            the implementation of {@link GenericConsCell} in use
 */
@FunctionalInterface
public interface ConsCellConstructor<Ty extends GenericConsType, To extends GenericConsCell<Ty, To>> {
	/**
	 * Constructs a new token with the given fields.
	 * 
	 * @param car
	 *            the car value
	 * @param carType
	 *            the car type
	 * @param cdr
	 *            the cdr value
	 * @param cdrType
	 *            the cdr type
	 * @return the new token
	 */
	public To construct(Object car, Ty carType, Object cdr, Ty cdrType);
	
	/**
	 * @return an empty token
	 */
	public default To construct() {
		return construct(null, null, null, null);
	}
}
