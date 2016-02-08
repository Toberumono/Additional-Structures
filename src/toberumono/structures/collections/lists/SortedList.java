package toberumono.structures.collections.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import toberumono.structures.SortingMethods;

/**
 * This class implements a sorted list. It is constructed with a {@link Comparator} for the type of elements being stored
 * ({@code T}) and sorts the elements accordingly. When you add an element to the list, it is inserted in the correct place.
 * Elements that are equal according to the {@link Comparator} will be sorted by the order in which they were inserted in the
 * {@link SortedList}.<br>
 * Although this implementation technically supports {@code null} elements, many {@link Comparator Comparators} do not, so
 * adding {@code null} elements is <i>not</i> recommended.
 * 
 * @author Toberumono
 * @param <T>
 *            the type stored in the {@link SortedList}
 * @see SortingMethods
 */
public class SortedList<T> extends ArrayList<T> implements Cloneable {
	private final Comparator<? super T> comparator;
	private boolean sortingEnabled = true;
	
	/**
	 * Constructs a new sorted list with the specified initialCapacity and comparator.<br>
	 * This sorts in descending order. That is, the items with the lower value appear at the end of the list.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of this list
	 * @param comparator
	 *            the comparator to sort this list with
	 */
	public SortedList(int initialCapacity, Comparator<? super T> comparator) {
		super(initialCapacity);
		this.comparator = comparator;
	}
	
	/**
	 * Constructs a new sorted list with the specified comparator.<br>
	 * If the comparator is null, this effectively becomes a Stack.<br>
	 * This sorts in descending order. That is, the items with the lower value appear at the end of the list.
	 * 
	 * @param comparator
	 *            the comparator to sort this list with
	 */
	public SortedList(Comparator<? super T> comparator) {
		this.comparator = comparator;
	}
	
	/**
	 * Constructs a new list with the items in <tt>list</tt> but sorted with a new comparator.<br>
	 * This sorts in descending order. That is, the items with the lower value appear at the end of the list.
	 * 
	 * @param list
	 *            the old list
	 * @param comparator
	 *            the comparator to sort this list with
	 */
	public SortedList(Collection<? extends T> list, Comparator<? super T> comparator) {
		super(list);
		this.comparator = comparator;
	}
	
	/**
	 * Constructs a new SortedList that is a duplicate of the specified list.
	 * 
	 * @param list
	 *            the list to duplicate
	 */
	public SortedList(SortedList<T> list) {
		this.comparator = list.comparator;
		this.addAll(list);
	}
	
	/**
	 * Explicitly specifying the position of an element is not valid for this kind of list, instead this method only returns
	 * the element at <tt>index</tt>. It does <i>NOT</i> modify the list in any way.
	 * 
	 * @return the element at the specified position
	 */
	@Override
	public T set(int index, T element) {
		return get(index);
	}
	
