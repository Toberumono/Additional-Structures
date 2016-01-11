package toberumono.structures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import toberumono.structures.collections.lists.SortedList;
import toberumono.structures.versioning.VersionNumber;

/**
 * A static class containing some common {@link Comparator Comparators} for sorting methods.
 * 
 * @author Toberumono
 * @see SortedList
 */
public class SortingMethods {
	
	/**
	 * A {@link Comparator} that sorts {@link String Strings} in ascending alphabetical order.
	 * 
	 * @see #STRING_ALPHABETICAL_DESCENDING
	 */
	public static final Comparator<String> STRING_ALPHABETICAL_ASCENDING = (o1, o2) -> o1.compareTo(o2);
	
	/**
	 * A {@link Comparator} that sorts {@link String Strings} in descending alphabetical order.
	 * 
	 * @see #STRING_ALPHABETICAL_ASCENDING
	 */
	public static final Comparator<String> STRING_ALPHABETICAL_DESCENDING = (o1, o2) -> o2.compareTo(o1); //o2.compareTo(o1) is the reverse of o1.compareTo(o2), thereby making this descending.
	
	/**
	 * A {@link Comparator} that sorts {@link String Strings} in ascending length order, with ascending lexographical order
	 * as a secondary sorting mechanism for strings of the same length.
	 * 
	 * @see #STRING_LENGTH_DESCENDING
	 */
	public static final Comparator<String> STRING_LENGTH_ASCENDING = (o1, o2) -> o1.length() < o2.length() ? -1 : (o1.length() == o2.length() ? o1.compareTo(o2) : 1);
	
	/**
	 * A {@link Comparator} that sorts {@link String Strings} in descending length order, with descending lexographical order
	 * as a secondary sorting mechanism for strings of the same length.
	 * 
	 * @see #STRING_LENGTH_ASCENDING
	 */
	public static final Comparator<String> STRING_LENGTH_DESCENDING = (o1, o2) -> o2.length() < o1.length() ? -1 : (o2.length() == o1.length() ? o2.compareTo(o1) : 1);
	
	/**
	 * A {@link Comparator} that sorts numbers in ascending order, taking into account that the {@link Long} to
	 * {@link Double} conversion can lose precision.
	 * 
	 * @see #NUMERIC_DESCENDING
	 */
	public static final Comparator<Number> NUMERIC_ASCENDING = (o1, o2) -> {
		if (o1 instanceof Long && o2 instanceof Long)
			return ((Long) o1).compareTo((Long) o2);
		return o1.doubleValue() > o2.doubleValue() ? 1 : (o1.doubleValue() == o2.doubleValue() ? 0 : -1);
	};
	
	/**
	 * A {@link Comparator} that sorts numbers in descending order, taking into account that the {@link Long} to
	 * {@link Double} conversion can lose precision.
	 * 
	 * @see #NUMERIC_ASCENDING
	 */
	public static final Comparator<Number> NUMERIC_DESCENDING = (o1, o2) -> {
		if (o1 instanceof Long && o2 instanceof Long)
			return ((Long) o2).compareTo((Long) o1);
		return o2.doubleValue() > o1.doubleValue() ? 1 : (o1.doubleValue() == o2.doubleValue() ? 0 : -1);
	};
	
	/**
	 * A {@link Comparator} that sorts {@link Path Paths} by date modified in ascending order.<br>
	 * In the event of an {@link IOException} from {@link Files#getLastModifiedTime(Path, java.nio.file.LinkOption...)}, the
	 * {@link Comparator} returns {@code 0}.
	 * 
	 * @see #PATH_MODIFIED_TIME_DESCENDING
	 */
	public static final Comparator<Path> PATH_MODIFIED_TIME_ASCENDING = (o1, o2) -> {
		try {
			return Files.getLastModifiedTime(o1).compareTo(Files.getLastModifiedTime(o2)); //Sort the Paths by last modified.  The first element will always be the oldest this way.
		}
		catch (IOException e) {
			return 0;
		}
	};
	
	/**
	 * A {@link Comparator} that sorts {@link Path Paths} by date modified in ascending order.<br>
	 * In the event of an {@link IOException} from {@link Files#getLastModifiedTime(Path, java.nio.file.LinkOption...)}, the
	 * {@link Comparator} returns {@code 0}.
	 * 
	 * @see #PATH_MODIFIED_TIME_ASCENDING
	 */
	public static final Comparator<Path> PATH_MODIFIED_TIME_DESCENDING = (o1, o2) -> {
		try {
			return Files.getLastModifiedTime(o2).compareTo(Files.getLastModifiedTime(o1)); //Sort the Paths by last modified.  The last element will always be the oldest this way.
		}
		catch (IOException e) {
			return 0;
		}
	};
	
	/**
	 * A {@link Comparator} that sorts {@link VersionNumber VersionNumbers} in ascending order.
	 * 
	 * @see #VERSIONNUMBER_DESCENDING
	 */
	public static final Comparator<VersionNumber> VERSIONNUMBER_ASCENDING = (o1, o2) -> o1.compareTo(o2);
	
	/**
	 * A {@link Comparator} that sorts {@link VersionNumber VersionNumbers} in descending order.
	 * 
	 * @see #VERSIONNUMBER_ASCENDING
	 */
	public static final Comparator<VersionNumber> VERSIONNUMBER_DESCENDING = (o1, o2) -> o2.compareTo(o1); //o2.compareTo(o1) is the reverse of o1.compareTo(o2), thereby making this descending.
	
	private SortingMethods() { /*This is a static class*/ }
}
