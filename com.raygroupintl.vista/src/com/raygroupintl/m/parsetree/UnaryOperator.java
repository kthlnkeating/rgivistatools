package com.raygroupintl.m.parsetree;

import com.raygroupintl.struct.IterableSingle;

public class UnaryOperator extends Nodes {
	private IterableSingle<Node> expr;

	public UnaryOperator(Nodes expr) {
		this.expr = new IterableSingle<Node>(expr);
	}
	
	@Override
	public Iterable<Node> getNodes() {
		return this.expr;
	}	
}
