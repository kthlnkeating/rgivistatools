package com.raygroupintl.m.parsetree;

import com.raygroupintl.struct.IterableArray;

public class BinaryOperator extends CompoundExpression {
	private IterableArray<Expression> exprs;

	public BinaryOperator(Expression lhs, Expression rhs) {
		Expression[] array = {lhs, rhs};
		this.exprs = new IterableArray<Expression>(array);
	}
	
	@Override
	public Iterable<Expression> getSubExpressions() {
		return this.exprs;
	}	
}
