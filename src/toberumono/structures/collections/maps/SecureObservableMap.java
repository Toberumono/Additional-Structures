package toberumono.structures.collections.maps;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/**
 * @author Toberumono
 *
 * @param <K> the type of the keys
 * @param <V> the type of the values
 */
public class SecureObservableMap<K, V> implements ObservableMap<K, V> {
	
	/**
	 * The default initial capacity - MUST be a power of two.
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
	
	/**
	 * The maximum capacity, used if a higher value is implicitly specified by either of the constructors with arguments.
	 * MUST be a power of two <= 1<<30.
	 */
	static final int MAXIMUM_CAPACITY = 1 << 30;
	
	/**
	 * The load factor used when none specified in constructor.
	 */
	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
	static class Node<K, V> implements Map.Entry<K, V> {
		final int hash;
		final K key;
		V value;
		Node<K, V> next;
		
		Node(int hash, K key, V value, Node<K, V> next) {
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		@Override
		public K getKey() {
			return key;
		}
		
		@Override
		public V getValue() {
			return value;
		}
		
		@Override
		public V setValue(V val) {
			V old = value;
			value = val;
			return old;
		}
		
		@Override
		public String toString() {
			return key + "=" + value;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == this) return true;
			if (o instanceof Map.Entry) {
				Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
				return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
			}
			return false;
		}
	}
	
	/* Static Utils */
	
	/**
	 * This method spreads the effect of the upper bits on the hash down to the lower ones. This allows the upper bits in the
	 * hash to affect the hash in small tables despite the power of 2 masking. Yes, it is a bit slower, but it reduces
	 * collisions.
	 * 
	 * @param key
	 *            the {@link Object} to get the modified hash code of
	 * @return the modified hash code
	 */
	static final int hash(Object key) {
		int h;
		return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}
	
	static final Class<?> classIfComparable(Object o) {
		if (o instanceof Comparable) {
			Class<?> c;
			Type[] ts, as;
			ParameterizedType p;
			if ((c = o.getClass()) == String.class) return c; //String is the most common key type and we know that it is comparable, so we can skip the other checks
			if ((ts = c.getGenericInterfaces()) != null) {
				for (Type t : ts)
					if (t instanceof ParameterizedType &&
							(p = (ParameterizedType) t).getRawType() == Comparable.class &&
							(as = p.getActualTypeArguments()) != null &&
							as.length == 1 && as[0] == c) // type arg is c
					return c;
			}
		}
		return null;
	}
	
	/**
	 * @param initialCapacity
	 *            the initial capacity of the map
	 * @param loadFactor
	 *            the load factor
	 * @throws IllegalArgumentException
	 *             if initial capacity &lt; 0 or the load factor &le; 0
	 */
	public SecureObservableMap(int initialCapacity, float loadFactor) {
		
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public V get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public V put(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void removeListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addListener(MapChangeListener<? super K, ? super V> listener) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void removeListener(MapChangeListener<? super K, ? super V> listener) {
		// TODO Auto-generated method stub
		
	}
	
}
