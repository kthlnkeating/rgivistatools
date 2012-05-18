package com.raygroupintl.m.parsetree;

public class GenericCommand extends AtomicCommand {
	public GenericCommand(Node node) {
		super(node);
	}
	
	@Override
	public void accept(Visitor visitor) {
	}	
}
