package com.raygroupintl.m.parsetree;

public class Do extends MultiCommand {
	public Do(Node postCondition, Node argument) {
		super(postCondition, argument);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitDo(this);
	}
}
