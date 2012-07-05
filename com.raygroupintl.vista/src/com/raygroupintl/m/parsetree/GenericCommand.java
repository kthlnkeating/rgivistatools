package com.raygroupintl.m.parsetree;

public class GenericCommand extends MultiCommand {
	private static final long serialVersionUID = 1L;

	public GenericCommand(Node postCondition, Node argument) {
		super(postCondition, argument);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitGenericCommand(this);
	}
}
