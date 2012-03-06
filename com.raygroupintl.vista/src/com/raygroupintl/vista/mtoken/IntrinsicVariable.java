package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.struct.MNameWithMnemonic;

public class IntrinsicVariable extends Intrinsic {
	private static final MNameWithMnemonic.Map INSTRINSIC_VARIABLES = new MNameWithMnemonic.Map();
	static {
		INSTRINSIC_VARIABLES.update("D", "DEVICE"); 	
		INSTRINSIC_VARIABLES.update("EC", "ECODE"); 	
		INSTRINSIC_VARIABLES.update("ES", "ESTACK"); 	
		INSTRINSIC_VARIABLES.update("ET", "ETRAP"); 	
		INSTRINSIC_VARIABLES.update("H", "HOROLOG"); 	
		INSTRINSIC_VARIABLES.update("I", "IO"); 	
		INSTRINSIC_VARIABLES.update("J", "JOB"); 	
		INSTRINSIC_VARIABLES.update("K", "KEY"); 	
		INSTRINSIC_VARIABLES.update("PD", "PDISPLAY"); 	
		INSTRINSIC_VARIABLES.update("P", "PRINCIPAL"); 	
		INSTRINSIC_VARIABLES.update("Q", "QUIT"); 	
		INSTRINSIC_VARIABLES.update("S", "STORAGE"); 	
		INSTRINSIC_VARIABLES.update("ST", "STACK"); 	
		INSTRINSIC_VARIABLES.update("SY", "SYSTEM"); 	
		INSTRINSIC_VARIABLES.update("T", "TEST"); 	
		INSTRINSIC_VARIABLES.update("X", "X"); 	
		INSTRINSIC_VARIABLES.update("Y", "Y"); 	
	}
	
	private MNameWithMnemonic iv;
	
	public IntrinsicVariable(MNameWithMnemonic iv, String identifier) {
		super(identifier);
		this.iv = iv;
	}
	
	protected MNameWithMnemonic getNameWithMnemonic() {
		return this.iv;
	}

	@Override
	public boolean hasArgument() {
		return false;
	}

	private static MNameWithMnemonic getInstrinsicVariable(String identifier) {
		char charAt0 = identifier.charAt(0);
		if ((charAt0 == 'Z') || (charAt0 == 'z')) {
			return new MNameWithMnemonic(identifier, identifier);
		} else {
			return INSTRINSIC_VARIABLES.get(identifier.toUpperCase());
		}			
	}
			
	public static IntrinsicVariable getInstance(String identifier) {
		MNameWithMnemonic iv = IntrinsicVariable.getInstrinsicVariable(identifier.toUpperCase());
		if (iv == null) {
			return null;
		} else {
			return new IntrinsicVariable(iv, identifier);
		}
	}
}