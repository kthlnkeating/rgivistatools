package com.raygroupintl.vista.token;

public class TFAnyBut extends TFBasic {
	@Override
	protected boolean stopOn(char ch) {
		return ch == ' ';
	}

	public static TFAnyBut getInstance() {
		return new TFAnyBut();
	}
}
