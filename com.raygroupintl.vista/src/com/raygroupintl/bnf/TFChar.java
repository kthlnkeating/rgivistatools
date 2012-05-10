package com.raygroupintl.bnf;


public abstract class TFChar extends TokenFactory {
	protected abstract boolean isValid(char ch);
	
	@Override
	public Token tokenize(String line, int fromIndex) {
		if (fromIndex < line.length()) {
			char ch = line.charAt(fromIndex);
			if (this.isValid(ch)) {
				return new TChar(ch);
			}
		}
		return null;
	}
}
