package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.TFSequence;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFSyntaxError;
import com.raygroupintl.bnf.TPair;
import com.raygroupintl.bnf.TokenStore;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;

public class TFIntrinsic extends TFSequence {		
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
		private MNameWithMnemonic mName;
		
		private TIntrinsicVariable(String value, MNameWithMnemonic mName) {
			super(value);
			this.mName = mName;
		}
		
		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return mName;
		}
		
		public static TIntrinsicVariable getInstance(TIdent name, MNameWithMnemonic mName) {
			return new TIntrinsicVariable(name.getStringValue(), mName);
		}
	}

	private static class FunctionInfo {
		private TokenFactory argumentFactory;
		private int minNumArguments;
		private int maxNumArguments;
		
		public FunctionInfo(TokenFactory argumentFactory, int minNumArguments, int maxNumArguments) {
			this.argumentFactory = argumentFactory;
			this.minNumArguments = minNumArguments;
			this.maxNumArguments = maxNumArguments;
		}		

		public int getMinNumArguments() {
			return this.minNumArguments;
		}
		
		public int getMaxNumArguments() {
			return this.maxNumArguments;
		}
		
		public TokenFactory getArgumentFactory() {
			return this.argumentFactory;
		}
	}

	private MNameWithMnemonic.Map variables = new MNameWithMnemonic.Map();
	private MNameWithMnemonic.Map functions;
	private Map<String, FunctionInfo> function_infos;
	private MTFSupply supply;
	
	public TFIntrinsic(MTFSupply supply) {
		this.supply = supply;
	}
	
	public void addVariable(String name) {
		String nameUpperCase = name.toUpperCase();
		this.variables.put(name, new MNameWithMnemonic(nameUpperCase, nameUpperCase));
	}
	
	public void addVariable(String mnemonic, String name) {
		this.variables.update(mnemonic, name); 	
	}
	
	public FunctionInfo addFunction(TokenFactory argumentFactory, String mnemonic, String name, int minNumArguments, int maxNumArguments) {
		FunctionInfo fi = new FunctionInfo(argumentFactory, minNumArguments, maxNumArguments);
		if (this.functions == null) {
			this.functions = new MNameWithMnemonic.Map();	
		}		
		this.functions.update(mnemonic, name);
		if (this.function_infos == null) {
			this.function_infos = new HashMap<String, FunctionInfo>();	
		}
		this.function_infos.put(mnemonic, fi);
		return fi;
	}
		
	public FunctionInfo addFunction(TokenFactory argumentFactory, String name, int minNumArguments, int maxNumArguments) {
		return this.addFunction(argumentFactory, name, name, minNumArguments, maxNumArguments);
	}
		
	public FunctionInfo addFunction(TokenFactory argumentFactory, String name) {
		return this.addFunction(argumentFactory, name, 1, Integer.MAX_VALUE);
	}

	private static class TIntrinsicFunctionName extends TIntrinsicName {
		private MNameWithMnemonic mName;
		
		public TIntrinsicFunctionName(String name, MNameWithMnemonic mnemonicNName) {
			super(name);
			this.mName = mnemonicNName;
		}
		
		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return this.mName;
		}		
	}
	
	private static class TIntrinsicFunction extends TPair {
		private TIntrinsicFunction(TIntrinsicFunctionName name, TInParantheses argument) {
			super(name, argument);
		}

		public static TIntrinsicFunction getInstance(TIdent name, TInParantheses argument, MNameWithMnemonic mnemonicNName) {
			TIntrinsicFunctionName functionName = new TIntrinsicFunctionName(name.getStringValue(), mnemonicNName);
			return new TIntrinsicFunction(functionName, argument);
		}
	}
	
	private static String getFoundIntrinsicName(TokenStore tokens) {
		Token token0 = tokens.get(0);
		String name = ((token0 instanceof TArray) ? (TArray) token0 : (TPair)token0).get(1).getStringValue().toUpperCase();
		return name;
	}
	
	@Override
	protected int getExpectedTokenCount() {
		return 4;
	}
	
	@Override
	protected TokenFactory getTokenFactory(int i, TokenStore foundTokens) {
		switch (i) {
			case 0: {
				return TFIntrinsic.this.supply.intrinsicname;
			}
			case 1:
				return TFIntrinsic.this.supply.lpar;
			case 2: {
				String name = getFoundIntrinsicName(foundTokens);
				MNameWithMnemonic mName = TFIntrinsic.this.functions.get(name);
				if (mName == null) {
					return new TFSyntaxError(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION);
				}
				String mnemonic = mName.getMnemonic();
				FunctionInfo info = TFIntrinsic.this.function_infos.get(mnemonic);
				TokenFactory argumentFactory = info.getArgumentFactory();
				return argumentFactory;
			}
			case 3:
				return TFIntrinsic.this.supply.rpar;
			default:
				return null;						
		}
	}	

	@Override
	protected ValidateResult validateNull(int seqIndex, int lineIndex, TokenStore foundTokens) throws SyntaxErrorException{
		if (seqIndex == 0) {
			return ValidateResult.NULL_RESULT;
		} else if (seqIndex == 1) {
			String name = getFoundIntrinsicName(foundTokens);
			if (this.variables.containsKey(name)) {
				return ValidateResult.BREAK;
			} else {
				throw new SyntaxErrorException(MError.ERR_UNKNOWN_INTRINSIC_VARIABLE, lineIndex, foundTokens);
			}
		} else if (seqIndex == 2) {
			String name = getFoundIntrinsicName(foundTokens);
			MNameWithMnemonic mName = TFIntrinsic.this.functions.get(name);
			String mnemonic = mName.getMnemonic();
			FunctionInfo info = TFIntrinsic.this.function_infos.get(mnemonic);
			if (info.getMinNumArguments() > 0) {
				throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX, lineIndex, foundTokens);				
			} else {
				return ValidateResult.CONTINUE;
			}
		} else {
			throw new SyntaxErrorException(MError.ERR_UNMATCHED_PARANTHESIS, lineIndex, foundTokens);
		}
	}
	
	@Override
	protected void validateEnd(int seqIndex, int lineIndex, TokenStore foundTokens) throws SyntaxErrorException {
		if (seqIndex > 0) {
			throw new SyntaxErrorException( MError.ERR_UNMATCHED_PARANTHESIS, lineIndex, foundTokens);
		}
	}
	
	@Override
	protected Token getToken(String line, int fromIndex, TokenStore foundTokens) {
		TArray token0 = (TArray) foundTokens.get(0);
		if (token0.get(2) == null) {		
			TIdent name = (TIdent) token0.get(1);		
			if (foundTokens.get(1) == null) {
				return TIntrinsicVariable.getInstance(name, this.variables.get(name.getStringValue().toUpperCase()));			
			} else {
				Token t = foundTokens.get(2);
				if (t == null) {
					t = new TList();
				}
				return TIntrinsicFunction.getInstance(name, new TInParantheses(t), this.functions.get(name.getStringValue().toUpperCase()));
			}
		} else {
			return new TArray(foundTokens.toArray());
		}
	}
}
