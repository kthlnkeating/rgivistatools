package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFChoice;

public class TFEnvironment extends TFChoice {
	private MVersion version;
	
	private TFEnvironment(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '|') {
			ITokenFactory d = new TFConstChar('|');
			return TFAllRequired.getInstance(d, TFExpr.getInstance(this.version), d);
		} else if (ch == '[') {
			ITokenFactory l = new TFConstChar('[');
			ITokenFactory f = TFCommaDelimitedList.getInstance(MTFSupply.getInstance(this.version).getTFExprAtom(), ',');
			ITokenFactory r = new TFConstChar(']');			
			return TFAllRequired.getInstance(l, f, r);
		} else {
			return null;
		}
	}
	
	public static TFEnvironment getInstance(MVersion version) {
		return new TFEnvironment(version);
	}
}
