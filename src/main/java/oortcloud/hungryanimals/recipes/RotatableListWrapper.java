package oortcloud.hungryanimals.recipes;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RotatableListWrapper<T> implements List<T> {

	private List<T> list;
	private int rotation;
	
	public RotatableListWrapper(List<T> list, int rotation) {
		this.list = list;
		this.rotation = rotation;
	}

	private int outToIn(int index_outside) {
		return (index_outside+rotation) % size();
	}
	
	private int inToOut(int index_inside) {
		return (index_inside-rotation+size()) % size();
	}
	
	@Override
	public boolean add(T e) {
		return list.add(e);
	}

	@Override
	public void add(int index, T element) {
		list.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return list.addAll(index, c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public T get(int index) {
		return list.get(outToIn(index));
	}

	@Override
	public int indexOf(Object o) {
		return inToOut(list.indexOf(o));
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int next = 0;
			
			@Override
			public boolean hasNext() {
				return next < size();
			}

			@Override
			public T next() {
				return get(next++);
			}
		};
	}

	@Override
	public int lastIndexOf(Object o) {
		return inToOut(list.lastIndexOf(o));
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return list.listIterator(outToIn(index));
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public T remove(int index) {
		return list.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public T set(int index, T element) {
		return list.set(outToIn(index), element);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	
}