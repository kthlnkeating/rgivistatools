package com.raygroupintl.bnf;

public class TFEnd extends TokenFactory {

	@Override
	public Token tokenize(Text text) {
		if (text.onChar()) {
			return null;
		} else {
			return new TEmpty();
		}
	}
}
