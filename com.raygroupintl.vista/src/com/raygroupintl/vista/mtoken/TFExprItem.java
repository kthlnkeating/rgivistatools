package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFConstString;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TFExprItem extends TFParallelCharBased {
	private static final class TFExtrinsic extends TFAllRequired {
		@Override
		protected final ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstString("$$"), TFDoArgument.getInstance(true)};
		}

		@Override
		protected final IToken getToken(IToken[] foundTokens) {
			return new TExtrinsic(foundTokens[1]);
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
