package toberumono.structures.tuples;

/**
 * An immutable pair of objects that can be used either with wholly new parameters or as a wrapper on an existing
 * {@link Pair}.
 * 
 * @author Toberumono
 * @param <X>
 *            the type of the first element
 * @param <Y>
 *            the type of the second element
 * @see Pair
 */
public class ImmutablePair<X, Y> extends Pair<X, Y> {
	private final Pair<X, Y> mutable;
	
	/**
	 * Constructs a new {@link ImmutablePair} with the given elements.
	 * 
	 * @param x
	 *            the first element
	 * @param y
	 *            the second element
	 */
	public ImmutablePair(X x, Y y) {
		mutable = new Pair<>(x, y);
	}
	
	/**
	 * Constructs a new {@link ImmutablePair} that wraps the given {@link Pair}
	 * 
	 * @param mutable
	 *            the {@link Pair} to wrap
	 */
	public ImmutablePair(Pair<X, Y> mutable) {
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
	public void setX(X newX) {
		throw new UnsupportedOperationException("Cannot set the value a field in an immutable pair.");
	}
	
	@Override
	public void setY(Y newY) {
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
