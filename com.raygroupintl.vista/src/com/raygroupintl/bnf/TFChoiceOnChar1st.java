package com.raygroupintl.bnf;

import com.raygroupintl.charlib.Predicate;

public class TFChoiceOnChar1st extends TFChoiceOnChar {
	private char leadingChar;
	
	public TFChoiceOnChar1st() {		
	}
			
	public TFChoiceOnChar1st(char leadingChar, TokenFactory defaultFactory, Predicate[] predicates, TokenFactory[] factories) {
		super(defaultFactory, predicates, factories);
		this.leadingChar = leadingChar;
	}
	
	public void setLeadingChar(char ch) {
		this.leadingChar = ch;
	}

	@Override
	protected TokenFactory getFactory(Text text) {
		if (text.onChar(1)) {			
			char ch0th = text.getChar();
			if (ch0th == this.leadingChar) {
				char ch1st = text.getChar(1);
				return this.getFactory(ch1st);
			}
		}
		return null;
	}	
}
