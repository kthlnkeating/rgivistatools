package com.raygroupintl.m.parsetree;

public class GenericCommand extends AtomicCommand {
	private Nodes postCondition;
	private NodeArray arguments;
	
	public GenericCommand(NodeArray arguments, Nodes postCondition) {
		this.arguments = arguments;
		this.postCondition = postCondition;
	}
	
	@Override
	public Nodes getPostcondition() {
		return this.postCondition;
	}

	@Override
	public NodeArray getArguments() {
		return this.arguments;
	}
}
