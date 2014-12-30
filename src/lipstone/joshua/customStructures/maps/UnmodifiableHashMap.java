package lipstone.joshua.customStructures.maps;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Wraps a {@link java.util.HashMap HashMap} such that only the accessor methods are valid. All other methods throw an
 * {@link java.lang.UnsupportedOperationException UnsupportedOperationException}.<br>
 * Instances of {@link UnmodifiableHashMap} are backed by the {@link java.util.Map Maps} that they wrap. Therefore, changes
 * made to the wrapped {@link java.util.Map Map} will be reflected in the {@link UnmodifiableHashMap}.
 * 
 * @author Joshua Lipstone
 * @param <K>
 *            the type of keys maintained by this map
 * @param <V>
 *            the type of mapped values
 */
public class UnmodifiableHashMap<K, V> extends HashMap<K, V> {
	private final Map<K, V> map;
	
	/**
	 * Creates a new {@link UnmodifiableHashMap} by wrapping the given {@link java.util.Map Map}.<br>
	 * This does <i>not</i> create a copy of the given {@link java.util.Map Map}. Therefore, any changes made to the given
	 * {@link java.util.Map Map} are reflected in this {@link UnmodifiableHashMap}.
	 * 
	 * @param map
	 *            the {@link java.util.Map Map} to wrap
	 */
	public UnmodifiableHashMap(Map<K, V> map) {
		this.map = map;
	}
	
	@Override
	public int size() {
		return map.size();
	}
	
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}
	
	@Override
	public V get(Object key) {
		return map.get(key);
	}
	
	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @return an unmodifiable set view of the keys contained in this map
	 */
	@Override
	public Set<K> keySet() {
		return Collections.unmodifiableSet(map.keySet());
	}
	
	/**
	 * @return an unmodifiable view of the values contained in this map
	 */
	@Override
	public Collection<V> values() {
		return Collections.unmodifiableCollection(map.values());
	}
	
	/**
	 * @return an unmodifiable set view of the mappings contained in this map
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return Collections.unmodifiableSet(map.entrySet());
	}
	
}
