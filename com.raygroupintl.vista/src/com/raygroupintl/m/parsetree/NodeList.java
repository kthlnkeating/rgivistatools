package com.raygroupintl.m.parsetree;

import java.util.ArrayList;
import java.util.List;

public class NodeList extends Nodes {
	List<Node> nodes = new ArrayList<Node>();

	public void add(Node node) {
		this.nodes.add(node);
	}
	
	@Override
	public Iterable<Node> getNodes() {
		return this.nodes;
	}
}
