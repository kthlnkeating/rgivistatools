package com.raygroupintl.m.parsetree;

public class ExtrinsicFunction extends NodeArray implements Caller {
	private CallInfo info;
	
	public ExtrinsicFunction(CallInfo info) {
		this.info = info;
	}
	
	@Override
	public CallInfo getCallInfo() {
		return this.info;
	}
}
