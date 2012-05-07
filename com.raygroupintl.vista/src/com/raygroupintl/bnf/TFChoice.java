package com.raygroupintl.bnf;


public abstract class TFChoice implements TokenFactory {
	protected abstract TokenFactory getFactory(char ch);
	
	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);			
			TokenFactory f = this.getFactory(ch);
			if (f != null) {
				Token result = f.tokenize(line, fromIndex);
				return result;
			}
		}
		return null;
	}
	
	public boolean isInitialize() {
		return false;
	}
}
