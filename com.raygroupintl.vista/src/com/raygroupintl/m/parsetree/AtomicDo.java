package com.raygroupintl.m.parsetree;

public class AtomicDo extends AtomicCommand implements Caller {
	private Nodes postCondition;
	private CallInfo callInfo;
	
	public AtomicDo(Nodes postCondition, CallInfo callInfo) {
		this.postCondition = postCondition;
		this.callInfo = callInfo;
	}
		
	@Override
	public Nodes getPostcondition() {
		return this.postCondition;
	}

	@Override
	public NodeArray getArguments() {
		return null;
	}

	@Override
	public CallInfo getCallInfo() {
		return this.callInfo;
	}
}