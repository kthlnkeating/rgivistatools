//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.m.parsetree.IntrinsicFunction;
import com.raygroupintl.m.parsetree.IntrinsicVariable;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.MNameWithMnemonic;
import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.TokenFactorySupply;

public class TFIntrinsic extends TokenFactorySupply {		
	public static class TIntrinsicVariable extends MSequence {
		public TIntrinsicVariable(int length) {
			super(length);
		}
		
		public TIntrinsicVariable(SequenceOfTokens tokens) {
			super(tokens);
		}
		
		@Override
		public Node getNode() {
			TextPiece name = this.getSubNodeToken(0).getSubNodeToken(1).toValue();
			return new IntrinsicVariable(name.toString());
		}
	}

	private static class TIntrinsicFunction extends MSequence {
		private TIntrinsicFunction(int length) {
			super(length);
		}
		
		private TIntrinsicFunction(SequenceOfTokens tokens) {
			super(tokens);
		}
		
		@Override
		public Node getNode() {
			TextPiece name = ((MToken) this.getToken(0)).getSubNodeToken(0).getSubNodeToken(1).toValue();
			MToken arguments = ((MToken) this.getToken(1)).getSubNodeToken(0);	
			return new IntrinsicFunction(name.toString(), arguments == null ? null : arguments.getNode());
		}

	}
	
	private static class FunctionInfo {
		private TokenFactory argumentFactory;
		//private String mnemonic;
		//private String name;
		private int minNumArguments;
		//private int maxNumArguments;
		
		public FunctionInfo(String mnemonic, String name, TokenFactory argumentFactory, int minNumArguments, int maxNumArguments) {
			//this.mnemonic = mnemonic;
			//this.name = name;
			this.argumentFactory = argumentFactory;
			this.minNumArguments = minNumArguments;
			//this.maxNumArguments = maxNumArguments;
		}		

		//public String getMnemonic() {
		//	return this.mnemonic;
		//}
		
		//public String getName() {
		//	return this.name;
		//}

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
	private Map<String, FunctionInfo> functions;
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
		FunctionInfo fi = new FunctionInfo(mnemonic, name, argumentFactory, minNumArguments, maxNumArguments);
		if (this.functions == null) {
			this.functions = new HashMap<String, FunctionInfo>();	
		}
		this.functions.put(mnemonic, fi);
		if (! mnemonic.equals(name)) {
			this.functions.put(name, fi);
		}
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
		
		public TFIntrinsicRest(FunctionInfo info, boolean nullAllowed, String name) {
			super(name, 2);
			this.nullAllowed = nullAllowed;
		}
		
		@Override
		protected ValidateResult validateNull(int seqIndex, SequenceOfTokens foundTokens, boolean noException) throws SyntaxErrorException {
			if (seqIndex == 0 && this.nullAllowed) {
				return ValidateResult.CONTINUE;
			}
			if (noException) return ValidateResult.NULL_RESULT;
			throw new SyntaxErrorException();
		}
	}
	
	@Override
	public TokenFactory getSupplyTokenFactory() {
		TFSequence result = new TFSequence("instrinsic.name", 2);
		result.add(this.supply.intrinsicname, true);
		result.add(this.supply.lpar, false);
		try {
			result.setSequenceTargetType(TIntrinsicVariable.class.getConstructor(SequenceOfTokens.class));
		} catch (Throwable t) {
			return null;
		}
		return result;		
	}
	
	@Override
	public TokenFactory getNextTokenFactory(Token token) throws SyntaxErrorException {
		MToken nameNLPar = (MToken) token;
		MToken nameTokens = nameNLPar.getSubNodeToken(0);
		String name = nameTokens.getSubNodeToken(1).toValue().toString();
		if ((nameNLPar.getNumSubNodes() > 1) && (nameNLPar.getSubNodeToken(1) != null)) {
			String mn = name.toUpperCase();			
			FunctionInfo info = TFIntrinsic.this.functions.get(mn);
			if (info == null) {
				throw new SyntaxErrorException(MError.ERR_UNKNOWN_INTRINSIC_FUNCTION);
			}
			TokenFactory argumentFactory = info.getArgumentFactory();
			TFSequence result = new TFIntrinsicRest(info, info.getMinNumArguments() == 0, "instrinsic.name");
			result.add(argumentFactory, info.getMinNumArguments() > 0);
			result.add(this.supply.rpar, true);
			return result;
		} else {
			if (TFIntrinsic.this.variables.containsKey(name)) {
				return null;
			} else {
				throw new SyntaxErrorException(MError.ERR_UNKNOWN_INTRINSIC_VARIABLE);
			}
		}
	}

	@Override
	public Token getToken(Token supplyToken, Token nextToken) {
		TIntrinsicFunction result = new TIntrinsicFunction(2);
		result.addToken(supplyToken);
		result.addToken(nextToken);
		return result;
	}
}
