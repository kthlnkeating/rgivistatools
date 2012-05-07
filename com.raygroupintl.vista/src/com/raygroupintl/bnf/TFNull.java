package com.raygroupintl.bnf;


public class TFNull implements TokenFactory {
	@Override
	public Token tokenize(String line, int fromIndex) {
		return null;
	}
}
