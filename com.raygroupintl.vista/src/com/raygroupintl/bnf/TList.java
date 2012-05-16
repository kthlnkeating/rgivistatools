package com.raygroupintl.bnf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TList implements Token {
	private List<Token> tokens = new ArrayList<Token>();
		
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
		this.tokens = tokens;
	}

	@Override
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		for (Token token : this.tokens) {
			sb.append(token.getStringValue());
		}
		return sb.toString();
	}

	@Override
	public int getStringSize() {
		int result = 0;
		for (Token token : this.tokens) {
			result += token.getStringSize();
		}
		return result;
	}

	@Override
	public void beautify() {
		for (Token token : this.tokens) {
			token.beautify();
		}
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
	
	public Iterator<Token> iterator() {
		return this.tokens.iterator();
	}
	
	List<Token> getList() {
		return this.tokens;
	}
}
