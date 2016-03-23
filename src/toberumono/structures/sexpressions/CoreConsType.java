package toberumono.structures.sexpressions;

public enum CoreConsType implements ConsType {
	EMPTY(null, null, "empty"),
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
