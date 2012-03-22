package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFNull implements ITokenFactory {
	@Override
	public IToken tokenize(String line, int fromIndex) {
		return null;
	}
}
