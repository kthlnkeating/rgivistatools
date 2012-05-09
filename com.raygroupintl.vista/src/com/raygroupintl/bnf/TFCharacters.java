package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ICharPredicate;

public class TFCharacters implements TokenFactory {
	private ICharPredicate predicate;
	
	public TFCharacters(ICharPredicate predicate) {
		this.predicate = predicate;
	}
		
	protected Token getToken(String line) {
		return new TCharacters(line);
	}
	
	@Override
	public Token tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		int index = fromIndex;
		while (index < endIndex) {
			char ch = line.charAt(index);
			if (! this.predicate.check(ch)) {
				if (fromIndex == index) {
					return null;
				} else {
					return this.getToken(line.substring(fromIndex, index));
				}
			}
			++index;
		}
		if (fromIndex < endIndex) {
			return this.getToken(line.substring(fromIndex));			
		} else {
			return null;
		}
	}
}
