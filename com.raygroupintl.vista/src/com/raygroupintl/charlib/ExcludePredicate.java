package com.raygroupintl.charlib;


public class ExcludePredicate implements Predicate {
	private Predicate predicate;
	
	public ExcludePredicate(Predicate predicate) {
		this.predicate = predicate;
	}
	
	@Override
	public boolean check(char ch) {
		return ! this.predicate.check(ch);
	}

}
