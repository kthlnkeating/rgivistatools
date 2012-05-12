package com.raygroupintl.bnf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.raygroupintl.vista.struct.MError;

public class TList implements Token {
	private class DelimitedIterator implements Iterator<Token> {
		private int index;
		
		@Override
	    public boolean hasNext() {
	    	return (this.index < TList.this.tokens.size());
	    }
	
		@Override
		public Token next() throws NoSuchElementException {
			Token t = TList.this.tokens.get(this.index);
			if (this.index > 0) {
				t = ((TArray) t).get(1);
			}
			++this.index;
			return t;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	private ArrayList<Token> tokens = new ArrayList<Token>();
		
	public TList() {
	}
	
	public TList(Token token) {
		this.tokens.add(token);
	}

	public TList(Token token0, Token token1) {
		this.tokens.add(token0);
		this.tokens.add(token1);
	}

	public TList(List<Token> tokens) {
		this.tokens.addAll(tokens);
	}

	public TList(TList tokens) {
		this.tokens.addAll(tokens.tokens);
	}

	protected String getDelimiter() {
		return null;
	}
	
	@Override
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		String delimiter = this.getDelimiter();
		for (Token token : this.tokens) {
			if ((delimiter != null) && (! first)) {
				sb.append(delimiter);
			}			
			sb.append(token.getStringValue());
			first = false;
		}
		return sb.toString();
	}

	@Override
	public int getStringSize() {
		int result = 0;
		for (Token token : this.tokens) {
			result += token.getStringSize();
		}
		String delimiter = this.getDelimiter();
		if (delimiter != null) {
			result += this.tokens.size() - 1;
		}		
		return result;
	}

	@Override
	public List<MError> getErrors() {
		List<MError> result = null;
		for (Token token : this.tokens) {
			if (token.hasError()) {
				List<MError> errors = token.getErrors();
				if (result == null) {
					result = new ArrayList<MError>(errors);
				} else {
					result.addAll(errors);
				}
			}
		}
		return result;
	}

	@Override
	public boolean hasError() {
		for (Token token : this.tokens) {
			if (token.hasError()) return true;
		}
		return false;
	}

	@Override
	public boolean hasFatalError() {
		for (Token token : this.tokens) {
			if (token.hasFatalError()) return true;
		}
		return false;
	}
	
	@Override
	public void beautify() {
		for (Token token : this.tokens) {
			token.beautify();
		}
	}
	
	public void add(Token token) {
		this.tokens.add(token);
	}
	
	public void addAll(List<Token> tokens) {
		this.tokens.addAll(tokens);
	}
	
	public void addAll(TList tokens) {
		this.tokens.addAll(tokens.tokens);
	}
	
	public Token get(int index) {
		return this.tokens.get(index);
	}
	
	public int size() {
		return this.tokens.size();
	}

	public void add(int index, Token token) {
		this.tokens.add(index, token);
	}
	
	public Iterator<Token> iteratorForDelimited() {
		return this.new DelimitedIterator();
	}

	public Iterator<Token> iterator() {
		return this.tokens.iterator();
	}
}
