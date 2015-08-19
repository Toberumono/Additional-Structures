package toberumono.structures;

import toberumono.structures.tuples.ImmutablePair;
import toberumono.structures.tuples.ImmutableTriple;
import toberumono.structures.tuples.Pair;
import toberumono.structures.tuples.Triple;

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
	
	/**
	 * Wraps the given {@link Triple} in an {@link ImmutableTriple}.
	 * 
	 * @param triple
	 *            the {@link Triple} to wrap
	 * @param <T>
	 *            the type of the x value of the {@link Triple}
	 * @param <U>
	 *            the type of the y value of the {@link Triple}
	 * @param <V>
	 *            the type of the z value of the {@link Triple}
	 * @return an {@link ImmutableTriple} that wraps the given {@link Triple}
	 */
	public static <T, U, V> Triple<T, U, V> immutableTriple(Triple<T, U, V> triple) {
		return new ImmutableTriple<>(triple);
	}
}
