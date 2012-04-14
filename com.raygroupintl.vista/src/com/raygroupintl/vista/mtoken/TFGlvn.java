package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TFGlvn extends TFParallelCharBased {
	private MVersion version;
	
	private TFGlvn(MVersion version) {
		this.version = version;
	}	
	
	@Override
	protected ITokenFactory getFactory(char ch) {
		if ((ch == '%') || Library.isIdent(ch)) {
			return TFLvn.getInstance(this.version);
		} else if (ch == '^') {
			return TFGvnAll.getInstance(this.version);
		} else if (ch == '@') {
			return TFIndirection.getInstance(this.version);
		} else {
			return null;
		}		
	}
	
	public static TFGlvn getInstance(MVersion version) {
		return new TFGlvn(version);
	}
}
