package com.raygroupintl.bnf;

public class TFAnyBut extends TFBasic {
	@Override
	protected boolean stopOn(char ch) {
		return ch == ' ';
	}

	public static TFAnyBut getInstance() {
		return new TFAnyBut();
	}
}
