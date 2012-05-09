package com.raygroupintl.m.struct;

import com.raygroupintl.charlib.LetterPredicate;
import com.raygroupintl.charlib.Predicate;

public class IdentifierStartPredicate implements Predicate {
	private LetterPredicate letterPred = new LetterPredicate();
	
	@Override
	public boolean check(char ch) {
		 return (ch == '%') || this.letterPred.check(ch);
	}
}
