package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;

public class TIntrinsicVar extends TIntrinsicName {
	private static final MNameWithMnemonic.Map INTRINSIC_VARIABLES = new MNameWithMnemonic.Map();
	static {
		INTRINSIC_VARIABLES.update("D", "DEVICE"); 	
		INTRINSIC_VARIABLES.update("EC", "ECODE"); 	
		INTRINSIC_VARIABLES.update("ES", "ESTACK"); 	
		INTRINSIC_VARIABLES.update("ET", "ETRAP"); 	
		INTRINSIC_VARIABLES.update("H", "HOROLOG"); 	
		INTRINSIC_VARIABLES.update("I", "IO"); 	
		INTRINSIC_VARIABLES.update("J", "JOB"); 	
		INTRINSIC_VARIABLES.update("K", "KEY"); 	
		INTRINSIC_VARIABLES.update("PD", "PDISPLAY"); 	
		INTRINSIC_VARIABLES.update("P", "PRINCIPAL"); 	
		INTRINSIC_VARIABLES.update("Q", "QUIT"); 	
		INTRINSIC_VARIABLES.update("S", "STORAGE"); 	
		INTRINSIC_VARIABLES.update("ST", "STACK"); 	
		INTRINSIC_VARIABLES.update("SY", "SYSTEM"); 	
		INTRINSIC_VARIABLES.update("T", "TEST"); 	
		INTRINSIC_VARIABLES.update("X", "X"); 	
		INTRINSIC_VARIABLES.update("Y", "Y"); 	
	}

	private TIntrinsicVar(String value) {
		super(value);
	}

	@Override
	public List<MError> getErrors() {
		String value = this.getIdentier();
		if (INTRINSIC_VARIABLES.get(value) == null) {
			return Arrays.asList(new MError[]{new MError(MError.ERR_UNKNOWN_INTRINSIC_VARIABLE)});
		} else {
			return null;
		}
	}
	
	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		String value = this.getIdentier();
		MNameWithMnemonic name = INTRINSIC_VARIABLES.get(value);
		return name;
	}
	
	public static TIntrinsicVar getInstance(TIdent token) {
		String v = token.getStringValue();
		return new TIntrinsicVar(v);		
	}
}
