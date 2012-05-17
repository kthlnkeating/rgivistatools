package com.raygroupintl.m.parsetree;

import java.util.ArrayList;
import java.util.List;

public class NodeList extends Nodes {
	List<Node> nodes = new ArrayList<Node>();

	public NodeList() {
		this(5);
	}
	
	public NodeList(int size) {
		this.nodes = new ArrayList<Node>(size);	
	}

	public void add(Node node) {
		this.nodes.add(node);
	}
	
	@Override
	public Iterable<Node> getNodes() {
		return this.nodes;
	}
}
