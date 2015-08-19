package toberumono.structures.tuples;

/**
 * An immutable pair of objects that can be used either with wholly new parameters or as a wrapper on an existing
 * {@link Triple}.
 * 
 * @author Toberumono
 * @param <X>
 *            the type of the first element
 * @param <Y>
 *            the type of the second element
 * @param <Z>
 *            the type of the third element
 * @see Triple
 */
public class ImmutableTriple<X, Y, Z> extends Triple<X, Y, Z> {
	private final Triple<X, Y, Z> mutable;
	
	/**
	 * Constructs a new {@link ImmutableTriple} with the given elements.
	 * 
	 * @param x
	 *            the first element
	 * @param y
	 *            the second element
	 * @param z
	 *            the third element
	 */
	public ImmutableTriple(X x, Y y, Z z) {
		mutable = new Triple<>(x, y, z);
	}
	
	/**
	 * Constructs a new {@link ImmutableTriple} that wraps the given {@link Triple}
	 * 
	 * @param mutable
	 *            the {@link Triple} to wrap
	 */
	public ImmutableTriple(Triple<X, Y, Z> mutable) {
		this.mutable = mutable;
	}
	
	@Override
	public X getX() {
		return mutable.getX();
	}
	
	@Override
	public Y getY() {
		return mutable.getY();
	}
	
	@Override
	public Z getZ() {
		return mutable.getZ();
	}
	
	@Override
	public void setX(X newX) {
		throw new UnsupportedOperationException("Cannot set the value a field in an immutable pair.");
	}
	
	@Override
	public void setY(Y newY) {
		throw new UnsupportedOperationException("Cannot set the value a field in an immutable pair.");
	}
	
	@Override
	public void setZ(Z newZ) {
		throw new UnsupportedOperationException("Cannot set the value a field in an immutable pair.");
	}
	
	@Override
	public String toString() {
		return mutable.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return mutable.equals(o);
	}
}
