package com.raygroupintl.m.parsetree;

public abstract class AtomicCommand implements Node {
	public abstract Expression getPostcondition();
	
	public abstract ExpressionArray getArguments();

	@Override
	public void accept(Visitor visitor) {
		visitor.visitAtomicCommand(this);
	}
}
