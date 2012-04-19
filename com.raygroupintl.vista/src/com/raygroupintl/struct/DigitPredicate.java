package com.raygroupintl.struct;

import com.raygroupintl.vista.fnds.ICharPredicate;

public class DigitPredicate implements ICharPredicate {
	@Override
	public boolean check(char ch) {
		return (ch >= '0') && (ch <= '9');
	}
}
