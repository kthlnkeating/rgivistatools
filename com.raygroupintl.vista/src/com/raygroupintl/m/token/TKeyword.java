package com.raygroupintl.m.token;

import com.raygroupintl.vista.struct.MNameWithMnemonic;

public abstract class TKeyword extends MTString {
	public TKeyword(String value) {
		super(value);
	}

	public String getIdentier() {
		return this.getValue();
	}
	
	protected abstract MNameWithMnemonic getNameWithMnemonic();
	
	@Override
	public void beautify() {
		MNameWithMnemonic name = this.getNameWithMnemonic();
		if (name != null) {
			this.setValue(name.getName());
		}
	}	
}
