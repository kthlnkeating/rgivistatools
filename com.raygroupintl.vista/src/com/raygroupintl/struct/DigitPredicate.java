package com.raygroupintl.struct;

import com.raygroupintl.fnds.ICharPredicate;

public class DigitPredicate implements ICharPredicate {
	@Override
	public boolean check(char ch) {
		return (ch >= '0') && (ch <= '9');
	}
}
