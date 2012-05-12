package com.raygroupintl.m.parsetree;

import com.raygroupintl.struct.IterableArray;

public class ExpressionArray extends CompoundExpression {
	private IterableArray<Expression> exprs;

	public ExpressionArray(Expression... expressions) {
		this.exprs = new IterableArray<Expression>(expressions);
	}

	@Override
	public Iterable<Expression> getSubExpressions() {
		return this.exprs;
	}	
}