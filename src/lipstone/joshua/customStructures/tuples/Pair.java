package lipstone.joshua.customStructures.tuples;

/**
 * An pair of objects - simplifies passing of directly related objects
 * 
 * @author Joshua Lipstone
 * @param <X>
 *            the type of the first element
 * @param <Y>
 *            the type of the second element
 */
public class Pair<X, Y> {
	private X x;
	private Y y;
	
	/**
	 * Constructs a new <tt>Pair</tt> with elements of types X and Y
	 * 
	 * @param x
	 *            the first element
	 * @param y
	 *            the second element
	 */
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the first item in this {@link Pair}
	 */
	public X getX() {
		return x;
	}
	
	/**
	 * @return the second item in this {@link Pair}
	 */
	public Y getY() {
		return y;
	}
	
	/**
	 * Changes the first item in this {@link Pair} to <tt>newX</tt>
	 * 
	 * @param newX
	 *            the new value for the first item in this {@link Pair}
	 */
	public void setX(X newX) {
		x = newX;
	}
	
	/**
	 * Changes the second item in this {@link Pair} to <tt>newY</tt>
	 * 
	 * @param newY
	 *            the new value for the second item in this {@link Pair}
	 */
	public void setY(Y newY) {
		y = newY;
	}
	
	/**
	 * @return a {@link String} containing: "&lt;x, y&gt;"
	 */
	@Override
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair))
			return false;
		Pair<?, ?> p = (Pair<?, ?>) o;
		return (x == null ? x == p.x : x.equals(p.x)) && (y == null ? y == p.y : y.equals(p.y));
	}
}