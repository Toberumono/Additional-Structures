package toberumono.customStructures.collections.maps;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This is a wrapper for {@link java.util.HashMap}. While linkable isn't quite the right word, this is effectively a HashMap
 * that can create submaps in the way that ArrayLists can create sublists.
 * 
 * @author Joshua Lipstone
 * @param <K>
 *            key type
 * @param <V>
 *            value type
 */
public class LinkableHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
	private final HashMap<K, Wrapper> core;
	
	public LinkableHashMap() {
		core = new HashMap<>();
	}
	
	/**
	 * Creates a new {@link LinkableHashMap} linked to the given map. If, among the maps, there is more than one mapping for
	 * a key, the first mapping encountered is linked.
	 * 
	 * @param map
	 *            the first map to link
	 * @param maps
	 *            the other maps to link
	 */
	public LinkableHashMap(LinkableHashMap<K, V> map, @SuppressWarnings("unchecked") LinkableHashMap<K, V>... maps) {
		core = new HashMap<>(map.core);
		for (LinkableHashMap<K, V> m : maps) {
			for (K key : m.core.keySet()) {
				if (core.containsKey(key))
					continue;
				core.put(key, m.core.get(key).link());
			}
		}
	}
	
	/**
	 * This is not indicative of whether this key was ever linked with another map, but rather whether it is linked to
	 * another map at the time this method is called
	 * 
	 * @param key
	 *            the key to check
	 * @return false if there is no mapping for this key or its mapping not linked to any other maps, otherwise true
	 */
	public boolean isLinked(Object key) {
		Wrapper v = core.get(key);
		return v != null && v.isLinked();
	}
	
	@Override
	public V get(Object key) {
		return core.get(key).getValue();
	}
	
	@Override
	public int size() {
		return core.size();
	}
	
	@Override
	public boolean isEmpty() {
		return core.isEmpty();
	}
	
	@Override
	public boolean containsKey(Object key) {
		return core.containsKey(key);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return core.containsValue(value);
	}
	
	@Override
	public V put(K key, V value) {
		Wrapper old = core.get(key);
		if (old == null) {
			core.put(key, new Wrapper(value));
			return null;
		}
		return old.setValue(value);
	}
	
	@Override
	public V remove(Object key) {
		return core.remove(key).unlink().getValue();
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		if (m instanceof LinkableHashMap) {
			@SuppressWarnings("unchecked")
			LinkableHashMap<K, V> map = (LinkableHashMap<K, V>) m;
			core.putAll(map.core);
		}
	}
	
	@Override
	public void clear() {
		for (Wrapper value : core.values())
			value.unlink();
		core.clear();
	}
	
	@Override
	public Set<K> keySet() {
		return new WrappedKeySet(core.keySet(), core.entrySet());
	}
	
	@Override
	public Collection<V> values() {
		return new WrappedValues(core.values(), core.entrySet());
	}
	
	private final class WrappedKeySet extends AbstractCollection<K> implements Set<K> {
		private final Set<K> core;
		private final Set<Map.Entry<K, Wrapper>> base;
		
		public WrappedKeySet(Set<K> set, Set<Map.Entry<K, Wrapper>> base) {
			core = set;
			this.base = base;
		}
		
		@Override
		public int size() {
			return core.size();
		}
		
		@Override
		public boolean isEmpty() {
			return core.isEmpty();
		}
		
		@Override
		public boolean contains(Object o) {
			return core.contains(o);
		}
		
		@Override
		public Iterator<K> iterator() {
			return new WrappedKeyIterator(base.iterator());
		}
		
		@Override
		public Object[] toArray() {
			return core.toArray();
		}
		
		@Override
		public <T> T[] toArray(T[] a) {
			return core.toArray(a);
		}
		
		@Override
		public boolean remove(Object o) {
			return LinkableHashMap.this.remove(o) != null;
		}
		
		@Override
		public void clear() {
			LinkableHashMap.this.clear();
		}
		
	}
	
	private final class WrappedKeyIterator extends WrappedHashIterator<K> {
		
		public WrappedKeyIterator(Iterator<Map.Entry<K, Wrapper>> iterator) {
			super(iterator);
		}
		
		@Override
		public K next() {
			return nextEntry().getKey();
		}
	}
	
	private final class WrappedValueIterator extends WrappedHashIterator<V> {
		
		public WrappedValueIterator(Iterator<Map.Entry<K, Wrapper>> iterator) {
			super(iterator);
		}
		
		@Override
		public V next() {
			return nextEntry().getValue().getValue();
		}
	}
	
	private final class WrappedValues extends AbstractCollection<V> {
		private final Collection<Wrapper> core;
		private final Set<Map.Entry<K, Wrapper>> base;
		
		public WrappedValues(Collection<Wrapper> collection, Set<Map.Entry<K, Wrapper>> base) {
			core = collection;
			this.base = base;
		}
		
		@Override
		public Iterator<V> iterator() {
			return new WrappedValueIterator(base.iterator());
		}
		
		@Override
		public int size() {
			return core.size();
		}
		
		@Override
		public boolean contains(Object o) {
			return containsValue(o);
		}
		
		@Override
		public Object[] toArray() {
			return core.toArray();
		}
		
		@Override
		public <T> T[] toArray(T[] a) {
			return core.toArray(a);
		}
		
		@Override
		public void clear() {
			core.clear();
		}
	}
	
	private final class WrappedEntrySet extends AbstractSet<Map.Entry<K, V>> {
		private final Set<Map.Entry<K, Wrapper>> core;
		
		public WrappedEntrySet(Set<Map.Entry<K, Wrapper>> set) {
			core = set;
		}
		
		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new WrappedEntrySetIterator(core.iterator());
		}
		
		@Override
		public int size() {
			return core.size();
		}
		
		@Override
		public Object[] toArray() {
			return core.toArray();
		}
		
		@Override
		public <T> T[] toArray(T[] a) {
			return core.toArray(a);
		}
		
	}
	
	private final class WrappedEntrySetIterator extends WrappedHashIterator<Map.Entry<K, V>> {
		
		public WrappedEntrySetIterator(Iterator<Map.Entry<K, Wrapper>> iterator) {
			super(iterator);
		}
		
		@Override
		public java.util.Map.Entry<K, V> next() {
			return new WrappedEntry(nextEntry());
		}
	}
	
	private abstract class WrappedHashIterator<E> implements Iterator<E> {
		private final Iterator<Map.Entry<K, Wrapper>> core;
		private Map.Entry<K, Wrapper> current;
		
		public WrappedHashIterator(Iterator<Map.Entry<K, Wrapper>> iterator) {
			core = iterator;
			current = null;
		}
		
		@Override
		public boolean hasNext() {
			return core.hasNext();
		}
		
		final Map.Entry<K, Wrapper> nextEntry() {
			return current = core.next();
		}
		
		@Override
		public abstract E next();
		
		@Override
		public void remove() {
			current.getValue().unlink();
			core.remove();
		}
		
	}
	
	private final class WrappedEntry implements Map.Entry<K, V> {
		private final Map.Entry<K, Wrapper> core;
		
		public WrappedEntry(Map.Entry<K, Wrapper> entry) {
			core = entry;
		}
		
		@Override
		public K getKey() {
			return core.getKey();
		}
		
		@Override
		public V getValue() {
			return core.getValue().getValue();
		}
		
		@Override
		public V setValue(V value) {
			return core.getValue().setValue(value);
		}
		
	}
	
	private final class Wrapper {
		private V value;
		private int referenceCount;
		
		public Wrapper(V value) {
			this.value = value;
			referenceCount = 0;
		}
		
		public V getValue() {
			return value;
		}
		
		public V setValue(V newValue) {
			V old = value;
			value = newValue;
			return old;
		}
		
		/**
		 * This returns itself for convinience in other methods
		 * 
		 * @return itself
		 */
		public Wrapper link() {
			referenceCount++;
			return this;
		}
		
		/**
		 * This returns itself for convinience in other methods
		 * 
		 * @return itself
		 */
		public Wrapper unlink() {
			referenceCount--;
			return this;
		}
		
		public boolean isLinked() {
			return referenceCount > 1;
		}
	}
	
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new WrappedEntrySet(core.entrySet());
	}
}
