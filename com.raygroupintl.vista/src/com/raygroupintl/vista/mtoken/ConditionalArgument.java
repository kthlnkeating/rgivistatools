package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;

public class ConditionalArgument extends DelimitedPair {
	private IToken argument;
	private IToken condition;
	
	public ConditionalArgument(IToken argument, IToken condition) {
		this.argument = argument;
		this.condition = condition;
	}
	
	@Override
	protected IToken getLeftToken() {
		return this.argument;
	}

	@Override
	protected IToken getRightToken() {
		return this.condition;
	}

	@Override
	protected String getDelimiter() {
		return ":";
	}

	@Override
	public boolean isError() {
		return false;
	}
}
