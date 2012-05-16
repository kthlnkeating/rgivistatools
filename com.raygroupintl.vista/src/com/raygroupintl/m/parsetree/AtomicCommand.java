package com.raygroupintl.m.parsetree;

public abstract class AtomicCommand implements Node {
	public abstract Nodes getPostcondition();
	
	public abstract NodeArray getArguments();

	@Override
	public void accept(Visitor visitor) {
		visitor.visitAtomicCommand(this);
	}
}
