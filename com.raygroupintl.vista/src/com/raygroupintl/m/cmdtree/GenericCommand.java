package com.raygroupintl.m.cmdtree;

public class GenericCommand extends Command {
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
