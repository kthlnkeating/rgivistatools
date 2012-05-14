package com.raygroupintl.m.parsetree;

public class Goto extends MultiCommand<AtomicGoto> {
	private Expression postcondition;

	@Override
	public Expression getPostcondition() {
		return this.postcondition;
	}
}
