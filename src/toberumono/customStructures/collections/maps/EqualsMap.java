package toberumono.customStructures.collections.maps;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Functions similarly to a <tt>HashMap</tt>, but uses equality to map keys to values instead of hash codes.<br>
 * Guarantees that the order of the map will be the same as the order that items were put in the map.<br>
 * Most of the code is a clone of the code found in {@link java.util.HashMap}, modified such that it uses equality instead of
 * hash codes.
 * 
 * @author Joshua Lipstone
 * @param <K>
 *            The key type for this map
 * @param <V>
 *            The value type for this map
 */
public class EqualsMap<K, V> implements Map<K, V> {
	
	/**
	 * The default initial capacity.
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 16;
	
	/* ---------------- Fields -------------- */
	
	/**
	 * The table, initialized on first use, and resized as necessary. When allocated, length is always a power of two. (We
	 * also tolerate length zero in some operations to allow bootstrapping mechanics that are currently not needed.)
	 */
	transient final ArrayList<Node<K, V>> table;
	
	/**
	 * Holds cached entrySet(). Note that AbstractMap fields are used for keySet() and values().
	 */
	transient Set<Map.Entry<K, V>> entrySet;
	
	/**
	 * The number of times this HashMap has been structurally modified Structural modifications are those that change the
	 * number of mappings in the HashMap or otherwise modify its internal structure (e.g., rehash). This field is used to
	 * make iterators on Collection-views of the HashMap fail-fast. (See ConcurrentModificationException).
	 */
	transient int modCount;
	
	/**
	 * Each of these fields are initialized to contain an instance of the appropriate view the first time this view is
	 * requested. The views are stateless, so there's no reason to create more than one of each.
	 */
	transient volatile Set<K> keySet;
	transient volatile Collection<V> values;
	
	static class Node<K, V> implements Map.Entry<K, V> {
		final K key;
		V value;
		
		Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public final K getKey() {
			return key;
		}
		
		@Override
		public final V getValue() {
			return value;
		}
		
		@Override
		public final String toString() {
			return key + "=" + value;
		}
		
		@Override
		public final int hashCode() {
			return Objects.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}
		
