package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFChoiceOnChar0th extends TFChoiceOnChar {
	public TFChoiceOnChar0th(ITokenFactory defaultFactory, String keys, ITokenFactory... factories) {
		super(defaultFactory, keys, factories);
	}

	public TFChoiceOnChar0th(ITokenFactory defaultFactory, char key, ITokenFactory factory) {
		this(defaultFactory, String.valueOf(key), factory);
	}

	@Override
	protected ITokenFactory getFactory(String line, int index) {
		if (index < line.length()) {
			char ch = line.charAt(index);
			return this.getFactory(ch);
		} else {
			return null;
		}
	}		
}
