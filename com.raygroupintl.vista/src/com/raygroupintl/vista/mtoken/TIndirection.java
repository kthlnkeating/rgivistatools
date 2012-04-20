package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.bnf.TBase;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.vista.struct.MError;

public class TIndirection extends TBase {
	private int precedingSpaces = 0;
	private IToken argument;
	private IToken subcripts;
	
	public TIndirection(IToken argument) {
		this.argument = argument;
	}
	
	public TIndirection(IToken argument, IToken subsripts) {
		this.argument = argument;
		this.subcripts = subsripts;
	}
	
	public TIndirection(int precedingSpaces, IToken argument) {
		this.precedingSpaces = precedingSpaces;
		this.argument = argument;
	}
	
	public TIndirection(int precedingSpaces, IToken argument, IToken subscripts) {
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
			result += "@(" + this.subcripts.getStringValue() + ")";
		}
		return result;
	}
	
	@Override
	public int getStringSize() {
		int result = 1 + this.precedingSpaces + this.argument.getStringSize();
		if (this.subcripts != null) {
			result += 3 + this.subcripts.getStringSize();
		}
		return result;
	}
	
	@Override
	public void beautify() {
		this.precedingSpaces = 0;
		if (this.argument != null) this.argument.beautify();
		if (this.subcripts != null) this.subcripts.beautify();
	}
	
	@Override
	public List<MError> getErrors() {
		return null;
	}
}

