package toberumono.structures.collections.maps;

import java.util.Comparator;
import java.util.TreeMap;

import toberumono.structures.collections.lists.SortedList;

/**
 * This is effectively a TreeMap for keys that map to multiple values. This does prevent duplicate values within each key's
 * list.
 * 
 * @author Joshua Lipstone
 * @param <T>
 *            the key type
 * @param <V>
 *            the value type
 */
public class MappedList<T, V extends Comparable<V>> {
	private TreeMap<T, SortedList<V>> map;
	private Comparator<V> valueComparator;
	
	/**
	 * Constructs a new MappedList that sorts the keys with the specified keyComparator and the values with the specified
	 * value comparator.
	 * 
	 * @param keyComparator
	 *            the comparator to sort the keys with
	 * @param valueComparator
	 *            the comparator to sort the values with
	 * @see toberumono.structures.SortingMethods SortingMethods
	 */
	public MappedList(Comparator<T> keyComparator, Comparator<V> valueComparator) {
		this.valueComparator = valueComparator;
		this.map = new TreeMap<>(keyComparator);
	}
	
	/**
	 * Constructs a new MappedList that sorts the keys with the specified keyComparator and the values in the order they were
	 * inserted.
	 * 
	 * @param keyComparator
	 *            the comparator to sort the keys with
	 * @see toberumono.structures.SortingMethods SortingMethods
	 */
	public MappedList(Comparator<T> keyComparator) {
		this(keyComparator, null);
	}
	
	/**
	 * @param key
	 *            the key to retrieve the values of
	 * @return the values associated with the specified key
	 */
	public SortedList<V> get(T key) {
		return map.get(key);
	}
	
	/**
	 * Adds the specified value to the specified key's list if it is not already in that list.
	 * 
	 * @param key
	 *            the key to associate the value with
	 * @param value
	 *            the value to be associated
	 */
	public void put(T key, V value) {
		if (!map.containsKey(key))
			map.put(key, new SortedList<>(valueComparator));
		SortedList<V> temp = map.get(key);
		if (!temp.contains(value)) {
			temp.add(value);
			map.put(key, temp);
		}
	}
	
	/**
	 * Removes the specified key from the map.
	 * 
	 * @param key
	 *            the key to remove
	 * @return the values associated with the specified keys
	 */
	public SortedList<V> remove(T key) {
		return map.remove(key);
	}
	
	/**
	 * Removes the specified value from the specified key's associated values list.
	 * 
	 * @param key
	 *            the key to dissociate the value from
	 * @param value
	 *            the value to be dissociated
	 */
	public void remove(T key, V value) {
		SortedList<V> temp = map.get(key);
		temp.remove(value);
		map.put(key, temp);
	}
}
