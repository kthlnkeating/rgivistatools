package com.raygroupintl.m.parsetree;

public abstract class CompoundExpression extends Expression {
	public abstract Iterable<Expression> getSubExpressions();

	@Override
	public void accept(Visitor visitor) {
		visitor.visitCompoundExpression(this);
	}
}
