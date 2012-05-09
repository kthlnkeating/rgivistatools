package com.raygroupintl.bnf;


public abstract class TFBasic implements TokenFactory {
	protected abstract boolean stopOn(char ch);
	
	protected Token getToken(String line) {
		return new TCharacters(line);
	}
	
	@Override
	public Token tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			int index = fromIndex;
			do {
				char ch = line.charAt(index);
				if (this.stopOn(ch)) {
					if (fromIndex == index) {
						return null;
					} else {
						return this.getToken(line.substring(fromIndex, index));
					}
				}
				++index;
			} while (index < endIndex);
			return this.getToken(line.substring(fromIndex));
		}
		return null;
	}
	
	public static TFBasic getInstance(final char ch0, final char ch1) {
		return new TFBasic() {			
			@Override
			protected boolean stopOn(char ch) {
				return (ch != ch0) && (ch != ch1);
			}
		};
	}
	
	public static TFBasic getInstance(final char chIn) {
		return new TFBasic() {			
			@Override
			protected boolean stopOn(char ch) {
				return ch != chIn;
			}
		};
	}
}
