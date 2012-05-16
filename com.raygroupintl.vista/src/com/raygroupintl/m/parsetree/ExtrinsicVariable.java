package com.raygroupintl.m.parsetree;

public class ExtrinsicVariable implements Node, Caller {
	private CallInfo info;
	
	public ExtrinsicVariable(CallInfo info) {
		this.info = info;
	}
	
	@Override
	public CallInfo getCallInfo() {
		return this.info;
	}

	@Override
	public void accept(Visitor visitor) {
	}
}
