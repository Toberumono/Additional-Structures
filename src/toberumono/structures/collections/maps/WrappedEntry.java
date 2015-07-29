package toberumono.structures.collections.maps;

import java.util.Map;
import java.util.function.Function;

/**
 * Wraps an {@link java.util.Map.Entry} of type <tt>T, S</tt> to produce a {@link java.util.Map.Entry} of type <tt>K, V</tt>.
 * 
 * @author Toberumono
 * @param <K>
 *            the key type of the wrapping {@link java.util.Map.Entry}
 * @param <V>
 *            the value type of the wrapping {@link java.util.Map.Entry}
 * @param <T>
 *            the key type of the wrapped {@link java.util.Map.Entry}
 * @param <S>
 *            the value type of the wrapped {@link java.util.Map.Entry}
 */
public class WrappedEntry<K, V, T, S> implements Map.Entry<K, V> {
	private final Map.Entry<T, S> back;
	private final Function<T, K> keyConverter;
	private final Function<S, V> valueConverter;
	private final Function<V, S> valueUnwrapper;
	
	/**
	 * Creates a new {@link WrappedEntry} from an existing {@link java.util.Map.Entry} of type <tt>T, S</tt>
	 * 
	 * @param back
	 *            the {@link java.util.Map.Entry} to be wrapped
	 * @param keyConverter
	 *            a {@link Function} that converts a value of type <tt>T</tt> into a equivalent value of type <tt>K</tt>
	 * @param valueConverter
	 *            a {@link Function} that converts a value of type <tt>S</tt> into a equivalent value of type <tt>V</tt>
	 */
	public WrappedEntry(Map.Entry<T, S> back, Function<T, K> keyConverter, Function<S, V> valueConverter) {
		this(back, keyConverter, valueConverter, null);
	}
	
	/**
	 * Creates a new {@link WrappedEntry} from an existing {@link java.util.Map.Entry} of type <tt>T, S</tt>
	 * 
	 * @param back
	 *            the {@link java.util.Map.Entry} to be wrapped
	 * @param keyConverter
	 *            a {@link Function} that converts a value of type <tt>T</tt> into a equivalent value of type <tt>K</tt>
	 * @param valueConverter
	 *            a {@link Function} that converts a value of type <tt>S</tt> into a equivalent value of type <tt>V</tt>
	 * @param valueUnwrapper
	 *            a {@link Function} that converts a value of type <tt>V</tt> into a equivalent value of type <tt>S</tt>. If
	 *            this is null, then {@link #setValue(Object)} throws an {@link UnsupportedOperationException}
	 */
	public WrappedEntry(Map.Entry<T, S> back, Function<T, K> keyConverter, Function<S, V> valueConverter, Function<V, S> valueUnwrapper) {
		this.back = back;
		this.keyConverter = keyConverter;
		this.valueConverter = valueConverter;
		this.valueUnwrapper = valueUnwrapper == null ? e -> {
			throw new UnsupportedOperationException("Cannot assign values without an appropriate converter.");
		} : valueUnwrapper;
	}
	
	@Override
	public K getKey() {
		return keyConverter.apply(back.getKey());
	}
	
	@Override
	public V getValue() {
		return valueConverter.apply(back.getValue());
	}
	
	/**
	 * Converts <tt>value</tt> to type <tt>S</tt> if the appropriate converter has been provided. Otherwise, it throws an
	 * {@link UnsupportedOperationException}.
	 * 
	 * @throws UnsupportedOperationException
	 *             if the <tt>V</tt> -&gt; <tt>S</tt> converter was not provided
	 */
	@Override
	public V setValue(V value) {
		return valueConverter.apply(back.setValue(valueUnwrapper.apply(value)));
	}
	
}
