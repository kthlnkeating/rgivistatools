package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFChoiceOnChar1st extends TFChoiceOnChar {
	public TFChoiceOnChar1st(ITokenFactory defaultFactory, String keys, ITokenFactory[] factories) {
		super(defaultFactory, keys, factories);
	}

	@Override
	protected ITokenFactory getFactory(String line, int index) {
		if (index+1 < line.length()) {			
			char ch = line.charAt(index);
			return this.getFactory(ch);
		} else {
			return null;
		}
	}	
}
