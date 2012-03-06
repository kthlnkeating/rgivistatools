package com.raygroupintl.vista.struct;

import java.util.HashMap;

public class MNameWithMnemonic {
	private String mnemonic;
	private String name;
	
	public MNameWithMnemonic(String mnemonic, String name) {
		this.mnemonic = mnemonic;
		this.name = name;
	}
	
	public String getMnemonic() {
		return this.mnemonic;
	}
	
	public String getName() {
		return this.name;
	}
	
	@SuppressWarnings("serial")
	public static class Map extends HashMap<String, MNameWithMnemonic> {
		public void update(String mnemonic, String name) {
			MNameWithMnemonic iv = new MNameWithMnemonic(mnemonic, name);
			this.put(mnemonic, iv);
			if (! mnemonic.equals(name)) {
				this.put(name, iv);			
			}
		}
	}
}
