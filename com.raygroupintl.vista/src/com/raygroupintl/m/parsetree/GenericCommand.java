package com.raygroupintl.m.parsetree;

public class GenericCommand extends MultiCommand {
	public GenericCommand(Node postCondition, Node argument) {
		super(postCondition, argument);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitGenericCommand(this);
	}
}
