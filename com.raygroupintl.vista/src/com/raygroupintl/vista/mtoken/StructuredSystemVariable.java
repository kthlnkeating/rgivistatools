package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;

public class StructuredSystemVariable extends MSpecial {
	private static final MNameWithMnemonic.Map SSVS = new MNameWithMnemonic.Map();
	static {
		SSVS.update("D", "DEVICE"); 	
		SSVS.update("DI", "DISPLAY"); 	
		SSVS.update("E", "EVENT"); 	
		SSVS.update("G", "GLOBAL"); 	
		SSVS.update("J", "JOB"); 	
		SSVS.update("L", "LOCK"); 	
		SSVS.update("R", "ROUTINE"); 	
		SSVS.update("S", "SYSTEM"); 	
		SSVS.update("W", "WINDOW"); 	
	}
	
	private MNameWithMnemonic ifunc;
	
	public StructuredSystemVariable(MNameWithMnemonic ifunc, String identifier) {
		super(identifier);
		this.ifunc = ifunc;
	}

	protected MNameWithMnemonic getNameWithMnemonic() {
		return this.ifunc;
	}
	
	@Override
	public String getPrefixString() {
		return "^$";
	}

	@Override
	public boolean hasArgument() {
		return true;
	}

	@Override
	public List<MError> getErrors() {
		return null;
	}

	public static StructuredSystemVariable getInstance(String identifier) {
		MNameWithMnemonic ifunc = SSVS.get(identifier.toUpperCase());
		if (ifunc == null) {
			return null;
		} else {
			return new StructuredSystemVariable(ifunc, identifier);
		}
	}
}
