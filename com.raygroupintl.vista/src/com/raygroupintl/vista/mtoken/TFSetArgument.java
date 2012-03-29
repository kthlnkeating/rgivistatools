package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerial;

public class TFSetArgument extends TFSerial {
	private static class TFSetLeft extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			switch(ch) {
			case '$':
				return TFIntrinsic.getInstance();
			case '@':
				return TFIndirection.getInstance();
			default:
				return TFGlvn.getInstance();
			}
		}
		
		public static TFSetLeft getInstance() {
			return new TFSetLeft();
		}
	}

	private static class TFSetDestination extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			TFSetLeft tfSL = TFSetLeft.getInstance();
			if (ch == '(') {
				TFDelimitedList tfDL = TFDelimitedList.getInstance(tfSL, ',');
				return TFInParantheses.getInstance(tfDL);
			} else {
				return new TFSetLeft();
			}
		}		
		
		public static TFSetDestination getInstance() {
			return new TFSetDestination();
		}
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{TFSetDestination.getInstance(), TFConstChar.getInstance('='), TFExpr.getInstance()}; 
	}

	@Override
	protected int getCodeNextIsNull(IToken[] foundTokens) {
		int n = foundTokens.length;
		if (n == 0) {
			return RETURN_NULL;
		}
		if (n == 1) {
			if (foundTokens[0] instanceof TIndirection) {
				return RETURN_TOKEN;
			} else {
				return this.getErrorCode();
			}
		}
		return CONTINUE;
	}
	
	@Override		
	protected int getCodeStringEnds(IToken[] foundTokens) {
		int n = foundTokens.length;
		if ((n == 1) && (foundTokens[0] instanceof TIndirection)) {
			return 0;
		} else {
			return this.getErrorCode();
		}
	}
}	

