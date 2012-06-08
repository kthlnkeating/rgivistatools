package com.raygroupintl.m.parsetree;

import java.util.List;

public abstract class Nodes implements Node {
	public abstract List<Node> getNodes();

	@Override
	public void accept(Visitor visitor) {
		visitor.visitNodes(this);
	}
}
