package com.raygroupintl.parser;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SyntaxErrorException extends Exception {
	private int code;
	private List<TokenStore> tokens = new ArrayList<TokenStore>();
		
	public SyntaxErrorException(int code, TokenStore store) {
		this.code = code;
		this.tokens.add(store);			
	}
	
	public SyntaxErrorException() {
	}
	
	public SyntaxErrorException(int code) {
		this.code = code;
	}
	
	public SyntaxErrorException(TokenStore store) {
		this.tokens.add(store);
	}
	
	public int getCode() {
		return this.code;
	}

	public void addStore(TokenStore store) {
		this.tokens.add(store);
	}
	
	public List<TokenStore> getTokenStores() {
		return this.tokens;
	}
}
