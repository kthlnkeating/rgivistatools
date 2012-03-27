package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFConstString;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRRO;

public class TFExprItem extends TFParallelCharBased {
	private static final class TFExtrinsic extends TFSerialRRO {
		@Override
		protected final ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstString("$$"), new TFLabelRef(), new TFActualList()};
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
	
	static class TFUnaryOnExprAtom extends TFAllRequired {
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstChars("+-\'"), TFExprAtom.getInstance()};
		}		
	}
	
	@Override
	protected ITokenFactory getFactory(char ch, char ch2) {
		if (ch == '$') {
			if (ch2 == '$') {
				return new TFExtrinsic();
			} else if (ch2 == '&') {
				return new External.TF();
			} else if (Library.isIdent(ch2)) {
				return new TFIntrinsic();
			} else {
				return null;
			}			
		}
		return null;
	}
		
	@Override
	protected ITokenFactory getFactory(char ch) {
		switch (ch) {
		case '"':
			return new TFStringLiteral();
		case '$':
			return null;
		case '\'':
		case '+':
		case '-':
			return new TFUnaryOnExprAtom();
		case '.':
			return TFNumLit.getInstance();
		case '(':
			return TFInParantheses.getInstance(TFExpr.getInstance());
		default:
			if (Library.isDigit(ch)) {
				return TFNumLit.getInstance();
			} else {
				return null;
			}				
		}
	}
	
	public static TFExprItem getInstance() {
		return new TFExprItem();
	}
}
