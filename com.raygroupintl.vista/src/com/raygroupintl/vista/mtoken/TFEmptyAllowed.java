package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TEmpty;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFEmptyAllowed implements ITokenFactory {
	protected abstract ITokenFactory getFactory();
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		ITokenFactory f = this.getFactory();
		IToken result = f.tokenize(line, fromIndex);
		if (result != null) {
			return result;
		} else {
			return new TEmpty();
		}
	}
}
