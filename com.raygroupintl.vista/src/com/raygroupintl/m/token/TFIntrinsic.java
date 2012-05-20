package com.raygroupintl.m.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.MNameWithMnemonic;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.TokenStore;

public class TFIntrinsic extends TFSequence {		
	private static class TIntrinsicVariable extends MTSequence {
		private TIntrinsicVariable(List<Token> tokens) {
			super(tokens);
		}
	}

	private static class TIntrinsicFunction extends MTSequence {
		private TIntrinsicFunction(List<Token> tokens) {
			super(tokens);
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

	private static String getFoundIntrinsicName(TokenStore tokens) {
		Token token0 = tokens.get(0);
		String name = ((MTSequence) token0).get(1).getStringValue().toUpperCase();
		return name;
	}
	
	@Override
	protected int getExpectedTokenCount() {
		return 4;
	}
	
	@Override
	protected TokenFactory getTokenFactory(int i, TokenStore foundTokens)  throws SyntaxErrorException {
		switch (i) {
			case 0: 
				return TFIntrinsic.this.supply.intrinsicname;
			case 1:
				return TFIntrinsic.this.supply.lpar;
			case 2: {
				String name = getFoundIntrinsicName(foundTokens);
				MNameWithMnemonic mName = TFIntrinsic.this.functions.get(name);
				if (mName == null) {
					throw new SyntaxErrorException(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION, foundTokens);
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
	protected ValidateResult validateNull(int seqIndex, TokenStore foundTokens) throws SyntaxErrorException {
		if (seqIndex == 0) {
			return ValidateResult.NULL_RESULT;
		} else if (seqIndex == 1) {
			String name = getFoundIntrinsicName(foundTokens);
			if (this.variables.containsKey(name)) {
				return ValidateResult.BREAK;
			} else {
				throw new SyntaxErrorException(MError.ERR_UNKNOWN_INTRINSIC_VARIABLE, foundTokens);
			}
		} else if (seqIndex == 2) {
			String name = getFoundIntrinsicName(foundTokens);
			MNameWithMnemonic mName = TFIntrinsic.this.functions.get(name);
			String mnemonic = mName.getMnemonic();
			FunctionInfo info = TFIntrinsic.this.function_infos.get(mnemonic);
			if (info.getMinNumArguments() > 0) {
				throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX, foundTokens);				
			} else {
				return ValidateResult.CONTINUE;
			}
		} else {
			throw new SyntaxErrorException(MError.ERR_UNMATCHED_PARANTHESIS, foundTokens);
		}
	}
	
	@Override
	protected void validateEnd(int seqIndex, TokenStore foundTokens) throws SyntaxErrorException {
		if (seqIndex > 0) {
			throw new SyntaxErrorException( MError.ERR_UNMATCHED_PARANTHESIS, foundTokens);
		}
	}
	
	@Override
	protected Token getToken(TokenStore foundTokens) {
		MTSequence token0 = (MTSequence) foundTokens.get(0);
		if (token0.get(2) == null) {		
			if (foundTokens.get(1) == null) {
				List<Token> result = new ArrayList<Token>(2);
				for (int i=0; i<2; ++i) result.add(foundTokens.get(i));
				return new TIntrinsicVariable(result);
			} else {
				List<Token> result = new ArrayList<Token>(3);
				for (int i=0; i<4; ++i) result.add(foundTokens.get(i));
				return new TIntrinsicFunction(result);
			}
		} else {
			return new MTSequence(foundTokens.toList());
		}
	}
}
