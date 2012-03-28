package com.raygroupintl.vista.mtoken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TArray;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialBase;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TFSyntaxError;
import com.raygroupintl.vista.token.TPair;

public class TFIntrinsic extends TFSerialBase {	
	private static MNameWithMnemonic.Map INTRINSIC_VARIABLES = new MNameWithMnemonic.Map();
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
	
	public static void addVariable(String name) {
		String nameUpperCase = name.toUpperCase();
		INTRINSIC_VARIABLES.put(name, new MNameWithMnemonic(nameUpperCase, nameUpperCase));
	}
	
	private static abstract class TIntrinsicName extends TKeyword {
		public TIntrinsicName(String value) {
			super(value);
		}
		
		@Override
		public String getStringValue() {
			return "$" + super.getStringValue();
		}

		@Override
		public int getStringSize() {
			return 1 + super.getStringSize();
		}	

		@Override
		public List<MError> getErrors() {
			return null;  // All errors during parsing
		}
	}

	private static class TIntrinsicVariable extends TIntrinsicName {
		private TIntrinsicVariable(String value) {
			super(value);
		}
	
		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			String value = this.getIdentier();
			MNameWithMnemonic name = INTRINSIC_VARIABLES.get(value);
			return name;
		}
		
		public static TIntrinsicVariable getInstance(TIdent name) {
			return new TIntrinsicVariable(name.getStringValue());
		}
	}

	private static MNameWithMnemonic.Map INTRINSIC_FUNCTIONS = new MNameWithMnemonic.Map();
	static {
		INTRINSIC_FUNCTIONS.update("A", "ASCII"); 	
		INTRINSIC_FUNCTIONS.update("C", "CHAR"); 	
		INTRINSIC_FUNCTIONS.update("D", "DATA"); 	
		INTRINSIC_FUNCTIONS.update("E", "EXTRACT"); 	
		INTRINSIC_FUNCTIONS.update("F", "FIND"); 	
		INTRINSIC_FUNCTIONS.update("G", "GET"); 	
		INTRINSIC_FUNCTIONS.update("I", "INCREMENT"); 	
		INTRINSIC_FUNCTIONS.update("J", "JUSTIFY"); 	
		INTRINSIC_FUNCTIONS.update("L", "LENGTH"); 	
		INTRINSIC_FUNCTIONS.update("O", "ORDER"); 	
		INTRINSIC_FUNCTIONS.update("P", "PIECE"); 	
		INTRINSIC_FUNCTIONS.update("Q", "QUERY"); 	
		INTRINSIC_FUNCTIONS.update("R", "RANDOM"); 	
		INTRINSIC_FUNCTIONS.update("S", "SELECT"); 	
		INTRINSIC_FUNCTIONS.update("T", "TEXT"); 	
		INTRINSIC_FUNCTIONS.update("V", "VIEW"); 	
		INTRINSIC_FUNCTIONS.update("FN", "FNUMBER"); 	
		INTRINSIC_FUNCTIONS.update("N", "NEXT"); 	
		INTRINSIC_FUNCTIONS.update("NA", "NAME"); 	
		INTRINSIC_FUNCTIONS.update("Q", "QUERY"); 	
		INTRINSIC_FUNCTIONS.update("QL", "QLENGTH"); 	
		INTRINSIC_FUNCTIONS.update("QS", "QSUBSCRIPT");
		INTRINSIC_FUNCTIONS.update("RE", "REVERSE");
		INTRINSIC_FUNCTIONS.update("ST", "STACK");
		INTRINSIC_FUNCTIONS.update("TR", "TRANSLATE");
		INTRINSIC_FUNCTIONS.update("WFONT", "WFONT");
		INTRINSIC_FUNCTIONS.update("WTFIT", "WTFIT");
		INTRINSIC_FUNCTIONS.update("WTWIDTH", "WTWIDTH");
	}

	private static class FunctionInfo {
		private ITokenFactory argumentFactory;
		private int minNumArguments;
		private int maxNumArguments;
		
		public FunctionInfo(int minNumArguments, int maxNumArguments) {
			this.minNumArguments = minNumArguments;
			this.maxNumArguments = maxNumArguments;

		}		

		public void setArgumentFactory(ITokenFactory f) {
			this.argumentFactory = f;
		}
		
		public int getMinNumArguments() {
			return this.minNumArguments;
		}
		
		public int getMaxNumArguments() {
			return this.maxNumArguments;
		}
		
		public ITokenFactory getArgumentFactory() {
			if (this.argumentFactory == null) {
				return TFDelimitedList.getInstance(TFExpr.getInstance(), ',');
			} else {
				return this.argumentFactory;
			}
		}
	}

	private static Map<String, FunctionInfo> FUNCTION_INFOS = new HashMap<String, FunctionInfo>();

	public static FunctionInfo addFunction(String name, int minNumArguments, int maxNumArguments) {
		FunctionInfo fi = new FunctionInfo(minNumArguments, maxNumArguments);
		INTRINSIC_FUNCTIONS.update(name, name);
		FUNCTION_INFOS.put(name, fi);
		return fi;
	}
		
	public static FunctionInfo addFunction(String name) {
		return addFunction(name, 1, Integer.MAX_VALUE);
	}

	static {
		addFunction("A", 1, 2); 	
		addFunction("C", 1, 999); 	
		addFunction("D", 1, 1); 	
		addFunction("E", 1, 3); 	
		addFunction("F", 2, 3); 	
		addFunction("G", 1, 2); 	
		addFunction("J", 2, 3); 	
		addFunction("L", 1, 2); 		
		FunctionInfo o = addFunction("O", 1, 2); 	
		o.setArgumentFactory(new TFSerialRO() {
			@Override
			protected ITokenFactory getRequired() {
				return TFGlvn.getInstance();
			}
			
			@Override
			protected ITokenFactory getOptional() {
				return TFAllRequired.getInstance(TFConstChar.getInstance(','), TFExpr.getInstance());
			}
		});		
		addFunction("P", 2, 4); 	
		addFunction("Q", 1, 1); 	
		addFunction("R", 1, 1); 	
		addFunction("RE", 1, 1);		
		FunctionInfo s = addFunction("S", 1, 999);
		s.setArgumentFactory(new TFCommaDelimitedList() {			
			@Override
			protected ITokenFactory getElementFactory() {
				ITokenFactory expr = TFExpr.getInstance();
				return TFAllRequired.getInstance(expr, TFConstChar.getInstance(':'), expr);
			}
		});
		FunctionInfo t = addFunction("T", 1, 1); 
		t.setArgumentFactory(TFGotoArgument.getInstance(true));			
		addFunction("V", 1, 999); 	
		addFunction("FN", 2, 3); 	
		addFunction("N", 1, 2); 	
		addFunction("NA", 1, 2); 	
		addFunction("Q", 1, 2); 	
		addFunction("QL", 1, 2); 	
		addFunction("QS", 1, 3);
		addFunction("ST", 1, 2);
		addFunction("TR", 1, 3);
		addFunction("WFONT", 4, 4);
		addFunction("WTFIT", 6, 6);
		addFunction("WTWIDTH", 5, 5);
	}

	private static class TIntrinsicFunctionName extends TIntrinsicName {
		public TIntrinsicFunctionName(String name) {
			super(name);
		}
		
		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			String value = this.getIdentier();
			MNameWithMnemonic name = INTRINSIC_FUNCTIONS.get(value);
			return name;
		}		
	}
	
	private static class TIntrinsicFunction extends TPair {
		private TIntrinsicFunction(TIntrinsicFunctionName name, TInParantheses argument) {
			super(name, argument);
		}

		public static TIntrinsicFunction getInstance(TIdent name, TInParantheses argument) {
			TIntrinsicFunctionName functionName = new TIntrinsicFunctionName(name.getStringValue());
			return new TIntrinsicFunction(functionName, argument);
		}
	}
	
	private static String getFoundIntrinsicName(IToken[] tokens) {
		String name = ((TArray) tokens[0]).get(1).getStringValue().toUpperCase();
		return name;
	}
	
	@Override
	protected ITokenFactorySupply getFactorySupply() {
		return new ITokenFactorySupply() {			
			@Override
			public int getCount() {
				return 4;
			}
			
			@Override
			public ITokenFactory get(IToken[] previousTokens) {
				int n = previousTokens.length;
				switch (n) {
					case 0:
						return TFAllRequired.getInstance(TFConstChar.getInstance('$'), TFIdent.getInstance());
					case 1:
						return TFConstChar.getInstance('(');
					case 2: {
						String name = getFoundIntrinsicName(previousTokens);
						MNameWithMnemonic mName = INTRINSIC_FUNCTIONS.get(name);
						if (mName == null) {
							return TFSyntaxError.getInstance(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION);
						}
						String mnemonic = mName.getMnemonic();
						FunctionInfo info = FUNCTION_INFOS.get(mnemonic);
						ITokenFactory argumentFactory = info.getArgumentFactory();
						return argumentFactory;
					}
					case 3:
						return TFConstChar.getInstance(')');
					default:
						return null;						
				}
			}
		};
	}

	@Override
	protected int getCodeNextIsNull(IToken[] foundTokens) {
		int n = foundTokens.length;
		if (n == 0) {
			return RETURN_NULL;
		} else if (n == 1) {
			String name = getFoundIntrinsicName(foundTokens);
			if (INTRINSIC_VARIABLES.containsKey(name)) {
				return RETURN_TOKEN;
			} else {
				return MError.ERR_UNKNOWN_INTRINSIC_VARIABLE;	
			}
		} else {
			return MError.ERR_UNMATCHED_PARANTHESIS;
		}
	}
	
	@Override
	protected int getCodeStringEnds(IToken[] foundTokens) {
		int n = foundTokens.length;
		if (n == 1) {
			return 0;
		} else {
			return MError.ERR_UNMATCHED_PARANTHESIS;	
		}
	}
	
	@Override
	protected IToken getToken(IToken[] foundTokens) {
		TIdent name = (TIdent) ((TArray) foundTokens[0]).get(1);		
		if (foundTokens[1] == null) {
			return TIntrinsicVariable.getInstance(name);			
		} else {
			return TIntrinsicFunction.getInstance(name, new TInParantheses(foundTokens[2]));
		}		
	}
	
	public static TFIntrinsic getInstance() {
		return new TFIntrinsic();
	}
}
