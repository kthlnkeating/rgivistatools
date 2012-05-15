package com.raygroupintl.bnf;


public class TFNull extends TokenFactory {
	public TFNull(String name) {
		super(name);
	}
	 
	@Override
	public Token tokenize(Text text) {
		return null;
	}
}
