package com.raygroupintl.m.parsetree;

public class ExternalDo extends AtomicCommand {
	private static final long serialVersionUID = 1L;

	public ExternalDo(Node additionalNodes) {
		super(additionalNodes);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitExternalDo(this);
	}
}
