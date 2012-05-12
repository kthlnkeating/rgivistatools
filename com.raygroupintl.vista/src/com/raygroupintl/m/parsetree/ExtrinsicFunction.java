package com.raygroupintl.m.parsetree;

public class ExtrinsicFunction extends ExpressionArray implements Caller {
	private CallInfo info;
	
	public ExtrinsicFunction(CallInfo info) {
		this.info = info;
	}
	
	@Override
	public CallInfo getCallInfo() {
		return this.info;
	}
}
