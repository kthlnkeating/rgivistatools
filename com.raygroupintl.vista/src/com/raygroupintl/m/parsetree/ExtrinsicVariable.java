package com.raygroupintl.m.parsetree;

public class ExtrinsicVariable extends Expression implements Caller {
	private CallInfo info;
	
	public ExtrinsicVariable(CallInfo info) {
		this.info = info;
	}
	
	@Override
	public CallInfo getCallInfo() {
		return this.info;
	}
}
