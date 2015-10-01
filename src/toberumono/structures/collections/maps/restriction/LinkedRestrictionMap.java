package toberumono.structures.collections.maps.restriction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import toberumono.structures.tuples.Pair;
import toberumono.structures.tuples.Triple;

public class LinkedRestrictionMap<K, V> implements RestrictionMap<K, V> {
	private final Map<K, RestrictionKernel> restricted; //Initial map is stored with null key
	private final RestrictionKernel allRK;
	private final Map<K, Pair<Map<String, Predicate<K>>, Map<String, Predicate<K>>>> restrictions;
	private final Supplier<Map<K, V>> mapConstructor;
	private final BiPredicate<Boolean, Boolean> mode;
	private final BooleanWrapper changed;
	private final Map<String, List<Triple<Predicate<K>, String, Predicate<K>>>> restrictionIDs;
	private RestrictionKernel backing;
	private K active;
	
	public LinkedRestrictionMap() {
		this((Supplier<Map<K, V>>) HashMap<K, V>::new);
	}
	
	public LinkedRestrictionMap(Supplier<Map<K, V>> mapConstructor) {
		this(mapConstructor, OR_MODE);
	}
	
	public LinkedRestrictionMap(BiPredicate<Boolean, Boolean> combinationMode) {
		this(HashMap<K, V>::new, combinationMode);
	}
	
	public LinkedRestrictionMap(Supplier<Map<K, V>> mapConstructor, BiPredicate<Boolean, Boolean> combinationMode) {
		this.mapConstructor = mapConstructor;
		this.mode = combinationMode;
		allRK = new RestrictionKernel(mapConstructor.get());
		restricted = new HashMap<>();
		restrictions = new HashMap<>();
		changed = new BooleanWrapper(false);
		restrictionIDs = new HashMap<>();
		restricted.put(null, allRK);
		restrict(null);
	}
	
	private LinkedRestrictionMap(Map<K, RestrictionKernel> restricted, RestrictionKernel allRK, Map<K, Pair<Map<String, Predicate<K>>, Map<String, Predicate<K>>>> restrictions,
			Supplier<Map<K, V>> mapConstructor, BiPredicate<Boolean, Boolean> mode, RestrictionKernel backing, K active, BooleanWrapper changed,
			Map<String, List<Triple<Predicate<K>, String, Predicate<K>>>> restrictionIDs) {
		this.restricted = restricted;
		this.restrictions = restrictions;
		this.allRK = allRK;
		this.mapConstructor = mapConstructor;
		this.mode = mode;
		this.changed = changed;
		this.restrictionIDs = restrictionIDs;
		this.active = active;
		this.backing = backing;
	}
	
	@Override
	public RestrictionMap<K, V> createLinkedCopy() {
		return new LinkedRestrictionMap<>(restricted, allRK, restrictions, mapConstructor, mode, backing, active, changed, restrictionIDs);
	}
	
	@Override
	public void reset() {
		restrict(null);
	}
	
	@Override
	public void restrict(K key) {
		processRestrictions();
		if (!restricted.containsKey(key))
			throw new UnsupportedOperationException("Cannot apply a restriction using a key (" + key.toString() + ") that is not in the map.");
		active = key;
		backing = restricted.get(active);
	}
	
	@Override
	public void unrestrict() {
		backing = allRK;
	}
	
	private synchronized void processRestrictions() {
		if (!changed.value)
			return;
		unrestrict();
		restrictions.clear(); //We need to remove the old restriction data.
		Pair<Map<String, Predicate<K>>, Map<String, Predicate<K>>> map;
		RestrictionKernel kernel;
		K key;
		for (Entry<K, V> entry : allRK.map.entrySet()) {
			key = entry.getKey();
			map = restrictions.get(key);
			if (map == null) {
				restricted.put(key, allRK);
				continue;
			}
			else
				restricted.put(key, kernel = new RestrictionKernel(mapConstructor.get()));
			boolean inc, exc;
			for (K k : allRK.map.keySet()) {
				exc = inc = (mode != OR_MODE);
				for (Predicate<K> rest : map.getX().values())
					inc = mode.test(inc, rest.test(k));
				for (Predicate<K> rest : map.getY().values())
					exc = mode.test(exc, rest.test(k));
				if ((map.getX().size() == 0 || inc) && (map.getY().size() == 0 || !exc))
					kernel.map.put(key, entry.getValue());
			}
			
		}
		changed.value = false;
	}
	
	@Override
	public synchronized void addRestriction(String identifier, Predicate<K> applies, String type, Predicate<K> satisfies) {
		if (restrictionIDs.containsKey(identifier))
			throw new UnsupportedOperationException("THIS IS A PLACEHOLDER"); //TODO Finish
		restrictionIDs.put(identifier, new ArrayList<>());
		type = type.replaceAll("-", "");
	}
	
