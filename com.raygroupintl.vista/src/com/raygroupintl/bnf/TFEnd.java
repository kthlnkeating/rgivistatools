package com.raygroupintl.bnf;

public class TFEnd extends TokenFactory {

	@Override
	public Token tokenize(String line, int fromIndex) {
		if (fromIndex < line.length()) {
			return null;
		} else {
			return new TEmpty();
		}
	}
}
