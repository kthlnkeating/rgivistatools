package com.raygroupintl.bnf;

import com.raygroupintl.bnf.TEmpty;

public abstract class TFEmptyAllowed extends TokenFactory {
	protected abstract TokenFactory getFactory();
	
	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		TokenFactory f = this.getFactory();
		Token result = f.tokenize(line, fromIndex);
		if (result != null) {
			return result;
		} else {
			return new TEmpty();
		}
	}
}
