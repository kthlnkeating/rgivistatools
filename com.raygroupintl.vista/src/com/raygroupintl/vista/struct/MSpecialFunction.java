package com.raygroupintl.vista.struct;


public class MSpecialFunction extends MNameWithMnemonic {
	private int minNumArguments;
	private int maxNumArguments;
	
	public MSpecialFunction(String mnemonic, String name, int minNumArguments, int maxNumArguments) {
		super(mnemonic, name);
		this.minNumArguments = minNumArguments;
		this.maxNumArguments = maxNumArguments;
	}		

	public int getMinNumArguments() {
		return this.minNumArguments;
	}
	
	public int getMaxNumArguments() {
		return this.maxNumArguments;
	}
}
