package toberumono.structures.sexpressions;

/**
 * Constructible implementation of {@link AbstractConsCell}.<br>
 * In order for cloning to work, all classes in this package must have the "accessDeclaredMembers" {@link RuntimePermission}
 * if a {@link SecurityManager} is enabled.
 * 
 * @author Toberumono
 */
public class ConsCell extends AbstractConsCell<ConsCell, ConsType> implements GenericConsCell<ConsCell, ConsType>, Cloneable {
	
	/**
	 * Constructs an empty {@link ConsCell}.
	 */
	public ConsCell() {
		super();
	}
	
	/**
	 * Constructs a {@link ConsCell} with an empty {@code cdr} value.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link ConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 */
	public ConsCell(Object car, ConsType carType) {
		super(car, carType);
	}
	
	/**
	 * Constructs a {@link ConsCell} with a {@link ConsCell} as its {@code cdr} value.
	 * 
	 * @param car
	 *            the {@code car} value of the {@link ConsCell}
	 * @param carType
	 *            the {@link ConsType type} of the {@code car} value
	 * @param cdr
	 *            the {@code cdr} value of the {@link ConsCell}
	 */
	public ConsCell(Object car, ConsType carType, ConsCell cdr) {
		super(car, carType, cdr);
	}
	
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
	 */
	public ConsCell(Object car, ConsType carType, Object cdr, ConsType cdrType) {
		super(car, carType, cdr, cdrType);
	}
	
	/**
	 * @return {@link CoreConsType#EMPTY}
	 */
	@Override
	protected ConsType getEmptyType() {
		return CoreConsType.EMPTY;
	}
	
	/**
	 * @return {@link CoreConsType#CONS_CELL}
	 */
	@Override
	protected ConsType getConsCellType() {
		return CoreConsType.CONS_CELL;
	}
}
