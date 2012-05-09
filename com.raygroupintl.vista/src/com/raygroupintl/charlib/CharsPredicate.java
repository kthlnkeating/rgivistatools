package com.raygroupintl.charlib;


public class CharsPredicate implements Predicate {
	private char[] chs;
	
	public CharsPredicate(char... chs) {
		this.chs = chs;
	}
	
	@Override
	public boolean check(char ch) {
		for (char c : this.chs) {
			if (ch == c) return true;
		}
		return false;
	}
}
