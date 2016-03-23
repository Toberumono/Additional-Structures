package toberumono.structures.sexpressions;

import java.util.Objects;

/**
 * An implementation of {@link ConsType} for types defined at runtime.<br>
 * <b>Instances of BasicConsType are immutable.</b>
 * 
 * @author Toberumono
 */
public class BasicConsType implements ConsType {
	private final String open, close;
	private final String name;
	
	/**
	 * Defines a {@link ConsType} that does not mark a descender.
	 * 
	 * @param name
	 *            the name of the {@link ConsType} this <i>should</i> be unique in order to ensure optimal operation
	 */
	public BasicConsType(String name) {
		this(name, null, null);
	}
	
	/**
	 * Defines a {@link ConsType} that marks a descender if {@code open} and {@code close} are not {@code null}.
	 * 
	 * @param name
	 *            the name of the {@link ConsType}. This <i>should</i> be unique in order to ensure optimal operation
	 * @param open
	 *            the open token of the descender if the {@link ConsType} marks a descender, otherwise {@code null}. If
	 *            {@code close} is non-null, this <i>must</i> be non-null as well.
	 * @param close
	 *            the close token of the descender if the {@link ConsType} marks a descender, otherwise {@code null}. If
	 *            {@code open} is non-null, this <i>must</i> be non-null as well.
	 * @throws IllegalArgumentException
	 *             if {@code open} is non-null and {@code close} is null or {@code open} is null and {@code close} is
	 *             non-null
	 */
	public BasicConsType(String name, String open, String close) {
		this.name = Objects.requireNonNull(name, "The name of a type cannot be null.");
		if (open != null ^ close != null)
			throw new IllegalArgumentException("If one of either open or close is not null, the other cannot be null.");
		this.open = open;
		this.close = close;
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
	
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getOpen(), getClose());
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ConsType))
			return false;
		ConsType c = (ConsType) o;
		return getName().equals(c.getName()) && (getOpen() == null ? c.getOpen() == null : getOpen().equals(c.getOpen())) &&
				(getClose() == null ? c.getClose() == null : getClose().equals(c.getClose()));
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
