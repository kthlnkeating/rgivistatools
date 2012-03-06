package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.struct.MNameWithMnemonic;

public abstract class Keyword extends Base {
	private Basic identifier;

	public Keyword(String identifier) {
		this.identifier = new Basic(identifier);
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
