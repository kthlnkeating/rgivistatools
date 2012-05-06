package com.raygroupintl.struct;

import com.raygroupintl.fnds.ICharPredicate;

public class CharRangePredicate implements ICharPredicate {
	private char ch0;
	private char ch1;
	
	public CharRangePredicate(char ch0, char ch1) {
		if (ch0 < ch1) {
			this.ch0 = ch0;
			this.ch1 = ch1;	
		} else {
			this.ch0 = ch1;
			this.ch1 = ch0;				
		}
	}
	
	@Override
	public boolean check(char ch) {
		return (ch >= this.ch0) && (ch <= this.ch1);
	}
}