	/**
	 * Specifying an index for insertion is invalid - therefore, this just forwards to {@link #addAll(Collection) addAll(c)}.
	 */
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return addAll(c);
	}
	
	/**
	 * Adds the items in the specified collection to this list such that they are correctly sorted with the initial elements
	 * as specified by this list's comparator. The behavior of this operation is undefined if the specified collection is
	 * modified while the operation is in progress. (This implies that the behavior of this call is undefined if the
	 * specified collection is this list, and this list is nonempty.)
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		if (!super.addAll(c))
			return false;
		if (sortingEnabled && comparator != null)
			quickSort(0, size());
		return true;
	}
	
	/**
	 * This method forwards to {@link #add(Object) add(element)} because it would otherwise violate the sorting order.
	 */
	@Override
	public void add(int index, T element) {
		add(element);
	}
	
	/**
	 * Adds an object to the list. The object will be inserted in the correct place so that the objects in the list are
	 * sorted. When the list already contains objects that are equal according to the comparator, the new object will be
	 * inserted immediately after these other objects.
	 * 
	 * @param element
	 *            the object to be added
	 */
	@Override
	public boolean add(T element) {
		if (!sortingEnabled || comparator == null)
			return super.add(element);
		super.add(getPos(element, 0, size()), element);
		return true;
	}
	
	/**
	 * Returns the element at the specified position in the list. If sorting was disabled at the time of this call, sorting
	 * is re-enabled before retrieving the item.
	 */
	@Override
	public T get(int index) {
		if (!sortingEnabled)
			enableSorting();
		return super.get(index);
	}
	
	/**
	 * @return the {@link java.util.Comparator Comparator} used to sort the elements in this list.
	 */
	public Comparator<? super T> getComparator() {
		return comparator;
	}
	
	/**
	 * Temporarily disables the automatic sorting of this list. Sorting will remain disabled until the next
	 * {@link #enableSorting()} or {@link #get(int)} call, whichever comes first.
	 */
	public void disableSorting() {
		sortingEnabled = false;
	}
	
	/**
	 * Enables the automatic sorting of this list if it was not already enabled and sorts it if the {@link Comparator} is not
	 * {@code null}.
	 */
	public void enableSorting() {
		sortingEnabled = true;
		if (comparator != null)
			quickSort(0, size() - 1);
	}
	
	/**
	 * @param element
	 *            the element of which to get the index
	 * @return the index of the first occurrence of the specified element in this list, or -1 if this list does not contain
	 *         the element.
	 */
	@Override
	public int indexOf(Object element) {
		try {
			@SuppressWarnings("unchecked")
			T e = (T) element;
			int item = getPos(e, 0, size());
			if (item == size())
				return -1;
			return get(item).equals(element) ? item : -1;
		}
		catch (ClassCastException e) {
			return super.indexOf(element);
		}
	}
	
	@Override
	public boolean contains(Object element) {
		return indexOf(element) != -1;
	}
	
	private int getPos(T element, int left, int right) {
		if (left == right)
			return left;
		int middle = left + (right - left) / 2;
		if (comparator.compare(element, get(middle)) < 0) { //If it is smaller than the middle element
			if (middle == 0)
				return 0;
			return getPos(element, left, middle);
		}
		else if ((right - left) < 250 && comparator.compare(element, get(middle)) == 0) { //If these elements are the same, skip forward until it finds a different one
			for (; middle < size() && comparator.compare(element, get(middle)) == 0; middle++);
			return middle;
		}
		else { //If it is larger than the middle element
			if (middle >= size() - 1)
				return size();
			return getPos(element, middle + 1, right);
		}
	}
	
	/**
	 * Used when the number of items to be sorted (right - left) is less than 250.
	 * 
	 * @param left
	 *            the leftmost bound to sort
	 * @param right
	 *            the rightmost bound (exclusive) to sort
	 */
	private void selectionSort(int left, int right) {
		for (int i = left; i < right; i++)
			for (int j = i + 1; j < right; j++)
				if (comparator.compare(get(i), get(j)) <= 0) {
					T temp = get(i);
					set(i, get(j));
					set(j, temp);
				}
	}
	
	/**
	 * Used when the number of items to be sorted ({@code right - left}) is at least 250. If the number of items to be sorted
	 * is less than 250, it forwards to {@link #selectionSort(int, int) selectionSort(left, right)}
	 * 
	 * @param left
	 *            the leftmost bound to sort
	 * @param right
	 *            the rightmost bound (exclusive) to sort
	 */
	private void quickSort(int left, int right) {
		if (left < right) { //If the section of this list has at least 2 items
			if (right - left < 250) {
				selectionSort(left, right);
				return;
			}
			int pivotIndex = left + (right - left) / 2;
			pivotIndex = partition(left, right, pivotIndex);
			quickSort(left, pivotIndex - 1);
			quickSort(pivotIndex - 1, right);
		}
	}
	
	private int partition(int left, int right, int pivotIndex) {
		T pivotItem = get(pivotIndex);
		set(pivotIndex, get(right));
		set(right, pivotItem);
		int storeIndex = left;
		for (int i = left; i < right; i++) {
			if (comparator.compare(get(i), pivotItem) <= 0) {
				T temp = get(i);
				set(i, get(storeIndex));
				set(storeIndex, temp);
				storeIndex++;
			}
		}
		set(right, get(storeIndex));
		set(storeIndex, pivotItem);
		return storeIndex;
	}
}
