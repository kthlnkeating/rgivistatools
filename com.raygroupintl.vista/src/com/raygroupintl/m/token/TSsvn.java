package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.m.struct.MNameWithMnemonic;
import com.raygroupintl.parser.Token;

public class TSsvn extends MTSequence {
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
	
	public TSsvn(List<Token> tokens) {
		super(tokens);
	}		
}
