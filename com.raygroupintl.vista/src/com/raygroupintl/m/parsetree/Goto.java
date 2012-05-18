package com.raygroupintl.m.parsetree;

public class Goto extends MultiCommand  {
	public Goto(Node postCondition, Node argument) {
		super(postCondition, argument);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitGoto(this);
	}
}
