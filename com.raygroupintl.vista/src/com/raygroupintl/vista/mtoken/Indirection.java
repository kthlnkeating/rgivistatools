package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;

public class Indirection extends Base {
	private int precedingSpaces = 0;
	private IToken argument;
	private IToken subcripts;
	
	public Indirection(IToken argument) {
		this.argument = argument;
	}
	
	public Indirection(int precedingSpaces, IToken argument) {
		this.precedingSpaces = precedingSpaces;
		this.argument = argument;
	}
	
	public Indirection(int precedingSpaces, IToken argument, IToken subscripts) {
		this.precedingSpaces = precedingSpaces;
		this.argument = argument;
		this.subcripts = subscripts;
	}
	
	@Override
	public String getStringValue() {
		String result = "@";
		for (int i=0; i<this.precedingSpaces; ++i) {
			result += ' ';
		}
		result += this.argument.getStringValue();
		if (this.subcripts != null) {
			result += "@" + this.subcripts.getStringValue();
		}
		return result;
	}
	
	@Override
	public int getStringSize() {
		int result = 1 + this.precedingSpaces + this.argument.getStringSize();
		if (this.subcripts != null) {
			result += 1 + this.subcripts.getStringSize();
		}
		return result;
	}
	
	@Override
	public void beautify() {
		this.precedingSpaces = 0;
		if (this.argument != null) this.argument.beautify();
		if (this.subcripts != null) this.subcripts.beautify();
	}
	
}

