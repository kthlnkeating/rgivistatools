package com.raygroupintl.m.parsetree;

import com.raygroupintl.struct.IterableArray;

public class NodeArray extends Nodes {
	private IterableArray<Node> nodes;

	public NodeArray(Node... nodes) {
		this.nodes = new IterableArray<Node>(nodes);
	}

	@Override
	public Iterable<Node> getNodes() {
		return this.nodes;
	}	
}