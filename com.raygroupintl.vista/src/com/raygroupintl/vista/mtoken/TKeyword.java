package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TBase;
import com.raygroupintl.vista.token.TBasic;

public abstract class TKeyword extends TBase {
	private TBasic identifier;

	public TKeyword(String identifier) {
		this.identifier = new TBasic(identifier);
	}

	public String getIdentier() {
		return this.identifier.getStringValue();
	}
	
	@Override
	public String getStringValue() {
		return this.identifier.getStringValue();
	}
	
	@Override
	public int getStringSize() {
		return this.identifier.getStringSize();
	}
		
	protected abstract MNameWithMnemonic getNameWithMnemonic();
	
	@Override
	public void beautify() {
		MNameWithMnemonic name = this.getNameWithMnemonic();
		String fullName = name.getName();
		this.identifier.setValue(fullName);
	}
	
}
