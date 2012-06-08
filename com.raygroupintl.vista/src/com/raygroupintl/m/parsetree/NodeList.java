package com.raygroupintl.m.parsetree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeList extends Nodes {
	List<Node> nodes = new ArrayList<Node>();

	public NodeList() {		
	}
	
	public NodeList(int size) {
		this.nodes = new ArrayList<Node>(size);	
	}

	public void add(Node node) {
		if (this.nodes == null) {
			this.nodes = new ArrayList<Node>();
		}
		this.nodes.add(node);
	}
		
	@Override
	public List<Node> getNodes() {
		if (this.nodes == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.nodes);
		}
	}
}
