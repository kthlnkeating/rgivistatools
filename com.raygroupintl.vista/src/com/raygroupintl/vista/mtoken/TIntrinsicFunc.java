package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TPair;

public class TIntrinsicFunc extends TPair {
	private static class IntrinsicFunction extends MNameWithMnemonic {
		private int minNumArguments;
		private int maxNumArguments;
		
		public IntrinsicFunction(String mnemonic, String name, int minNumArguments, int maxNumArguments) {
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

	@SuppressWarnings("serial")
	private static class IntrinsicFunctions extends HashMap<String, IntrinsicFunction> {
		public void update(String mnemonic, String name, int minNumArguments, int maxNumArguments) {
			IntrinsicFunction ifunc = new IntrinsicFunction(mnemonic, name, minNumArguments, maxNumArguments);
			this.put(mnemonic, ifunc);
			if (! mnemonic.equals(name)) {
				this.put(name, ifunc);			
			}
		}
	}
	
	private static final IntrinsicFunctions INTRINSIC_FUNCTIONS = new IntrinsicFunctions();
	static {
		INTRINSIC_FUNCTIONS.update("A", "ASCII", 1, 2); 	
		INTRINSIC_FUNCTIONS.update("C", "CHAR", 1, 999); 	
		INTRINSIC_FUNCTIONS.update("D", "DATA", 1, 1); 	
		INTRINSIC_FUNCTIONS.update("E", "EXTRACT", 1, 3); 	
		INTRINSIC_FUNCTIONS.update("F", "FIND", 2, 3); 	
		INTRINSIC_FUNCTIONS.update("G", "GET", 1, 2); 	
		INTRINSIC_FUNCTIONS.update("J", "JUSTIFY", 2, 3); 	
		INTRINSIC_FUNCTIONS.update("L", "LENGTH", 1, 2); 	
		INTRINSIC_FUNCTIONS.update("O", "ORDER", 1, 2); 	
		INTRINSIC_FUNCTIONS.update("P", "PIECE", 2, 4); 	
		INTRINSIC_FUNCTIONS.update("Q", "QUERY", 1, 1); 	
		INTRINSIC_FUNCTIONS.update("R", "RANDOM", 1, 1); 	
		INTRINSIC_FUNCTIONS.update("S", "SELECT", 1, 999); 	
		INTRINSIC_FUNCTIONS.update("T", "TEXT", 1, 1); 	
		INTRINSIC_FUNCTIONS.update("V", "VIEW",1, 999); 	
		INTRINSIC_FUNCTIONS.update("FN", "FNUMBER", 2, 3); 	
		INTRINSIC_FUNCTIONS.update("N", "NEXT", 1, 2); 	
		INTRINSIC_FUNCTIONS.update("NA", "NAME", 1, 2); 	
		INTRINSIC_FUNCTIONS.update("Q", "QUERY", 1, 2); 	
		INTRINSIC_FUNCTIONS.update("QL", "QLENGTH", 1, 2); 	
		INTRINSIC_FUNCTIONS.update("QS", "QSUBSCRIPT", 1, 3);
		INTRINSIC_FUNCTIONS.update("RE", "REVERSE", 1, 1);
		INTRINSIC_FUNCTIONS.update("S", "SELECT", 1, 2);
		INTRINSIC_FUNCTIONS.update("ST", "STACK", 1, 2);
		INTRINSIC_FUNCTIONS.update("TR", "TRANSLATE", 1, 3);
		INTRINSIC_FUNCTIONS.update("WFONT", "WFONT", 4, 4);
		INTRINSIC_FUNCTIONS.update("WTFIT", "WTFIT", 6, 6);
		INTRINSIC_FUNCTIONS.update("WTWIDTH", "WTWIDTH", 5, 5);
	}
	
	private static class TIFName extends TIntrinsicName {
		public TIFName(String value) {
			super(value);
		}
		
		@Override
		public List<MError> getErrors() {
			String value = this.getIdentier();
			if (INTRINSIC_FUNCTIONS.get(value) == null) {
				return Arrays.asList(new MError[]{new MError(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION)});
			} else {
				return null;
			}
		}
		
		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			String value = this.getIdentier();
			MNameWithMnemonic name = INTRINSIC_FUNCTIONS.get(value);
			return name;
		}		
	}
	
	private TIntrinsicFunc(TIFName func, TActualList argument) {
		super(func, argument);
	}

	public static TIntrinsicFunc getInstance(TIdent name, TActualList argument) {
		TIFName v = new TIFName(name.getStringValue());
		return new TIntrinsicFunc(v, argument);
	}
}
