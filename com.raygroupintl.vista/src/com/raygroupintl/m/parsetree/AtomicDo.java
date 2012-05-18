package com.raygroupintl.m.parsetree;

public class AtomicDo extends AtomicCommand {
	public AtomicDo(Node additionalNodes) {
		super(additionalNodes);
	}
		
	@Override
	public void accept(Visitor visitor) {
		visitor.visitAtomicDo(this);
	}
}