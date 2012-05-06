package com.raygroupintl.struct;

import com.raygroupintl.fnds.ICharPredicate;

public class AndPredicate implements ICharPredicate {
	private ICharPredicate predicate0;
	private ICharPredicate predicate1;
	
	public AndPredicate(ICharPredicate predicate0, ICharPredicate predicate1) {
		this.predicate0 = predicate0;
		this.predicate1 = predicate1;
	}
	@Override
	public boolean check(char ch) {
		return this.predicate0.check(ch) && this.predicate1.check(ch);
	}
}
