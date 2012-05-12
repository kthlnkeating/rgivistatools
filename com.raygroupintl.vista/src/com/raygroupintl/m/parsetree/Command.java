package com.raygroupintl.m.parsetree;

public abstract class Command implements Node {
	public abstract Expression getPostcondition();
	
	public abstract ExpressionArray getArguments();

	@Override
	public void accept(Visitor visitor) {
		visitor.visitCommand(this);
	}
}
