package com.raygroupintl.bnf;


public class TFEol implements TokenFactory {
	@Override
	public Token tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch0th = line.charAt(fromIndex);
			if ((ch0th == '\n') || (ch0th == '\r')) {
				int nextIndex = fromIndex + 1;
				if (nextIndex < endIndex) {
					char ch1st = line.charAt(nextIndex);
					if ((ch1st == '\n') || (ch1st == '\r')) {
						return new TCharacters(line.substring(fromIndex, fromIndex+2));
					}
				}
				return new TCharacters(line.substring(fromIndex, fromIndex+1));
			}
		}
		return null;
	}
}
