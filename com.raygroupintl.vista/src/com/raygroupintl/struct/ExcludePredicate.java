package com.raygroupintl.struct;

import com.raygroupintl.fnds.ICharPredicate;

public class ExcludePredicate implements ICharPredicate {
	private ICharPredicate predicate;
	
	public ExcludePredicate(ICharPredicate predicate) {
		this.predicate = predicate;
	}
	
	@Override
	public boolean check(char ch) {
		return ! this.predicate.check(ch);
	}

}
