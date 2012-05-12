package com.raygroupintl.m.parsetree;

public class Do extends GenericCommand implements Caller {
	private CallInfo callInfo;
	
	public Do(CallInfo callInfo, ExpressionArray array, Expression postCondition) {
		super(array, postCondition);
		this.callInfo = callInfo;
	}
		
	@Override
	public CallInfo getCallInfo() {
		return this.callInfo;
	}
}
