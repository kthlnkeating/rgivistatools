package com.raygroupintl.m.struct;

import com.raygroupintl.fnds.ICharPredicate;
import com.raygroupintl.struct.LetterPredicate;

public class IdentifierStartPredicate implements ICharPredicate {
	private LetterPredicate letterPred = new LetterPredicate();
	
	@Override
	public boolean check(char ch) {
		 return (ch == '%') || this.letterPred.check(ch);
	}
}
