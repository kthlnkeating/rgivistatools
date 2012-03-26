package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFChar implements ITokenFactory {
	protected abstract boolean isValid(char ch);
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		if (fromIndex < line.length()) {
			char ch = line.charAt(fromIndex);
			if (this.isValid(ch)) {
				return new TChar(ch);
			}
		}
		return null;
	}
}
