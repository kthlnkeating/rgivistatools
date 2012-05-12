package com.raygroupintl.m.parsetree;

public class Goto extends Command implements Caller {
	private Expression postCondition;
	private CallInfo callInfo;
	
	public Goto(Expression postCondition, CallInfo callInfo) {
		this.postCondition = postCondition;
		this.callInfo = callInfo;
	}
		
	@Override
	public Expression getPostcondition() {
		return this.postCondition;
	}

	@Override
	public ExpressionArray getArguments() {
		return null;
	}

	@Override
	public CallInfo getCallInfo() {
		return this.callInfo;
	}
}
