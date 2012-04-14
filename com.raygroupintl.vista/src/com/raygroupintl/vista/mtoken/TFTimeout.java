package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;

public class TFTimeout extends TFAllRequired {
	private MVersion version;
	
	private TFTimeout(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{TFConstChar.getInstance(':'), TFExpr.getInstance(this.version)};
	}
	
	public static TFTimeout getInstance(MVersion version) {
		return new TFTimeout(version);
	}
}
