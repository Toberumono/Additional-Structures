package toberumono.structures;

import toberumono.structures.tuples.ImmutablePair;
import toberumono.structures.tuples.Pair;

/**
 * A simple static class that holds convenience methods for the library.
 * 
 * @author Toberumono
 */
public class Structures {
	
	private Structures() {/* This is a static class. */}
	
	/**
	 * Wraps the given {@link Pair} in an {@link ImmutablePair}.
	 * 
	 * @param pair
	 *            the {@link Pair} to wrap
	 * @param <T>
	 *            the type of the x value of the {@link Pair}
	 * @param <U>
	 *            the type of the y value of the {@link Pair}
	 * @return an {@link ImmutablePair} that wraps the given {@link Pair}
	 */
	public static <T, U> Pair<T, U> immutablePair(Pair<T, U> pair) {
		return new ImmutablePair<>(pair);
	}
}
