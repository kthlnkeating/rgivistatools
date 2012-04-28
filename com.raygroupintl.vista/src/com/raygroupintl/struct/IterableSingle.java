package com.raygroupintl.struct;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IterableSingle<T> implements Iterable<T> {
	private T element;

	public IterableSingle(T element) {
		this.element = element;
	}
	
	@Override
	public Iterator<T> iterator() {
		return this.new SingleIterator();
	}
	
	private class SingleIterator implements Iterator<T> {	
		private boolean initialState = true;
		
		@Override
	    public boolean hasNext() {
	    	return this.initialState;
	    }
	
		@Override
		public T next() throws NoSuchElementException {
			if (! this.initialState) {
				throw new NoSuchElementException();
			}
			this.initialState = false;
			return IterableSingle.this.element;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}