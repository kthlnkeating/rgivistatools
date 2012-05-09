package com.raygroupintl.charlib;


public class DigitPredicate implements Predicate {
	@Override
	public boolean check(char ch) {
		return (ch >= '0') && (ch <= '9');
	}
}
