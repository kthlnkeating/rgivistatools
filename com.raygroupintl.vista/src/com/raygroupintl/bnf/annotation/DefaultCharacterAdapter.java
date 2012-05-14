package com.raygroupintl.bnf.annotation;

import com.raygroupintl.bnf.CharacterAdapter;
import com.raygroupintl.bnf.TChar;
import com.raygroupintl.bnf.Token;

class DefaultCharacterAdapter implements CharacterAdapter {
	@Override
	public Token convert(char value) {
		return new TChar(value);
	}
}
