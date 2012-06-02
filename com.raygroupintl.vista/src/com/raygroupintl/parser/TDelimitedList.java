package com.raygroupintl.parser;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class TDelimitedList extends TList {
	private static class TDelimitedListIterator implements Iterator<Token> {
		private Iterator<Token> iterator;
		boolean firstNextCall = true;
		
		public TDelimitedListIterator(Iterator<Token> iterator) {
			this.iterator = iterator;
		}
		
		@Override
	    public boolean hasNext() {
			return this.iterator.hasNext();
	    }
	
		@Override
		public Token next() throws NoSuchElementException {
			if (this.firstNextCall) {
				this.firstNextCall = false;
				return this.iterator.next();
			} else {
				TSequence fullResult = (TSequence) this.iterator.next();
				Token result = fullResult.get(1);
				if (result == null) {
					result = new TEmpty();
				}
				return result;
			}
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}		
	}

	public TDelimitedList(List<Token> tokens) {
		super(tokens);
	}

	@Override
	public Iterator<Token> iterator() {
		return new TDelimitedListIterator(super.iterator());
	}
}

