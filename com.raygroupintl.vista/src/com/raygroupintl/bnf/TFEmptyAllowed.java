package com.raygroupintl.bnf;

import com.raygroupintl.bnf.TEmpty;

public abstract class TFEmptyAllowed implements TokenFactory {
	protected abstract TokenFactory getFactory();
	
	@Override
	public Token tokenize(String line, int fromIndex) {
		TokenFactory f = this.getFactory();
		Token result = f.tokenize(line, fromIndex);
		if (result != null) {
			return result;
		} else {
			return new TEmpty();
		}
	}
}
