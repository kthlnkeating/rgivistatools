package com.raygroupintl.m.parsetree;

public class IntegerLiteral extends TerminalNode {
	private String value;
	
	public IntegerLiteral(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitIntegerLiteral(this);
	}
}
