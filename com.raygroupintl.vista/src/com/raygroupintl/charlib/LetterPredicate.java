package com.raygroupintl.charlib;


public class LetterPredicate implements Predicate {
	@Override
	public boolean check(char ch) {
		return ((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <='Z'));
	}
}
