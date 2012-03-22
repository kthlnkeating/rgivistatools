package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TEmpty;

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
