package com.raygroupintl.struct;

import com.raygroupintl.fnds.ICharPredicate;

public class EOLPredicate implements ICharPredicate {
	@Override
	public boolean check(char ch) {
		return (ch == '\r') || (ch <= '\n');
	}
}
