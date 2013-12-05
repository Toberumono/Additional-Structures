package lipstone.joshua.customStructures.maps;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Functions similarly to a <tt>HashMap</tt>, but uses equality to map keys to values instead of hash codes.</br> Guarantees
 * that the order of the map will be the same as the order that items were put in the map.
 * 
 * @author Joshua Lipstone
 * @param <T>
 *            The key type for this map
 * @param <V>
 *            The value type for this map
 */
public class EqualsMap<T, V> implements Iterable<T>, Comparable<EqualsMap<T, V>> {
	private KeySet keys;
	private ValueSet values;
	
	/**
	 * This represents a set or keys or values that is backed by the map
	 * 
	 * @author Joshua Lipstone
	 * @param <K>
	 *            the type for this set
	 */
	private abstract class MapSet<K> extends AbstractSet<K> {
		ArrayList<K> backend = new ArrayList<K>();
		
		/**
		 * This is used to avoid infinite recursion
		 */
		void clear0() {
			backend.clear();
		}
		
		int indexOf(Object o) {
			return backend.indexOf(o);
		}
		
		K remove(int index) {
			return backend.remove(index);
		}
		
		K get(int index) {
			return backend.get(index);
		}
		
		K set(int index, K value) {
			return backend.set(index, value);
		}
		
		boolean add0(K value) {
			return backend.add(value);
		}
		
		@Override
		public Iterator<K> iterator() {
			return new Iterator<K>() {
				private Iterator<K> i = backend.iterator();
				
				@Override
				public boolean hasNext() {
					return i.hasNext();
				}
				
				@Override
				public K next() {
					return i.next();
				}
				
				@Override
				public void remove() {
					i.remove();
				}
			};
		}
		
		@Override
		public int size() {
			return backend.size();
		}
		
		@Override
		public abstract void clear();
	}
	
	private final class KeySet extends MapSet<T> {
		
		@Override
		public void clear() {
			values.clear0();
			backend.clear();
		}
	}
	
	private final class ValueSet extends MapSet<V> {
		
		@Override
		public void clear() {
			keys.clear0();
			backend.clear();
		}
	}
	
	/**
	 * Constructs a new <tt>EqualsMap</tt>
	 */
	public EqualsMap() {
		keys = new KeySet();
		values = new ValueSet();
	}
	
	/**
	 * If key is already in the list, it overwrites the value associated with key. Otherwise, it adds it to the list.
	 * 
	 * @param key
	 *            the key to set the value of
	 * @param value
	 *            the value to set the key to
	 */
	public V put(T key, V value) {
		int index = keys.indexOf(key);
		if (index < 0) {
			keys.add0(key);
			values.add0(value);
			return value;
		}
		keys.set(index, key);
		return values.set(index, value);
	}
	
	/**
	 * @param key
	 *            the key to get the value of
	 * @return the value associated with the key if the key is in the map, otherwise null
	 */
	public V get(T key) {
		int index = keys.indexOf(key);
		if (index < 0)
			return null;
		return values.get(index);
	}
	
	/**
	 * @param key
	 *            the key to remove
	 * @return the value mapped to the key or null if the key was not in the list
	 */
	public V remove(T key) {
		int index = keys.indexOf(key);
		if (index < 0)
			return null;
		keys.remove(index);
		return values.remove(index);
	}
	
	public boolean containsKey(T key) {
		return keys.indexOf(key) >= 0;
	}
	
	/**
	 * @return the size of this list, which is equal to the number of keys within the list.
	 */
	public int size() {
		return keys.size();
	}
	
	public Set<T> getKeys() {
		return keys;
	}
	
	public Set<V> getValues() {
		return values;
	}
	
	@Override
	public String toString() {
		String output = "";
		for (T key : keys)
			output = output + "<" + key.toString() + ", " + get(key).toString() + ">, ";
		if (output.endsWith(", "))
			output = output.substring(0, output.length() - 2);
		return output;
	}
	
	@Override
	public Iterator<T> iterator() {
		return keys.iterator();
	}
	
	@Override
	public int compareTo(EqualsMap<T, V> o) {
		if (keys.size() > o.keys.size())
			return 1;
		if (keys.size() < o.keys.size())
			return -1;
		if (values.get(0) instanceof Comparable) {
			int result = 0;
			for (T key : keys)
				result += ((Comparable<V>) get(key)).compareTo(o.get(key));
			return result;
		}
		return 0;
	}
}
