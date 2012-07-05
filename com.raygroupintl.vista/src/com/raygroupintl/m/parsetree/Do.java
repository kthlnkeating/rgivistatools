package com.raygroupintl.m.parsetree;

public class Do extends MultiCommand {
	private static final long serialVersionUID = 1L;

	public Do(Node postCondition, Node argument) {
		super(postCondition, argument);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitDo(this);
	}
}
