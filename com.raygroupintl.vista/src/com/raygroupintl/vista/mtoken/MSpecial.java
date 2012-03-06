package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;

public abstract class MSpecial extends Keyword {
	private IToken arguments;
	
	public MSpecial(String identifier) {
		super(identifier);
	}
	
	@Override
	public String getStringValue() {
		String result = this.getPrefixString() + super.getStringValue();
		if (this.arguments != null) {
			result += this.arguments.getStringValue();
		}
		return result;
	}
	
	@Override
	public int getStringSize() {
		int size = this.getPrefixStringSize() + super.getStringSize();
		if (this.arguments != null) {
			size += this.arguments.getStringSize();
		}
		return size;
	}
	
	public void setArguments(IToken arguments) {
		this.arguments = arguments;
	}
	
	public abstract String getPrefixString();
	
	public abstract boolean hasArgument();
	
	public int getPrefixStringSize() {
		return this.getPrefixString().length();
	}
}

