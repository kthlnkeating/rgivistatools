package com.raygroupintl.parser;

public class TCopy implements Token {
	private Token master;
	
	public TCopy(Token token) {
		this.master = token;
	}
	
	public void beautify() {
		this.master.beautify();
	}
	
	@Override
	public StringPieceImpl toValue() {
		return this.master.toValue();
	}	
}
