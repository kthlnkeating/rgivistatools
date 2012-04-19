package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.ICharPredicate;
import com.raygroupintl.vista.fnds.ITokenFactory;

class TFChoiceOnChar1st extends TFChoiceOnChar {
	private char leadingChar;
	
	public TFChoiceOnChar1st(char leadingChar, ITokenFactory defaultFactory, ICharPredicate[] predicates, ITokenFactory[] factories) {
		super(defaultFactory, predicates, factories);
		this.leadingChar = leadingChar;
	}

	@Override
	protected ITokenFactory getFactory(String line, int index) {
		if (index+1 < line.length()) {			
			char ch0th = line.charAt(index);
			if (ch0th == this.leadingChar) {
				char ch1st = line.charAt(index+1);
				return this.getFactory(ch1st);
			}
		}
		return null;
	}	
}
