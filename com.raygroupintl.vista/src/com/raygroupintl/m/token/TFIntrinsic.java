package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.MNameWithMnemonic;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.TokenFactorySupply;

public class TFIntrinsic extends TokenFactorySupply {		
	public static class TIntrinsicVariable extends MSequence {
		public TIntrinsicVariable(Token token) {
			super(token);
		}
	}

	private static class TIntrinsicFunction extends MSequence {
		private TIntrinsicFunction(Token token) {
			super(token);
		}
	}
	
	private static class FunctionInfo {
		private TokenFactory argumentFactory;
		private int minNumArguments;
		//private int maxNumArguments;
		
		public FunctionInfo(TokenFactory argumentFactory, int minNumArguments, int maxNumArguments) {
			this.argumentFactory = argumentFactory;
			this.minNumArguments = minNumArguments;
			//this.maxNumArguments = maxNumArguments;
		}		

		public int getMinNumArguments() {
			return this.minNumArguments;
		}
		
		//public int getMaxNumArguments() {
		//	return this.maxNumArguments;
		//}
		
		public TokenFactory getArgumentFactory() {
			return this.argumentFactory;
		}
	}

	private MNameWithMnemonic.Map variables = new MNameWithMnemonic.Map();
	private MNameWithMnemonic.Map functions;
	private Map<String, FunctionInfo> function_infos;
	private MTFSupply supply;
	
	public TFIntrinsic(String name, MTFSupply supply) {
		super(name);
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

	private class TFIntrinsicRest extends TFSequence {	
		private boolean nullAllowed;
		//private FunctionInfo info;
		
		public TFIntrinsicRest(FunctionInfo info, boolean nullAllowed, String name, TokenFactory... factories) {
			super(name, factories);
			this.nullAllowed = nullAllowed;
			//this.info = info;
		}
		
/*		@Override
		protected ValidateResult validateNext(int seqIndex, TokenStore foundTokens, Token nextToken) throws SyntaxErrorException {
			if (seqIndex == 0) {
				TList list = (TList) nextToken;
				int n = list.size();
				if (n < this.info.getMinNumArguments()) {
					throw new SyntaxErrorException(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION, foundTokens);				
				}
				if (n > this.info.getMaxNumArguments()) {
					throw new SyntaxErrorException(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION, foundTokens);				
				}
			}
			return ValidateResult.CONTINUE;
		}
*/
		@Override
		protected ValidateResult validateNull(int seqIndex, TSequence foundTokens, boolean noException) throws SyntaxErrorException {
			if (seqIndex == 0 && this.nullAllowed) {
				return ValidateResult.CONTINUE;
			}
			if (noException) return ValidateResult.NULL_RESULT;
			throw new SyntaxErrorException();
		}
	}
	
	@Override
	public TokenFactory getSupplyTokenFactory() {
		TFSequence result = new TFSequence("instrinsic.name", this.supply.intrinsicname, this.supply.lpar);
		result.setRequiredFlags(true, false);
		result.setTargetType(TIntrinsicVariable.class);
		return result;		
	}
	
	@Override
	public TokenFactory getNextTokenFactory(Token token) throws SyntaxErrorException {
		String name = token.toValue().toString();
		int length = name.length();
		if (name.charAt(length-1) =='(') {
			String base =name.substring(1, length-1);
			String[] mna = base.split("\\.");
			String mn = mna[0].toUpperCase();			
			MNameWithMnemonic mName = TFIntrinsic.this.functions.get(mn);
			if (mName == null) {
				throw new SyntaxErrorException(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION);
			}
			String mnemonic = mName.getMnemonic();
			FunctionInfo info = TFIntrinsic.this.function_infos.get(mnemonic);
			TokenFactory argumentFactory = info.getArgumentFactory();
			TFSequence result = new TFIntrinsicRest(info, info.getMinNumArguments() == 0, "instrinsic.name", argumentFactory, this.supply.rpar);
			result.setRequiredFlags(info.getMinNumArguments() > 0, true);
			return result;
		} else {
			String base =name.substring(1);
			MNameWithMnemonic mName = TFIntrinsic.this.variables.get(base);
			if (mName == null) {
				throw new SyntaxErrorException(MError.ERR_UNKNOWN_INTRINSIC_VARIABLE);
			}
			return null;
		}
	}

	@Override
	public Token getToken(Token supplyToken, Token nextToken) {
		MSequence result = new MSequence(2);
		result.addToken(supplyToken);
		result.addToken(nextToken);
		return new TIntrinsicFunction(result);
	}

}
