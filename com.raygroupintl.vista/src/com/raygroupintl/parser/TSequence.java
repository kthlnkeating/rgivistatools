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
	public String getStringValue() {	
		String result = "";
		for (Token t : this.tokens) if (t != null) {
			result += t.getStringValue();
		}		
		return result;
	}

	@Override
	public int getStringSize() {
		int result = 0;
		for (Token t : this.tokens) if (t != null) {
			result +=  t.getStringSize();
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
}
