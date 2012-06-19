package com.raygroupintl.m.parsetree;

public class PostConditional extends AdditionalNodeHolder {
	public PostConditional(Node node) {
		super(node);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitPostConditional(this);
	}
}
