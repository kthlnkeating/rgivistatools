package com.raygroupintl.bnf;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SyntaxErrorException extends Exception {
	private int code;
	private int location;
	private List<TokenStore> tokens = new ArrayList<TokenStore>();
		
	public SyntaxErrorException(int code, int location, TokenStore store) {
		this.code = code;
		this.location = location;
		this.tokens.add(store);			
	}
	
	public SyntaxErrorException(int code, int location) {
		this.code = code;
		this.location = location;
	}
	
	public SyntaxErrorException(int location, TokenStore store) {
		this.location = location;
		this.tokens.add(store);
	}
	
	public SyntaxErrorException(int location) {
		this.location = location;
	}
	
	public int getLocation() {
		return this.location;
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
