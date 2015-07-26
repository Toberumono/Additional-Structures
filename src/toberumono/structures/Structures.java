package toberumono.structures;

import toberumono.structures.tuples.ImmutablePair;
import toberumono.structures.tuples.Pair;

public class Structures {
	
	private Structures() {/* This is a static class. */}
	
	public static <T, U> Pair<T, U> immutablePair(Pair<T, U> pair) {
		return new ImmutablePair<>(pair);
	}
}
