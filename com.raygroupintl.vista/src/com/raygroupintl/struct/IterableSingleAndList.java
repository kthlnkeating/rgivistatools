//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.struct;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IterableSingleAndList<T> implements Iterable<T> {
	private T leadingElement;
	private Iterable<T> remainingElements;

	public IterableSingleAndList(T leadingElement, Iterable<T> remainingElements) {
		this.leadingElement = leadingElement;
		this.remainingElements = remainingElements;
	}
	
	@Override
	public Iterator<T> iterator() {
		return this.new SingleAndListIterator();
	}
	
	private class SingleAndListIterator implements Iterator<T> {	
		private boolean initialState = true;
		private Iterator<T> remainingIterator;
		
		public SingleAndListIterator() {
			this.remainingIterator = IterableSingleAndList.this.remainingElements.iterator();
		}
		
		@Override
	    public boolean hasNext() {
	    	return this.initialState || this.remainingIterator.hasNext();
	    }
	
		@Override
		public T next() throws NoSuchElementException {
			if (! this.initialState) {
				return this.remainingIterator.next();
			} else {
				this.initialState = false;
				return IterableSingleAndList.this.leadingElement;
			}
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}