package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFChoice implements ITokenFactory {
	protected abstract ITokenFactory getFactory(char ch);
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);			
			ITokenFactory f = this.getFactory(ch);
			if (f != null) {
				IToken result = f.tokenize(line, fromIndex);
				return result;
			}
		}
		return null;
	}
	
	public boolean isInitialize() {
		return false;
	}
}
