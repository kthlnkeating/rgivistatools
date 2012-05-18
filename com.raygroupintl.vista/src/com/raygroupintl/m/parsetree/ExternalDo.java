package com.raygroupintl.m.parsetree;

public class ExternalDo extends AtomicCommand {
	public ExternalDo(Node additionalNodes) {
		super(additionalNodes);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitExternalDo(this);
	}
}
