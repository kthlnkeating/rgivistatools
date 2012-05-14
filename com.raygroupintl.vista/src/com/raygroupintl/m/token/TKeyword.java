package com.raygroupintl.m.token;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.vista.struct.MNameWithMnemonic;

public abstract class TKeyword implements Token {
	private String value;

	public TKeyword(String value) {
		this.value = value;
	}

	public String getIdentier() {
		return this.value;
	}
	
	protected abstract MNameWithMnemonic getNameWithMnemonic();
	
	@Override
	public void beautify() {
		MNameWithMnemonic name = this.getNameWithMnemonic();
		if (name != null) {
			this.value = name.getName();
		}
	}	
}
