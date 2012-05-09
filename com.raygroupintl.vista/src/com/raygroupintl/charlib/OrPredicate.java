package com.raygroupintl.charlib;


public class OrPredicate implements Predicate {
	private Predicate predicate0;
	private Predicate predicate1;
	
	public OrPredicate(Predicate predicate0, Predicate predicate1) {
		this.predicate0 = predicate0;
		this.predicate1 = predicate1;
	}
	@Override
	public boolean check(char ch) {
		return this.predicate0.check(ch) || this.predicate1.check(ch);
	}
}
