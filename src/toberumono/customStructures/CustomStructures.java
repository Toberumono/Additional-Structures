package toberumono.customStructures;

import toberumono.customStructures.tuples.ImmutablePair;
import toberumono.customStructures.tuples.Pair;

public class CustomStructures {
	
	private CustomStructures() {/* This is a static class. */}
	
	public static <T, U> Pair<T, U> immutablePair(Pair<T, U> pair) {
		return new ImmutablePair<>(pair);
	}
}
