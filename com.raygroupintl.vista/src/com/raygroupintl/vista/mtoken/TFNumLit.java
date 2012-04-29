package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFSeqOR;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqROR;
import com.raygroupintl.bnf.TFSeqXYY;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFNumLit extends TFSeqRO {
	private static class TFExp extends TFSeqROR {
		protected ITokenFactory[] getFactories() {
			ITokenFactory f = TIntLit.getFactory();
			return new ITokenFactory[]{new TFConstChar('E'), new TFConstChars("+-"), f};
		}		
	}
	
	@Override
	protected ITokenFactory[] getFactories() {
		ITokenFactory f = TIntLit.getFactory();
		ITokenFactory p = new TFConstChar('.');
		TFSeqXYY number = TFSeqXYY.getInstance(f, p, f);
		ITokenFactory f0 = TFSeqOR.getInstance(TFConstChars.getInstance("-+"), number);
		ITokenFactory f1 = new TFExp();
		return new ITokenFactory[]{f0, f1};
	}
	
	@Override
	protected IToken getToken(IToken[] foundTokens) {
		if (foundTokens[1] == null) {
			String value = foundTokens[0].getStringValue();
			return new TNumLit(value);			
		} else {		
			String value = foundTokens[0].getStringValue() + foundTokens[1].getStringValue();
			return new TNumLit(value);
		}	
	}
	
	public static TFNumLit getInstance() {
		return new TFNumLit();
	}
}
