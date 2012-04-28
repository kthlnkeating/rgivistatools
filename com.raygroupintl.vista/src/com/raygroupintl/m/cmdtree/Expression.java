package com.raygroupintl.m.cmdtree;

public class Expression implements Node {
	@Override
	public void accept(Visitor visitor) {
		visitor.visitExpression(this);
	}
}
