package com.raygroupintl.bnf;

import com.raygroupintl.charlib.Predicate;

public class TFCharacter extends TokenFactory {
	private Predicate predicate;
	private CharacterAdapter adapter;
	
	public TFCharacter(Predicate predicate) {
		this.predicate = predicate;
	}
		
	protected Token getToken(char value) {
		if (this.adapter == null) {
			return new TChar(value);
		} else {
			return this.adapter.convert(value);
		}
	}
	
	public void setAdapter(CharacterAdapter adapter) {
		this.adapter = adapter;
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
