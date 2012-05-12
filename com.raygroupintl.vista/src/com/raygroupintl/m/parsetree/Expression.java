package com.raygroupintl.m.parsetree;

public class Expression implements Node {
	@Override
	public void accept(Visitor visitor) {
		visitor.visitExpression(this);
	}
}
