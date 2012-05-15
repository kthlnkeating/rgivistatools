package com.raygroupintl.bnf;


public class DefaultStringAdapter implements StringAdapter {
	@Override
	public Token convert(String value) {
		return new TString(value);
	}
}
