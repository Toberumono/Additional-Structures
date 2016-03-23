package toberumono.structures.sexpressions;

/**
 * Implementations of the two {@link ConsType ConsTypes} required to implement {@link ConsCell}.
 * 
 * @author Toberumono
 */
public enum CoreConsType implements ConsType {
	/**
	 * The default type used to indicate empty values
	 */
	EMPTY(null, null, "empty"),
	/**
	 * The default type used to indicate values that implement {@link ConsCell}
	 */
	CONS_CELL("(", ")", "ConsCell");
	private final String open, close, name;
	
	CoreConsType(String open, String close, String name) {
		this.open = open;
		this.close = close;
		this.name = name;
	}
	
	@Override
	public String getOpen() {
		return open;
	}
	
	@Override
	public String getClose() {
		return close;
	}
	
	@Override
	public String getName() {
		return name;
	}
}
