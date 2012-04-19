package com.raygroupintl.struct;

import com.raygroupintl.vista.fnds.ICharPredicate;

public class CharsPredicate implements ICharPredicate {
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
