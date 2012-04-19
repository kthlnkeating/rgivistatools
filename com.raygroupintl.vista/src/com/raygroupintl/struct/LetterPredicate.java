package com.raygroupintl.struct;

import com.raygroupintl.vista.fnds.ICharPredicate;

public class LetterPredicate implements ICharPredicate {
	@Override
	public boolean check(char ch) {
		return ((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <='Z'));
	}
}
