package com.raygroupintl.m.parsetree;

public class PostConditional extends AdditionalNodeHolder {
	private static final long serialVersionUID = 1L;

	public PostConditional(Node node) {
		super(node);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitPostConditional(this);
	}
}
