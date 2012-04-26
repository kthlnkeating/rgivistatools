package com.raygroupintl.m.cmdtree;

public class CommandVisitor extends Visitor {
	public <T extends Node> void visit(Block<T> block) {
		for (T node : block.getNodes()) {
			node.accept(this);
		}
	}
}
