package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFConstString implements ITokenFactory {
	private String value;
	private boolean ignoreCase;
	
	public TFConstString(String value) {
		this.value = value;
		this.ignoreCase = false;
	}
	
	public TFConstString(String value, boolean ignoreCase) {
		this.value = value;
		this.ignoreCase = ignoreCase;
	}

	private String getMatched(String line, int fromIndex) {
		if (this.ignoreCase) {
			String piece = line.substring(fromIndex, fromIndex+this.value.length());
			if (piece.equalsIgnoreCase(this.value)) {
				return piece;
			}
		} else {
			if (line.startsWith(this.value, fromIndex)) {
				return this.value;
			}
		}
		return null;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		String result = this.getMatched(line, fromIndex);
		if (result != null) {
			return new TBasic(result);
		} else {
			return null;
		}
	}
}
