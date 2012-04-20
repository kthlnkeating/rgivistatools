package com.raygroupintl.bnf;

import com.raygroupintl.vista.fnds.IToken;

public class TPrefixedCopy extends TCopy {
	private String prefix;
	
	public TPrefixedCopy(IToken source, String prefix) {
		super(source);
		this.prefix = prefix;
	}

	@Override
	public String getStringValue() {
		return this.prefix + super.getStringValue();
	}

	@Override
	public int getStringSize() {
		return this.prefix.length() + super.getStringSize();
	}
}
