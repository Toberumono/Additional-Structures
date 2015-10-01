package toberumono.structures.collections.maps.restriction;

import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface RestrictionMap<K, V> extends Map<K, V> {
	/**
	 * A mode for the {@link RestrictionMap} wherein the individual restrictions for each key are combined using and.
	 */
	public static final BiPredicate<Boolean, Boolean> AND_MODE = (b, r) -> b && r;
	/**
	 * A mode for the {@link RestrictionMap} wherein the individual restrictions for each key are combined using or.<br>
	 * This is the default mode and allows for several convenient optimizations.
	 */
	public static final BiPredicate<Boolean, Boolean> OR_MODE = (b, r) -> b || r;
	/**
	 * A mode for the {@link RestrictionMap} wherein the individual restrictions for each key are combined using xor.
	 */
	public static final BiPredicate<Boolean, Boolean> XOR_MODE = (b, r) -> b ^ r;
	
	public static final String FOLLOWED_BY = ">";
	
	public static final String NOT_FOLLOWED_BY = "!>";
	
	public static final String PRECEDED_BY = "<";
	
	public static final String NOT_PRECEDED_BY = "<!";
	
	/**
	 * This method creates a "copy" of this {@link LinkedRestrictionMap} whose fields are pointers to the fields in this
	 * {@link LinkedRestrictionMap}. Because all of the fields except for the current subset of available key-value pairs are
	 * final, this means that the update time for M maps is roughly O(update time for one map) instead of O(M * update time
	 * for one map).
	 * 
	 * @return a linked copy of this {@link LinkedRestrictionMap}
	 */
	public RestrictionMap<K, V> createLinkedCopy();
	
	/**
	 * Resets the {@link #get(Object)} function to access the values that are valid for an initial access.<br>
	 * Equivalent to: {@code restrict(null);}
	 * 
	 * @see #restrict(Object)
	 */
	public void reset();
	
	public void restrict(K key);
	
	/**
	 * Sets the {@link #get(Object)} function to access all of the values in the map without restriction.
	 * 
	 * @see #restrict(Object)
	 */
	public void unrestrict();
	
	/**
	 * Adds a restriction to this {@link RestrictionMap}.
	 * 
	 * @param identifier
	 *            the key by which to identify the restriction for later operations such as removal
	 * @param applies
	 *            a {@link Predicate} that takes a key in the {@link RestrictionMap} as an argument and returns {@code true}
	 *            iff this restriction should be applied to that key
	 * @param type
	 *            the type of the restriction, either "&gt;", "!&gt;", "&lt;", or "&lt;!" for followed by, not followed by,
	 *            preceded by, and not preceded by respectively
	 * @param satisfies
	 *            a {@link Predicate} that takes a key in the {@link RestrictionMap} as an argument and returns {@code true}
	 *            if the key satisfies the restriction's condition (e.g. if it is a "not followed by" restriction, then the a
	 *            return of {@code true} would indicate that the key should not follow the an keys that cause the first
	 *            {@link Predicate} to return {@code true})
	 */
	public void addRestriction(String identifier, Predicate<K> applies, String type, Predicate<K> satisfies);
	
	/**
	 * Removes a restriction from this {@link RestrictionMap}.
	 * 
	 * @param identifier
	 *            the key by which the restriction to be removed is identified
	 */
	public void removeRestriction(String identifier);
}