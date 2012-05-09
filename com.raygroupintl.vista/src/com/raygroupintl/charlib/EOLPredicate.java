package com.raygroupintl.charlib;


public class EOLPredicate implements Predicate {
	@Override
	public boolean check(char ch) {
		return (ch == '\r') || (ch <= '\n');
	}
}
