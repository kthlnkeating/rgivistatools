package com.raygroupintl.m.parsetree;

public class CallInfo {
	private Fanout fanout;
	private ExpressionArray arguments;
	
	public CallInfo(Fanout fanout) {
		this.fanout = fanout;
		this.arguments = null;
	}
	
	public CallInfo(Fanout fanout, ExpressionArray arguments) {
		this.fanout = fanout;
		this.arguments = arguments;
	}
	
	public Fanout getFanout() {
		return this.fanout;
	}
	
	public ExpressionArray getArguments() {
		return this.arguments;
	}
}
