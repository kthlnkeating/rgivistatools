package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TBasic;

public class TFConstString implements ITokenFactory {
	private String value;
	
	public TFConstString(String value) {
		this.value = value;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		if (line.startsWith(this.value, fromIndex)) {
			return new TBasic(this.value);
		} else {
			return null;
		}
	}
}
