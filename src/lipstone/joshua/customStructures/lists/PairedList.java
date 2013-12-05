package lipstone.joshua.customStructures.lists;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A set of ArrayLists that are kept in synch. This does allow for multiple values per key and duplicate values, even under
 * the same key.
 * 
 * @author Joshua Lipstone
 * @param <T>
 * @param <V>
 */
public class PairedList<T, V> implements Iterable<T>, Comparable<PairedList<T, V>> {
	private ArrayList<T> keys;
	private ArrayList<V> values;
	
	/**
	 * Constructs a new <tt>PairedList</tt>
	 */
	public PairedList() {
		keys = new ArrayList<T>();
		values = new ArrayList<V>();
	}
	
	/**
	 * Constructs a new <tt>PairedList</tt> from the values in the given <tt>PairedList</tt>
	 * 
	 * @param list
	 *            the <tt>PairedList</tt> containing the values that this list should start with
	 */
	public PairedList(PairedList<T, V> list) {
		this();
		for (T key : list.keys)
			keys.add(key);
		for (V value : list.values)
			values.add(value);
	}
	
	/**
	 * @return the list of keys in this <tt>PairedList</tt>
	 */
	public ArrayList<T> getKeys() {
		return keys;
	}
	
	/**
	 * @return the list of values in this <tt>PairedList</tt>
	 */
	public ArrayList<V> getValues() {
		return values;
	}
	
	/**
	 * Adds the given key to the end of this <tt>PairedList</tt> with the given value
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void add(T key, V value) {
		keys.add(key);
		values.add(value);
	}
	
	/**
	 * Adds the given key to this <tt>PairedList</tt> at the given index with the given value, and shifts all elements with
	 * indecies >= i to the right
	 * 
	 * @param i
	 *            the index
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void add(int i, T key, V value) {
		keys.add(i, key);
		values.add(i, value);
	}
	
	/**
	 * @param key
	 *            the key to get the value of
	 * @return the values associated with the key if the key is in the <tt>PairedList</tt>, otherwise empty list
	 */
	public ArrayList<V> get(T key) {
		ArrayList<V> output = new ArrayList<>();
		for (int i = 0; i < keys.size(); i++)
			if (keys.get(i).equals(key))
				output.add(values.get(i));
		return output;
	}
	
	/**
	 * @param value
	 *            the value
	 * @return all the keys in this <tt>PairedList</tt> that are paired with the given value
	 */
	public ArrayList<T> getByValue(V value) {
		ArrayList<T> output = new ArrayList<>();
		for (int i = 0; i < values.size(); i++)
			if (values.get(i).equals(value))
				output.add(keys.get(i));
		return output;
	}
	
	/**
	 * Removes the ith key and its associated value from this <tt>PairedList</tt>
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
	public V remove(T key) {
		int index = keys.indexOf(key);
		keys.remove(index);
		return values.remove(index);
	}
	
	/**
	 * Removes all the values pointed to by the given key from this <tt>PairedList</tt>
	 * 
	 * @param key
	 *            the key
	 * @return the removed values as an ArrayList, otherwise an empty list
	 */
	public ArrayList<V> removeAll(T key) {
		ArrayList<V> output = new ArrayList<V>();
		for (int i = 0; i < keys.size(); i++)
			if (keys.get(i).equals(key)) {
				keys.remove(i);
				output.add(values.remove(i--));
			}
		return output;
	}
	
	/**
	 * @return the size of this list, which is equal to the number of keys within the list.
	 */
	public int size() {
		return keys.size();
	}
	
	public boolean containsKey(T key) {
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
	public Iterator<T> iterator() {
		return keys.iterator();
	}
	
	@Override
	public int compareTo(PairedList<T, V> o) {
		if (keys.size() > o.keys.size())
			return 1;
		if (keys.size() < o.keys.size())
			return -1;
		if (values.get(0) instanceof Comparable) {
			int result = 0;
			for (T key : keys) {
				ArrayList<V> vals = get(key), oVals = o.get(key);
				if (vals.size() > oVals.size())
					result += 1;
				else if (vals.size() < oVals.size())
					result -= 1;
				else
					for (int i = 0; i < vals.size(); i++)
						result += ((Comparable<V>) vals.get(i)).compareTo(oVals.get(i));
			}
			return result;
		}
		return 0;
	}
}
