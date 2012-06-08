package com.raygroupintl.m.parsetree;

public class DoBlock extends NodeList {
	private Node postCondition;
	
	public DoBlock(Node postCondition) {
		this.postCondition = postCondition;
	}
	
	public Node getPostCondition() {
		return this.postCondition;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitDoBlock(this);
	}
}
