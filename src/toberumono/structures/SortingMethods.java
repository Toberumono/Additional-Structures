package toberumono.structures;

import java.util.Comparator;

/**
 * Some pre-configured {@link java.util.Comparator Comparators} for common sorting methods
 * 
 * @author Joshua Lipstone
 */
public class SortingMethods {
	
	/**
	 * A Comparator that sorts {@link java.lang.String Strings} in ascending alphabetical order.
	 */
	public static final Comparator<String> STRING_ALPHABETICAL_ASCENDING = (o1, o2) -> {
		return o1.compareTo(o2);
	};
	/**
	 * A Comparator that sorts {@link java.lang.String Strings} in descending alphabetical order.
	 */
	public static final Comparator<String> STRING_ALPHABETICAL_DESCENDING = (o2, o1) -> {
		return o1.compareTo(o2);
	};
	
	/**
	 * A Comparator that sorts {@link java.lang.String Strings} in ascending length order, with ascending lexographical order
	 * as a secondary sorting mechanism for strings of the same length.
	 */
	public static final Comparator<String> STRING_LENGTH_ASCENDING = (o1, o2) -> {
		return o1.length() > o2.length() ? 1 : (o1.length() == o2.length() ? o1.compareTo(o2) : -1);
	};
	/**
	 * A Comparator that sorts {@link java.lang.String Strings} in descending length order, with descending lexographical
	 * order as a secondary sorting mechanism for strings of the same length.
	 */
	public static final Comparator<String> STRING_LENGTH_DESCENDING = (o1, o2) -> {
		return o2.length() > o1.length() ? 1 : (o1.length() == o2.length() ? o2.compareTo(o1) : -1);
	};
	
	/**
	 * A Comparator that sorts numbers in ascending order, taking into account that the long to double conversion can lose
	 * precision.
	 */
	public static final Comparator<Number> NUMERIC_ASCENDING = (o1, o2) -> {
		if (o1 instanceof Long && o2 instanceof Long)
			return ((Long) o1).compareTo((Long) o2);
		return o1.doubleValue() > o2.doubleValue() ? 1 : (o1.doubleValue() == o2.doubleValue() ? 0 : -1);
	};
	/**
	 * A Comparator that sorts numbers in descending order, taking into account that the long to double conversion can lose
	 * precision.
	 */
	public static final Comparator<Number> NUMERIC_DESCENDING = (o1, o2) -> {
		if (o1 instanceof Long && o2 instanceof Long)
			return ((Long) o2).compareTo((Long) o1);
		return o2.doubleValue() > o1.doubleValue() ? 1 : (o1.doubleValue() == o2.doubleValue() ? 0 : -1);
	};
}
