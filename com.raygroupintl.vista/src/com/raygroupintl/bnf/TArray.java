package com.raygroupintl.bnf;

public class TArray implements Token, TokenArray {
	private Token[] tokens;
	
	public TArray(Token[] tokens) {
		this.tokens = tokens;
	}

	public TArray(TArray token) {
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
	
	@Override
	public int getCount() {
		return this.tokens.length;
	}
	
	@Override
	public Token get(int i) {
		if (this.tokens.length > i) {
			return this.tokens[i];
		} else {
			return null;
		}
	}
	
	protected Token[] getTokens() {
		return this.tokens;
	}
}
