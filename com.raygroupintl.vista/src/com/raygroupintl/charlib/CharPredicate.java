package com.raygroupintl.charlib;


public class CharPredicate implements Predicate {
	private char ch;
	
	public CharPredicate(char ch) {
		this.ch = ch;
	}
	
	@Override
	public boolean check(char ch) {
		return this.ch == ch;
	}
}
