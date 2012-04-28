package com.raygroupintl.m.cmdtree;

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
