package com.raygroupintl.m.cmdtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Block<T extends Node> extends Node {
	private List<T> nodes;

	public void add(T node) {
		if (this.nodes == null) {
			this.nodes = new ArrayList<T>();
		}
		this.nodes.add(node);
	}
	
	public List<T> getNodes() {
		if (this.nodes == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.nodes);
		}
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
