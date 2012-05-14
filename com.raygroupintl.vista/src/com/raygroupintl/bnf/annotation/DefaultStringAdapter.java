package com.raygroupintl.bnf.annotation;

import com.raygroupintl.bnf.StringAdapter;
import com.raygroupintl.bnf.TString;
import com.raygroupintl.bnf.Token;

class DefaultStringAdapter implements StringAdapter {
	@Override
	public Token convert(String value) {
		return new TString(value);
	}
}
