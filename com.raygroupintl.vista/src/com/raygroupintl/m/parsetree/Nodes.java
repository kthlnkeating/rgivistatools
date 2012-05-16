package com.raygroupintl.m.parsetree;

public abstract class Nodes implements Node {
	public abstract Iterable<Node> getNodes();

	@Override
	public void accept(Visitor visitor) {
		visitor.visitNodes(this);
	}
}
