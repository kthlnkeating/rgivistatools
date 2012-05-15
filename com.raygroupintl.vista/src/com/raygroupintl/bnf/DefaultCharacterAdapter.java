package com.raygroupintl.bnf;


public class DefaultCharacterAdapter implements CharacterAdapter {
	@Override
	public Token convert(char value) {
		return new TChar(value);
	}
}