	private void addRestrictionInner(String identifier, Predicate<K> applies, String type, Predicate<K> satisfies) {
		//Step 1: Convert any preceded-by restrictions to followed-by restrictions
		if (type.contains("<")) {
			//X not preceded by Y -> Y not followed by X
			if (type.contains("!")) {
				addRestrictionInner(identifier, satisfies, NOT_FOLLOWED_BY, applies);
			}
			//X preceded by Y -> ((not Y) not followed by X) and (Y followed by X)
			else {
				addRestrictionInner(identifier, satisfies.negate(), NOT_FOLLOWED_BY, applies);
				addRestrictionInner(identifier, satisfies, FOLLOWED_BY, applies);
			}
		}
		else if (type.contains(">")) {
			Pair<Map<String, Predicate<K>>, Map<String, Predicate<K>>> maps;
			for (K key : allRK.map.keySet()) {
				if (!applies.test(key))
					continue;
				if (!restrictions.containsKey(key))
					restrictions.put(key, maps = new Pair<>(new LinkedHashMap<>(), new LinkedHashMap<>()));
				else
					maps = restrictions.get(key);
				(type.contains("!") ? maps.getY() : maps.getX()).put(identifier, satisfies);
			}
			restrictionIDs.get(identifier).add(new Triple<>(applies, type, satisfies));
		}
		changed.value = true;
	}
	
	@Override
	public synchronized void removeRestriction(String identifier) {
		if (!restrictionIDs.containsKey(identifier))
			return;
		for (Pair<Map<String, Predicate<K>>, Map<String, Predicate<K>>> restriction : restrictions.values()) {
			if (restriction.getX().containsKey(identifier)) {
				restriction.getX().remove(identifier);
				changed.value = true;
			}
			if (restriction.getY().containsKey(identifier)) {
				restriction.getY().remove(identifier);
				changed.value = true;
			}
		}
		restrictionIDs.remove(identifier);
	}
	
	@Override
	public int size() {
		processRestrictions();
		return backing.map.size();
	}
	
	@Override
	public boolean isEmpty() {
		processRestrictions();
		return backing.map.isEmpty();
	}
	
	@Override
	public boolean containsKey(Object key) {
		processRestrictions();
		return backing.map.containsKey(key);
	}
	
	@Override
	public boolean containsValue(Object value) {
		processRestrictions();
		return backing.map.containsValue(value);
	}
	
	@Override
	public V get(Object key) {
		processRestrictions();
		return backing.map.get(key);
	}
	
	@Override
	public V put(K key, V value) {
		if (key == null)
			throw new UnsupportedOperationException("Cannot change the null map in a RestrictionMap.");
		if (changed.value = !restricted.containsKey(key))
			restricted.put(key, allRK);
		V out = allRK.map.put(key, value);
		for (RestrictionKernel k : restricted.values())
			k.map.put(key, value);
		changed.value = true;
		return out;
	}
	