		@Override
		public final boolean equals(Object o) {
			if (o == this)
				return true;
			if (o instanceof Map.Entry) {
				Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
				if (Objects.equals(key, e.getKey()) &&
						Objects.equals(value, e.getValue()))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * Constructs a new {@link EqualsMap}
	 */
	public EqualsMap() {
		table = new ArrayList<>(DEFAULT_INITIAL_CAPACITY);
	}
	
	/**
	 * Constructs a new {@link EqualsMap} with the same mappings as the specified {@link java.util.Map Map}. The
	 * {@link EqualsMap} is created with default load factor (0.75) and an initial capacity sufficient to hold the mappings
	 * in the specified {@link java.util.Map Map}.
	 *
	 * @param m
	 *            the map whose mappings are to be placed in this map
	 * @throws NullPointerException
	 *             if the specified map is null
	 */
	public EqualsMap(Map<? extends K, ? extends V> m) {
		this();
		putMapEntries(m);
	}
	
	final int indexOfKey(Object o) {
		for (int i = 0; i < size(); i++)
			if (table.get(i).key.equals(o))
				return i;
		return -1;
	}
	
	/**
	 * Implements Map.putAll and Map constructor
	 *
	 * @param m
	 *            the map
	 */
	final void putMapEntries(Map<? extends K, ? extends V> m) {
		for (K key : m.keySet())
			putVal(key, m.get(key), false);
	}
	
	/**
	 * Implements Map.put and related methods
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value to put
	 * @param onlyIfAbsent
	 *            if true, don't change existing value
	 * @return previous value, or null if none
	 */
	final V putVal(K key, V value, boolean onlyIfAbsent) {
		int putIndex = indexOfKey(key);
		if (putIndex != -1) {
			Node<K, V> e = table.get(putIndex);
			V oldValue = e.value;
			if (!onlyIfAbsent || oldValue == null)
				e.value = value;
			return oldValue;
		}
		else
			table.add(new Node<>(key, value));
		++modCount;
		return null;
	}
	
	/**
	 * If key is already in the list, it overwrites the value associated with key. Otherwise, it adds it to the list.
	 * 
	 * @param key
	 *            the key to set the value of
	 * @param value
	 *            the value to set the key to
	 * @return the replaced value
	 */
	@Override
	public V put(K key, V value) {
		return putVal(key, value, false);
	}
	
	/**
	 * @param key
	 *            the key to get the value of
	 * @return the value associated with the key if the key is in the map, otherwise null
	 */
	@Override
	public V get(Object key) {
		int getIndex = indexOfKey(key);
		return getIndex == -1 ? null : table.get(getIndex).getValue();
	}
	
	/**
	 * @param key
	 *            the key to remove
	 * @return the value mapped to the key or null if the key was not in the list
	 */
	@Override
	public V remove(Object key) {
		int removeIndex = indexOfKey(key);
		if (removeIndex == -1)
			return null;
		++modCount;
		return table.remove(removeIndex).getValue();
	}
	
	@Override
	public boolean containsKey(Object key) {
		return indexOfKey(key) >= 0;
	}
	
	@Override
	public int size() {
		return table.size();
	}
	
	@Override
	public String toString() {
		if (table.size() == 0)
			return "{}";
		StringBuilder output = new StringBuilder();
		output.append('{');
		for (Node<K, V> node : table)
			output.append('<').append(node.getKey().toString()).append(", ").append(node.getValue().toString()).append(">, ");
		return output.delete(output.length() - 2, output.length()).append('}').toString();
	}
	
	@Override
	public boolean isEmpty() {
		return table.isEmpty();
	}
	
	@Override
	public boolean containsValue(Object value) {
		for (Node<K, V> node : table)
			if (node.getValue().equals(value))
				return true;
		return false;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (java.util.Map.Entry<? extends K, ? extends V> entry : m.entrySet())
			put(entry.getKey(), entry.getValue());
	}
	
	@Override
	public void clear() {
		table.clear();
	}
	
	@Override
	public Set<K> keySet() {
		Set<K> ks;
		return (ks = keySet) == null ? (keySet = new KeySet()) : ks;
	}
	
	final class KeyIterator implements Iterator<K> {
		private Iterator<Node<K, V>> tableIterator = table.iterator();
		
		@Override
		public boolean hasNext() {
			return tableIterator.hasNext();
		}
		
		@Override
		public K next() {
			return tableIterator.next().getKey();
		}
		
	}
	
	final class KeySet extends AbstractSet<K> {
		@Override
		public final int size() {
			return EqualsMap.this.size();
		}
		
		@Override
		public final void clear() {
			EqualsMap.this.clear();
		}
		
		@Override
		public final Iterator<K> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public final boolean contains(Object o) {
			return containsKey(o);
		}
		
		@Override
		public final boolean remove(Object key) {
			return EqualsMap.this.remove(key) != null;
		}
		
		@Override
		public final void forEach(Consumer<? super K> action) {
			if (action == null)
				throw new NullPointerException();
			if (table.size() > 0) {
				int mc = modCount;
				for (int i = 0; i < table.size(); ++i)
					action.accept(table.get(i).key);
				if (modCount != mc)
					throw new ConcurrentModificationException();
			}
		}
	}
	
	@Override
	public Collection<V> values() {
		Collection<V> vs;
		return (vs = values) == null ? (values = new Values()) : vs;
	}
	
	final class ValueIterator implements Iterator<V> {
		private Iterator<Node<K, V>> tableIterator = table.iterator();
		
		@Override
		public boolean hasNext() {
			return tableIterator.hasNext();
		}
		
		@Override
		public V next() {
			return tableIterator.next().getValue();
		}
		
	}
	
	final class Values extends AbstractCollection<V> {
		@Override
		public final int size() {
			return table.size();
		}
		
		@Override
		public final void clear() {
			EqualsMap.this.clear();
		}
		
		@Override
		public final Iterator<V> iterator() {
			return new ValueIterator();
		}
		
		@Override
		public final boolean contains(Object o) {
			return containsValue(o);
		}
		
		@Override
		public final void forEach(Consumer<? super V> action) {
			if (action == null)
				throw new NullPointerException();
			if (table.size() > 0) {
				int mc = modCount;
				for (int i = 0; i < table.size(); ++i)
					action.accept(table.get(i).value);
				if (modCount != mc)
					throw new ConcurrentModificationException();
			}
		}
	}
	
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> es;
		return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
	}
	
	final class EntryIterator implements Iterator<Map.Entry<K, V>> {
		private Iterator<Node<K, V>> tableIterator = table.iterator();
		
		@Override
		public boolean hasNext() {
			return tableIterator.hasNext();
		}
		
		@Override
		public Map.Entry<K, V> next() {
			return tableIterator.next();
		}
		
	}
	
	final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
		@Override
		public final int size() {
			return table.size();
		}
		
		@Override
		public final void clear() {
			EqualsMap.this.clear();
		}
		
		@Override
		public final Iterator<Map.Entry<K, V>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public final boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
			Object key = e.getKey();
			return indexOfKey(key) != -1;
		}
		
		@Override
		public final boolean remove(Object o) {
			if (o instanceof Map.Entry) {
				Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
				Object key = e.getKey();
				return EqualsMap.this.remove(key) != null;
			}
			return false;
		}
		
		@Override
		public final void forEach(Consumer<? super Map.Entry<K, V>> action) {
			if (action == null)
				throw new NullPointerException();
			if (table.size() > 0) {
				int mc = modCount;
				for (int i = 0; i < table.size(); ++i)
					action.accept(table.get(i));
				if (modCount != mc)
					throw new ConcurrentModificationException();
			}
		}
	}
}
