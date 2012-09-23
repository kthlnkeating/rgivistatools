package com.raygroupintl.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class TDelimitedList implements Token, TokenStore {
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

	public TDelimitedList(Token token) {
		this.tokens = token.toList();
	}

	public TDelimitedList(List<Token> tokens) {
		this.tokens = tokens;
	}

	public TDelimitedList() {
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
	public void beautify() {
		if (this.tokens != null) for (Token token : this.tokens) {
			token.beautify();
		}
	}
	
	@Override
	public Token get(int index) {
		return this.tokens == null ? null : this.tokens.get(index);
	}
	
	@Override
	public Iterator<Token> iterator() {
		return new TDelimitedListIterator(this.toList().iterator());
	}
}

