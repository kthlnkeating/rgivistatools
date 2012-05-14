package com.raygroupintl.bnf;

import com.raygroupintl.charlib.Predicate;

public class TFCharacter extends TokenFactory {
	private Predicate predicate;
	private CharacterAdapter adapter;
	
	public TFCharacter(Predicate predicate) {
		this.predicate = predicate;
		this.adapter = new DefaultCharacterAdapter();
	}
		
	public TFCharacter(Predicate predicate, CharacterAdapter adapter) {
		this.predicate = predicate;
		this.adapter = adapter;
	}
		
	protected Token getToken(char value) {
		return this.adapter.convert(value);
	}
	
	@Override
	public Token tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);
			if (this.predicate.check(ch)) {
				return this.getToken(ch);
			}
		}
		return null;
	}
}
