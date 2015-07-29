package toberumono.structures.interfaces;

/**
 * A simple functional interface for currying functions such that they get one additional argument of type P
 * 
 * @author Toberumono
 * @param <P>
 *            the argument type
 * @param <R>
 *            the return type (often a functional interface)
 */
@FunctionalInterface
public interface SingleArgumentCurrier<P, R> {
	
	/**
	 * Curries the result with the given argument
	 * 
	 * @param argument
	 *            the desired argument
	 * @return the desired result
	 */
	public R curry(P argument);
}
