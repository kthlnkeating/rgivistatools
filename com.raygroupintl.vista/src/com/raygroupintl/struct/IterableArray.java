package com.raygroupintl.struct;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IterableArray<T> implements Iterable<T> {
	private T[] array;

	public IterableArray(T[] array) {
		this.array = array;
	}
	
	@Override
	public Iterator<T> iterator() {
		return this.new ArrayIterator();
	}
	
	private class ArrayIterator implements Iterator<T> {
		private int index;
		
		@Override
	    public boolean hasNext() {
	    	return (this.index < IterableArray.this.array.length);
	    }
	
		@Override
		public T next() throws NoSuchElementException {
			if (this.index >= IterableArray.this.array.length) {
				throw new NoSuchElementException();
			}
			T result = IterableArray.this.array[this.index];
			++this.index;
			return result;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
