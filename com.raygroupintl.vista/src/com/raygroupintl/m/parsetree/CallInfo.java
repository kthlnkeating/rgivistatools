package com.raygroupintl.m.parsetree;

public class CallInfo {
	private Fanout fanout;
	private NodeArray arguments;
	
	public CallInfo(Fanout fanout) {
		this.fanout = fanout;
		this.arguments = null;
	}
	
	public CallInfo(Fanout fanout, NodeArray arguments) {
		this.fanout = fanout;
		this.arguments = arguments;
	}
	
	public Fanout getFanout() {
		return this.fanout;
	}
	
	public NodeArray getArguments() {
		return this.arguments;
	}
}
