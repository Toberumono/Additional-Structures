package lipstone.joshua.customStructures.tuples;

public class Pair<X, Y> {
	private X x;
	private Y y;
	
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public X getX() {
		return x;
	}
	
	public Y getY() {
		return y;
	}
	
	public void setX(X newX) {
		x = newX;
	}
	
	public void setY(Y newY) {
		y = newY;
	}
	
	@Override
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
}