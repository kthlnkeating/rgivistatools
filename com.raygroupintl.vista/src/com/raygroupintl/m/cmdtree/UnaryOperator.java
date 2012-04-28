package com.raygroupintl.m.cmdtree;

import com.raygroupintl.struct.IterableSingle;

public class UnaryOperator extends CompoundExpression {
	private IterableSingle<Expression> expr;

	public UnaryOperator(Expression expr) {
		this.expr = new IterableSingle<Expression>(expr);
	}
	
	@Override
	public Iterable<Expression> getSubExpressions() {
		return this.expr;
	}	
}
