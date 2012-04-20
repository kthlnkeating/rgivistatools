package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TBase;
import com.raygroupintl.vista.struct.MNameWithMnemonic;

public abstract class TKeyword extends TBase {
	private String value;

	public TKeyword(String value) {
		this.value = value;
	}

	public String getIdentier() {
		return this.value;
	}
	
	@Override
	public String getStringValue() {
		return this.value;
	}
	
	@Override
	public int getStringSize() {
		return this.value.length();
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
