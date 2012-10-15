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

package com.raygroupintl.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class DelimitedListStore implements TokenStore {
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
				Token rawFullResult = this.iterator.next();
				TokenStore fullResult = (TokenStore) rawFullResult;
				Token result = fullResult.get(1);
				return result;
			}
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}		
	}

	private List<Token> tokens;

	public DelimitedListStore(List<Token> tokens) {
		this.tokens = tokens;
	}

	@Override
	public void addToken(Token token) {
		if (this.tokens == null) {
			this.tokens = new ArrayList<Token>();
		}
		this.tokens.add(token);
	}

	@Override
	public List<Token> toList() {
		if (this.tokens == null) {
			return Collections.emptyList();
		} else {
			return this.tokens;
		}
	}

	@Override
	public void setLength(int length) {		
	}

	@Override
	public void resetIndex(int index) {		
	}
	
	@Override
	public boolean isAllNull() {
		return (this.tokens == null) || (this.tokens.size() == 0);
	}

	@Override
	public int size() {
		return this.tokens == null ? 0 : this.tokens.size();
	}

	@Override
	public boolean hasToken() {
		return this.tokens != null;
	}
	
	public void set(int index, Token token) {
		this.tokens.set(index, token);
	}

	@Override
	public StringPiece toValue() {	
		StringPiece result = new StringPiece();
		if (this.tokens != null) for (Token t : this.tokens) if (t != null) {
			result.add(t.toValue());
		}		
		return result;
	}

	@Override
	public Token get(int index) {
		return this.tokens == null ? null : this.tokens.get(index);
	}
	
	public Iterable<Token> all() {
		if (this.tokens == null) {
			return Collections.emptyList();
		} else {
			return this.tokens;
		}
	}
		
	@Override
	public Iterator<Token> iterator() {
		return new TDelimitedListIterator(this.toList().iterator());
	}
}
