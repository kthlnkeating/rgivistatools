package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFConstString;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TFExprItem extends TFParallelCharBased {
	private static final class TFExtrinsic extends TFAllRequired {
		private MVersion version; 
		
		private TFExtrinsic(MVersion version) {			
			this.version = version;
		}
		
		@Override
		protected final ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstString("$$"), TFDoArgument.getInstance(this.version, true)};
		}

		@Override
		protected final IToken getToken(IToken[] foundTokens) {
			return new TExtrinsic(foundTokens[1]);
		}
	}
	
	static class TFUnaryOnExprAtom extends TFAllRequired {
		private MVersion version; 
		
		private TFUnaryOnExprAtom(MVersion version) {			
			this.version = version;
		}
				
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstChars("+-\'"), TFExprAtom.getInstance(this.version)};
		}		
	}
	
	private MVersion version;
	
	private TFExprItem(MVersion version) {
		this.version = version;
	}
	
	@Override
	protected ITokenFactory getFactory(char ch, char ch2) {
		if (ch == '$') {
			if (ch2 == '$') {
				return new TFExtrinsic(this.version);
			} else if (ch2 == '&') {
				return new External.TF(this.version);
			} else if (Library.isIdent(ch2)) {
				return TFIntrinsic.getInstance(this.version);
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
			return new TFUnaryOnExprAtom(this.version);
		case '.':
			return TFNumLit.getInstance();
		case '(':
			return TFInParantheses.getInstance(TFExpr.getInstance(this.version));
		default:
			if (Library.isDigit(ch)) {
				return TFNumLit.getInstance();
			} else {
				return null;
			}				
		}
	}
	
	public static TFExprItem getInstance(MVersion version) {
		return new TFExprItem(version);
	}
}
