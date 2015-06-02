package toberumono.customStructures.collections.iterators;

import java.util.Iterator;
import java.util.function.Function;

/**
 * Wraps an {@link Iterator} of type <tt>V</tt> to produce an {@link Iterator} of type <tt>T</tt>.
 * 
 * @author Joshua Lipstone
 * @param <T>
 *            the type of the wrapping {@link Iterator}
 * @param <V>
 *            the type of the original {@link Iterator}
 */
public class WrappedIterator<T, V> implements Iterator<T> {
	private final Iterator<V> back;
	private final Function<V, T> converter;
	
	/**
	 * Creates a new {@link WrappedIterator} from an existing {@link Iterator} of type <tt>T</tt>
	 * 
	 * @param back
	 *            the original {@link Iterator}
	 * @param converter
	 *            a {@link Function} that converts a value of type <tt>V</tt> into a equivalent value of type <tt>T</tt>
	 */
	public WrappedIterator(Iterator<V> back, Function<V, T> converter) {
		this.back = back;
		this.converter = converter;
	}
	
	@Override
	public boolean hasNext() {
		return back.hasNext();
	}
	
	@Override
	public T next() {
		return converter.apply(back.next());
	}
	
	@Override
	public void remove() {
		back.remove();
	}
}
