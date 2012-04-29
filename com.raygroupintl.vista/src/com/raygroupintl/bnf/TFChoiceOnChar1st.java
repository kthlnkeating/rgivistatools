package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ICharPredicate;
import com.raygroupintl.fnds.ITokenFactory;

public class TFChoiceOnChar1st extends TFChoiceOnChar {
	private char leadingChar;
	
	public TFChoiceOnChar1st() {		
	}
			
	public TFChoiceOnChar1st(char leadingChar, ITokenFactory defaultFactory, ICharPredicate[] predicates, ITokenFactory[] factories) {
		super(defaultFactory, predicates, factories);
		this.leadingChar = leadingChar;
	}
	
	public void setLeadingChar(char ch) {
		this.leadingChar = ch;
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
