package com.raygroupintl.m.parsetree;

public class AtomicGoto extends AtomicCommand implements Caller {
	private CallInfo callInfo;
	
	public AtomicGoto(Node additionalNodes, CallInfo callInfo) {
		super(additionalNodes);
		this.callInfo = callInfo;
	}
		
	@Override
	public void accept(Visitor visitor) {
	}	

	@Override
	public CallInfo getCallInfo() {
		return this.callInfo;
	}
}
