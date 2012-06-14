package com.raygroupintl.m.parsetree;

abstract class AdditionalNodeHolder extends BasicNode {
	private Node additionalNode;
	
	protected AdditionalNodeHolder(Node node) {
		this.additionalNode = node;
	}
	
	public Node getAdditionalNode() {
		return this.additionalNode;
	}
}
