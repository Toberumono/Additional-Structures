package lipstone.joshua.customStructures.lists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * A pair of ArrayLists that are kept in sync. This does not allow for multiple values per key.
 * 
 * @author Joshua Lipstone
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public class SortedPairedList<K extends Comparable<K>, V> implements Iterable<K> {
	private SortedList<K> keys;
	private ArrayList<V> values;
	
	/**
	 * Constructs a new <tt>SortedPairedList</tt>
	 * 
	 * @param keySorter
	 *            the {@link java.util.Comparator Comparator} used to sort the keys.
	 */
	public SortedPairedList(Comparator<K> keySorter) {
		keys = new SortedList<>(keySorter);
		values = new ArrayList<>();
	}
	
	/**
	 * Constructs a new <tt>SortedPairedList</tt> from the values in the given <tt>SortedPairedList</tt>
	 * 
	 * @param list
	 *            the <tt>SortedPairedList</tt> containing the values that this list should start with
	 */
	public SortedPairedList(SortedPairedList<K, V> list) {
		keys = new SortedList<>(list.keys);
		values = new ArrayList<>(list.values);
	}
	
	/**
	 * @return the list of keys in this <tt>SortedPairedList</tt>
	 */
	public SortedList<K> getKeys() {
		return keys;
	}
	
	/**
	 * @return the list of values in this <tt>SortedPairedList</tt>
	 */
	public ArrayList<V> getValues() {
		return values;
	}
	
	/**
	 * Adds the given key to the end of this <tt>SortedPairedList</tt> with the given value
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void add(K key, V value) {
		keys.add(key);
		values.add(value);
	}
	
	/**
	 * Adds the given key to this <tt>SortedPairedList</tt> at the given index with the given value, and shifts all elements
	 * with indecies {@literal >=} i to the right
	 * 
	 * @param i
	 *            the index
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void add(int i, K key, V value) {
		keys.add(i, key);
		values.add(i, value);
	}
	
	/**
	 * @param key
	 *            the key to get the value of
	 * @return the values associated with the key if the key is in the <tt>SortedPairedList</tt>, otherwise empty list
	 */
	public V get(K key) {
		int index = keys.indexOf(key);
		return index == -1 ? null : values.get(index);
	}
	
	/**
	 * Removes the ith key and its associated value from this <tt>SortedPairedList</tt>
	 * 
	 * @param i
	 *            the index of the key-value pair to remove
	 * @return the removed value
	 */
	public V remove(int i) {
		keys.remove(i);
		return values.remove(i);
	}
	
	/**
	 * Removes the first value mapped to the given keys
	 * 
	 * @param key
	 *            the key to remove
	 * @return the value mapped to the key or null if the key was not in the list
	 */
	public V remove(K key) {
		int index = keys.indexOf(key);
		keys.remove(index);
		return values.remove(index);
	}
	
	/**
	 * @return the size of this list, which is equal to the number of keys within the list.
	 */
	public int size() {
		return keys.size();
	}
	
	public boolean containsKey(K key) {
		return keys.indexOf(key) > -1;
	}
	
	@Override
	public String toString() {
		String output = "";
		for (int i = 0; i < keys.size(); i++)
			output = output + "<" + keys.get(i).toString() + ", " + values.get(i).toString() + ">, ";
		if (output.endsWith(", "))
			output = output.substring(0, output.length() - 2);
		return output;
	}
	
	@Override
	public Iterator<K> iterator() {
		return keys.iterator();
	}
}
