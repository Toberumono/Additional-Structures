package toberumono.structures.tuples;
/**
 * An triple of objects - simplifies passing of directly related objects
 * 
 * @author Toberumono
 * @param <X>
 *            the type of the first element
 * @param <Y>
 *            the type of the second element
 * @param <Z>
 *            the type of the third element
 * @see ImmutableTriple
 */
public class Triple<X, Y, Z> {
	private X x;
	private Y y;
	private Z z;
	
	/**
	 * Constructs a new {@link Triple} with null elements.
	 */
	public Triple() {
		x = null;
		y = null;
		z = null;
	}
	
	/**
	 * Constructs a new {@link Triple} with the given elements.
	 * 
	 * @param x
	 *            the first element
	 * @param y
	 *            the second element
	 * @param z
	 *            the third element
	 */
	public Triple(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * @return the first item in this {@link Triple}
	 */
	public X getX() {
		return x;
	}
	
	/**
	 * @return the second item in this {@link Triple}
	 */
	public Y getY() {
		return y;
	}
	
	/**
	 * @return the third item in this {@link Triple}
	 */
	public Z getZ() {
		return z;
	}
	
	/**
	 * Changes the first item in this {@link Triple} to <tt>newX</tt>
	 * 
	 * @param newX
	 *            the new value for the first item in this {@link Triple}
	 */
	public void setX(X newX) {
		x = newX;
	}
	
	/**
	 * Changes the second item in this {@link Triple} to <tt>newY</tt>
	 * 
	 * @param newY
	 *            the new value for the second item in this {@link Triple}
	 */
	public void setY(Y newY) {
		y = newY;
	}
	
	/**
	 * Changes the third item in this {@link Triple} to <tt>newZ</tt>
	 * 
	 * @param newZ
	 *            the new value for the third item in this {@link Triple}
	 */
	public void setZ(Z newZ) {
		z = newZ;
	}
	
	/**
	 * @return a {@link String} equal to: "&lt;x, y, z&gt;"
	 */
	@Override
	public String toString() {
		return "<" + x + ", " + y + ", " + z + ">";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Triple))
			return false;
		Triple<?, ?, ?> t = (Triple<?, ?, ?>) o;
		return (x == t.x || (x != null && x.equals(t.x))) && (y == t.y || (y != null && y.equals(t.y)));
	}
}