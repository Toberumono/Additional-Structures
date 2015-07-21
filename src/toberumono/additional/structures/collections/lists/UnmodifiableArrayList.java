package toberumono.additional.structures.collections.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Wraps an {@link java.util.ArrayList ArrayList} or a {@link java.util.Collection Collection} such that only the accessor
 * methods are valid. All other methods throw an {@link java.lang.UnsupportedOperationException
 * UnsupportedOperationException}.<br>
 * Instances of {@link UnmodifiableArrayList} are backed by the {@link java.util.ArrayList ArrayList} that they wrap.
 * Therefore, changes made to the wrapped {@link java.util.ArrayList ArrayList} will be reflected in the
 * {@link UnmodifiableArrayList}.
 * 
 * @author Joshua Lipstone
 * @param <E>
 *            the type of the elements in the list
 */
public class UnmodifiableArrayList<E> extends ArrayList<E> implements List<E> {
	private final ArrayList<E> list;
	
	public UnmodifiableArrayList(ArrayList<E> list) {
		super();
		this.list = list;
	}
	
	public UnmodifiableArrayList(Collection<E> c) {
		super();
		this.list = new ArrayList<>(c);
	}
	
	@Override
	public boolean equals(Object o) {
		return o == this || list.equals(o);
	}
	
	@Override
	public int hashCode() {
		return list.hashCode();
	}
	
	@Override
	public E get(int index) {
		return list.get(index);
	}
	
	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}
	
	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void sort(Comparator<? super E> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}
	
	@Override
	public ListIterator<E> listIterator(final int index) {
		return new ListIterator<E>() {
			private final ListIterator<? extends E> i = list.listIterator(index);
			
			@Override
			public boolean hasNext() {
				return i.hasNext();
			}
			
			@Override
			public E next() {
				return i.next();
			}
			
			@Override
			public boolean hasPrevious() {
				return i.hasPrevious();
			}
			
			@Override
			public E previous() {
				return i.previous();
			}
			
			@Override
			public int nextIndex() {
				return i.nextIndex();
			}
			
			@Override
			public int previousIndex() {
				return i.previousIndex();
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void set(E e) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void add(E e) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void forEachRemaining(Consumer<? super E> action) {
				i.forEachRemaining(action);
			}
		};
	}
	
	@Override
	public ArrayList<E> subList(int fromIndex, int toIndex) {
		return new UnmodifiableArrayList<>((ArrayList<E>) list.subList(fromIndex, toIndex));
	}
	
	@Override
	public int size() {
		return list.size();
	}
	
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}
	
	@Override
	public Iterator<E> iterator() {
		return (Iterator<E>) listIterator();
	}
	
	@Override
	public Object[] toArray() {
		return list.toArray();
	}
	
	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}
	
	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
}
