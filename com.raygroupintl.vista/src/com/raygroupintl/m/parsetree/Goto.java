package com.raygroupintl.m.parsetree;

public class Goto extends MultiCommand  {
	private static final long serialVersionUID = 1L;

	public Goto(Node postCondition, Node argument) {
		super(postCondition, argument);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitGoto(this);
	}
}
