package com.raygroupintl.vista.mtoken;

import java.util.HashMap;

import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.struct.MSpecialFunction;

public class IntrinsicFunction extends Intrinsic {
	@SuppressWarnings("serial")
	private static class IntrinsicFunctions extends HashMap<String, MSpecialFunction> {
		public void update(String mnemonic, String name, int minNumArguments, int maxNumArguments) {
			MSpecialFunction ifunc = new MSpecialFunction(mnemonic, name, minNumArguments, maxNumArguments);
			this.put(mnemonic, ifunc);
			if (! mnemonic.equals(name)) {
				this.put(name, ifunc);			
			}
		}
	}
	
	private static final IntrinsicFunctions INSTRINSIC_FUNCTIONS = new IntrinsicFunctions();
	static {
		INSTRINSIC_FUNCTIONS.update("A", "ASCII", 1, 2); 	
		INSTRINSIC_FUNCTIONS.update("C", "CHAR", 1, 999); 	
		INSTRINSIC_FUNCTIONS.update("D", "DATA", 1, 1); 	
		INSTRINSIC_FUNCTIONS.update("E", "EXTRACT", 1, 3); 	
		INSTRINSIC_FUNCTIONS.update("F", "FIND", 2, 3); 	
		INSTRINSIC_FUNCTIONS.update("G", "GET", 1, 2); 	
		INSTRINSIC_FUNCTIONS.update("J", "JUSTIFY", 2, 3); 	
		INSTRINSIC_FUNCTIONS.update("L", "LENGTH", 1, 2); 	
		INSTRINSIC_FUNCTIONS.update("O", "ORDER", 1, 2); 	
		INSTRINSIC_FUNCTIONS.update("P", "PIECE", 2, 4); 	
		INSTRINSIC_FUNCTIONS.update("Q", "QUERY", 1, 1); 	
		INSTRINSIC_FUNCTIONS.update("R", "RANDOM", 1, 1); 	
		INSTRINSIC_FUNCTIONS.update("S", "SELECT", 1, 999); 	
		INSTRINSIC_FUNCTIONS.update("T", "TEXT", 1, 1); 	
		INSTRINSIC_FUNCTIONS.update("V", "VIEW",1, 999); 	
		INSTRINSIC_FUNCTIONS.update("FN", "FNUMBER", 2, 3); 	
		INSTRINSIC_FUNCTIONS.update("N", "NEXT", 1, 2); 	
		INSTRINSIC_FUNCTIONS.update("NA", "NAME", 1, 2); 	
		INSTRINSIC_FUNCTIONS.update("Q", "QUERY", 1, 2); 	
		INSTRINSIC_FUNCTIONS.update("QL", "QLENGTH", 1, 2); 	
		INSTRINSIC_FUNCTIONS.update("QS", "QSUBSCRIPT", 1, 3);
		INSTRINSIC_FUNCTIONS.update("RE", "REVERSE", 1, 1);
		INSTRINSIC_FUNCTIONS.update("S", "SELECT", 1, 2);
		INSTRINSIC_FUNCTIONS.update("ST", "STACK", 1, 2);
		INSTRINSIC_FUNCTIONS.update("TR", "TRANSLATE", 1, 3);
		INSTRINSIC_FUNCTIONS.update("WFONT", "WFONT", 4, 4);
		INSTRINSIC_FUNCTIONS.update("WTFIT", "WTFIT", 6, 6);
		INSTRINSIC_FUNCTIONS.update("WTWIDTH", "WTWIDTH", 5, 5);
	}
	
	private MSpecialFunction ifunc;
	
	public IntrinsicFunction(MSpecialFunction ifunc, String identifier) {
		super(identifier);
		this.ifunc = ifunc;
	}
	
	protected MNameWithMnemonic getNameWithMnemonic() {
		return this.ifunc;
	}

	@Override
	public boolean hasArgument() {
		return true;
	}

	private static MSpecialFunction getInstrinsicFunction(String identifier) {
		char charAt0 = identifier.charAt(0);
		if ((charAt0 == 'Z') || (charAt0 == 'z')) {
			return new MSpecialFunction(identifier, identifier, 0, 999);
		} else {
			return INSTRINSIC_FUNCTIONS.get(identifier.toUpperCase());
		}			
	}
	
	public static IntrinsicFunction getInstance(String identifier) {
		MSpecialFunction ifunc = IntrinsicFunction.getInstrinsicFunction(identifier.toUpperCase());
		if (ifunc == null) {
			return null;
		} else {
			return new IntrinsicFunction(ifunc, identifier);
		}
	}
}	