	@Override
	public V remove(Object key) {
		processRestrictions();
		if (key == null)
			throw new UnsupportedOperationException("Cannot remove the null map from a RestrictionMap.");
		V out = allRK.map.remove(key);
		for (RestrictionKernel rk : restricted.values())
			rk.map.remove(key);
		restricted.remove(key);
		restrictions.remove(key);
		changed.value = true;
		return out;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Entry<? extends K, ? extends V> e : m.entrySet())
			put(e.getKey(), e.getValue());
	}
	
	@Override
	public void clear() {
		restricted.clear();
		restrictions.clear();
		allRK.map.clear();
		restricted.put(null, allRK);
		changed.value = false; //Because the map is empty there is nothing to work with.
		restrict(null);
	}
	
	@Override
	public Set<K> keySet() {
		return backing.ks == null ? (backing.ks = new KeySet(backing.map.keySet())) : backing.ks;
	}
	
	@Override
	public Collection<V> values() {
		return backing.vc == null ? (backing.vc = new ValueCollection(backing.map.values())) : backing.vc;
	}
	
	@Override
	public Set<Entry<K, V>> entrySet() {
		return backing.es == null ? (backing.es = new EntrySet(backing.map.entrySet())) : backing.es;
	}
	
	private class BooleanWrapper {
		private boolean value;
		
		private BooleanWrapper() {/* Nothing to do here */}
		
		private BooleanWrapper(boolean initial) {
			value = initial;
		}
	}
	
	private class RestrictionKernel {
		private final Map<K, V> map;
		private EntrySet es = null;
		private KeySet ks = null;
		private ValueCollection vc = null;
		
		private RestrictionKernel(Map<K, V> backing) {
			this.map = backing;
		}
	}
	
	private abstract class MapSetIterator<I> implements Iterator<I> {
		private final Iterator<I> backing;
		protected I current;
		
		private MapSetIterator(Iterator<I> backing) {
			this.backing = backing;
		}
		
		@Override
		public boolean hasNext() {
			return backing.hasNext();
		}
		
		@Override
		public I next() {
			return current = backing.next();
		}
	}
	
	private abstract class MapCollection<E> implements Collection<E> {
		private final Collection<E> backing;
		
		private MapCollection(Collection<E> backing) {
			this.backing = backing;
		}
		
		@Override
		public int size() {
			return backing.size();
		}
		
		@Override
		public boolean isEmpty() {
			return backing.isEmpty();
		}
		
		@Override
		public boolean contains(Object o) {
			return backing.contains(o);
		}
		
		@Override
		public Object[] toArray() {
			return backing.toArray();
		}
		
		@Override
		public <T> T[] toArray(T[] a) {
			return backing.toArray(a);
		}
		
		@Override
		public boolean containsAll(Collection<?> c) {
			return backing.containsAll(c);
		}
		
		@Override
		public boolean addAll(Collection<? extends E> c) {
			boolean out = false;
			for (E e : c)
				if (add(e))
					out = true;
			return out;
		}
		
		@Override
		public boolean retainAll(Collection<?> c) {
			boolean out = false;
			for (E e : this)
				if (!c.contains(e) && remove(e))
					out = true;
			return out;
		}
		
		@Override
		public void clear() {
			LinkedRestrictionMap.this.clear();
		}
		
		@Override
		public boolean removeAll(Collection<?> c) { //Just the code from AbstractSet
			Objects.requireNonNull(c);
			boolean modified = false;
			
			if (size() > c.size()) {
				for (Iterator<?> i = c.iterator(); i.hasNext();)
					modified |= remove(i.next());
			}
			else {
				for (Iterator<?> i = iterator(); i.hasNext();) {
					if (c.contains(i.next())) {
						i.remove();
						modified = true;
					}
				}
			}
			return modified;
		}
	}
	
	private class ValueSetIterator extends MapSetIterator<V> {
		
		private ValueSetIterator(Iterator<V> backing) {
			super(backing);
		}
	}
	
	private class ValueCollection extends MapCollection<V> implements Collection<V> {
		
		private ValueCollection(Collection<V> backing) {
			super(backing);
		}
		
		@Override
		public Iterator<V> iterator() {
			return new ValueSetIterator(super.backing.iterator());
		}
		
		@Override
		public boolean add(V e) {
			throw new UnsupportedOperationException("add");
		}
		
		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException("remove");
		}
	}
	
	private class KeySetIterator extends MapSetIterator<K> {
		
		private KeySetIterator(Iterator<K> backing) {
			super(backing);
		}
		
		@Override
		public void remove() {
			if (super.current == null)
				throw new IllegalStateException();
			super.backing.remove(); //This is here to avoid ConcurrentModificationException problems
			LinkedRestrictionMap.this.remove(super.current);
			super.current = null;
		}
		
	}
	
	private class KeySet extends MapCollection<K> implements Set<K> {
		
		private KeySet(Set<K> backing) {
			super(backing);
		}
		
		@Override
		public boolean remove(Object o) {
			try {
				@SuppressWarnings("unchecked")
				K key = (K) o;
				boolean out = LinkedRestrictionMap.this.allRK.map.containsKey(key);
				LinkedRestrictionMap.this.remove(key);
				return out;
			}
			catch (UnsupportedOperationException unused) {
				return false;
			}
			catch (ClassCastException unused) {
				return false;
			}
			catch (NullPointerException unused) {
				return false;
			}
		}
		
		@Override
		public boolean add(K e) {
			throw new UnsupportedOperationException("add");
		}
		
		@Override
		public Iterator<K> iterator() {
			return new KeySetIterator(super.backing.iterator());
		}
	}
	
	private class EntrySetIterator extends MapSetIterator<Entry<K, V>> {
		
		private EntrySetIterator(Iterator<Entry<K, V>> backing) {
			super(backing);
		}
		
		@Override
		public void remove() {
			if (super.current == null)
				throw new IllegalStateException();
			super.backing.remove(); //This is here to avoid ConcurrentModificationException problems
			LinkedRestrictionMap.this.remove(super.current.getKey());
			super.current = null;
		}
		
	}
	
	private class EntrySet extends MapCollection<Entry<K, V>> implements Set<Entry<K, V>> {
		
		private EntrySet(Set<Entry<K, V>> backing) {
			super(backing);
		}
		
		@Override
		public boolean add(Entry<K, V> e) {
			boolean out = !LinkedRestrictionMap.this.containsKey(e.getKey());
			if (!out) {
				V val = LinkedRestrictionMap.this.get(e.getKey());
				out = val == null ? e.getValue() != null : !val.equals(e.getValue());
			}
			LinkedRestrictionMap.this.put(e.getKey(), e.getValue());
			return out;
		}
		
		@Override
		public boolean remove(Object o) {
			try {
				@SuppressWarnings("unchecked")
				Entry<K, V> e = (Entry<K, V>) o;
				boolean out = LinkedRestrictionMap.this.allRK.map.containsKey(e.getKey());
				LinkedRestrictionMap.this.remove(e.getKey());
				return out;
			}
			catch (UnsupportedOperationException unused) {
				return false;
			}
			catch (ClassCastException unused) {
				return false;
			}
			catch (NullPointerException unused) {
				return false;
			}
		}
		
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new EntrySetIterator(super.backing.iterator());
		}
	}
}
