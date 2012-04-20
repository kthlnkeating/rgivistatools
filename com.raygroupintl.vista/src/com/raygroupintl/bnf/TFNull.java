package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFNull implements ITokenFactory {
	@Override
	public IToken tokenize(String line, int fromIndex) {
		return null;
	}
}
