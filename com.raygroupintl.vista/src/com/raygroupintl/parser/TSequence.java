package com.raygroupintl.parser;

import java.util.Iterator;
import java.util.List;

public class TSequence implements Token, Iterable<Token> {
	private List<Token> tokens;
	
	public TSequence(List<Token> tokens) {
		this.tokens = tokens;
	}

	public TSequence(TSequence token) {
		this.tokens = token.tokens;
	}

	@Override
	public StringPiece toValue() {	
		StringPiece result = new StringPiece();
		for (Token t : this.tokens) if (t != null) {
			result.add(t.toValue());
		}		
		return result;
	}

	@Override
	public void beautify() {
		for (Token t : this.tokens) if (t != null) {
			t.beautify();
		}
	}
	
	public int size() {
		return this.tokens.size();
	}
	
	public Token get(int i) {
		if (this.tokens.size() > i) {
			return this.tokens.get(i);
		} else {
			return null;
		}
	}
	
	@Override
	public Iterator<Token> iterator() {
		return this.tokens.iterator();
	}
	
	@Override
	public List<Token> toList() {
		return this.tokens;
	}
}
