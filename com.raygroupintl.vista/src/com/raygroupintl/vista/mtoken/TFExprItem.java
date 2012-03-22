package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TFSerialRRO;

public class TFExprItem extends TFParallelCharBased {
	private static final class TFExtrinsicTail extends TFSerialRRO {
		@Override
		protected final ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstChar('$'), new TFLabelRef(), new TFActualList()};
		}

		@Override
		protected final IToken getTokenWhenNoOptional(IToken[] foundTokens) {
			return new TExtrinsicVar((TLabelRef) foundTokens[1]);
		}
		
		@Override
		protected final IToken getTokenWhenAll(IToken[] foundTokens) {
			return new TExtrinsicFunc((TLabelRef) foundTokens[1], (TActualList) foundTokens[2]);			
		}
	}
	
	private static class TFInstrinsic extends TFSerialRO {
		protected ITokenFactory getRequired() {
			return new TFIdent();
		}
				
		protected ITokenFactory getOptional() {
			return new TFActualList();
		}
		
		protected IToken getTokenRequired(IToken requiredToken) {
			return new TIntrinsicVar((TBasic) requiredToken) ;
		}
		
		protected IToken getTokenBoth(IToken requiredToken, IToken optionalToken) {
			return new TIntrinsicFunc((TBasic) requiredToken, (TActualList) optionalToken);
		}
	}
	
	private static class TFDollarItems extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			if (ch == '$') {
				return new TFExtrinsicTail();
			} else if (ch == '&') {
				return new External.TF();
			} else if (Library.isIdent(ch)) {
				return new TFInstrinsic();
			} else {
				return null;
			}
		}		
	}
	
	static class TFUnaryOnExprAtom extends TFAllRequired {
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstChars("+-\'"), TFExprAtom.getInstance()};
		}		
	}
		
	protected ITokenFactory getFactory(char ch) {
		switch (ch) {
		case '"':
			return new TFStringLiteral();
		case '$':
			return new TFAllRequired() {				
				@Override
				protected ITokenFactory[] getFactories() {
					return new ITokenFactory[]{new TFConstChar('$'), new TFDollarItems()};
				}
				
				@Override
				protected IToken getToken(IToken[] foundTokens) {
					IToken result = foundTokens[1];
					if (result instanceof External.TReference) {
						return new External.TVariable((External.TReference) result); 
					} else if (result instanceof External.TReferenceWithArgument) {
						return new External.TFunction((External.TReferenceWithArgument) result); 						
					} else {
						return result;
					}
				}
			};
		case '\'':
		case '+':
		case '-':
			return new TFUnaryOnExprAtom();
		case '.':
			return TNumLit.getFactory();
		default:
			if (Library.isDigit(ch)) {
				return TNumLit.getFactory();
			} else {
				return null;
			}				
		}
	}
}
