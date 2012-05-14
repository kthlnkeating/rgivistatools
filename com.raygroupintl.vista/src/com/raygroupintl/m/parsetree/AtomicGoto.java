package com.raygroupintl.m.parsetree;

public class AtomicGoto extends AtomicCommand implements Caller {
	private Expression postCondition;
	private CallInfo callInfo;
	
	public AtomicGoto(Expression postCondition, CallInfo callInfo) {
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
