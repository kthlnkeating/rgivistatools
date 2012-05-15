package com.raygroupintl.bnf;

public class TFEnd extends TokenFactory {
	public TFEnd(String name) {
		super(name);
	}
	
	@Override
	public Token tokenize(Text text) {
		if (text.onChar()) {
			return null;
		} else {
			return new TEmpty();
		}
	}
}
