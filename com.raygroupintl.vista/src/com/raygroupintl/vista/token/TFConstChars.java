package com.raygroupintl.vista.token;

import com.raygroupintl.vista.mtoken.TFChar;

public class TFConstChars extends TFChar {
	private String chars;

	public TFConstChars(String chars) {
		this.chars = chars;
	}
	
	@Override
	protected boolean isValid(char ch) {
		return this.chars.indexOf(ch, 0) >= 0;
	}
}
