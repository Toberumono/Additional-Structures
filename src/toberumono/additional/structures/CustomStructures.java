package toberumono.additional.structures;

import toberumono.additional.structures.tuples.ImmutablePair;
import toberumono.additional.structures.tuples.Pair;

public class CustomStructures {
	
	private CustomStructures() {/* This is a static class. */}
	
	public static <T, U> Pair<T, U> immutablePair(Pair<T, U> pair) {
		return new ImmutablePair<>(pair);
	}
}
