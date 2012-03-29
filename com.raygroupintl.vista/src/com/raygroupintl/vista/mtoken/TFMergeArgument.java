package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerial;

public class TFMergeArgument extends TFSerial {
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{TFGlvn.getInstance(), TFConstChar.getInstance('='), TFExpr.getInstance()}; 
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
		if ((n == 0) && (foundTokens[0] instanceof TIndirection)) {
			return 0;
		} else {
			return this.getErrorCode();
		}
	}
}	
