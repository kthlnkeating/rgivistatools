package com.raygroupintl.m.parsetree;

public class GenericCommand extends AtomicCommand {
	private Expression postCondition;
	private ExpressionArray arguments;
	
	public GenericCommand(ExpressionArray arguments, Expression postCondition) {
		this.arguments = arguments;
		this.postCondition = postCondition;
	}
	
	@Override
	public Expression getPostcondition() {
		return this.postCondition;
	}

	@Override
	public ExpressionArray getArguments() {
		return this.arguments;
	}
}
