package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TFEnvironment extends TFParallelCharBased {
	
	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '|') {
			ITokenFactory d = new TFConstChar('|');
			return TFAllRequired.getInstance(d, TFExpr.getInstance(), d);
		} else if (ch == '[') {
			ITokenFactory l = new TFConstChar('[');
			ITokenFactory f = TFCommaDelimitedList.getInstance(TFExprAtom.getInstance(), ',');
			ITokenFactory r = new TFConstChar(']');			
			return TFAllRequired.getInstance(l, f, r);
		} else {
			return null;
		}
	}
	
	public static TFEnvironment getInstance() {
		return new TFEnvironment();
	}
}
