package com.raygroupintl.struct;

import com.raygroupintl.fnds.ICharPredicate;

public class CharPredicate implements ICharPredicate {
	private char ch;
	
	public CharPredicate(char ch) {
		this.ch = ch;
	}
	
	@Override
	public boolean check(char ch) {
		return this.ch == ch;
	}
}
