package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.token.TArray;

public class TLine extends TArray {	
	public TLine(IToken[] tokens) {
		super(tokens);
	}

	public String getTag() {
		IToken tag = this.get(0);
		if (tag == null) {
			return null;
		} else {
			return tag.getStringValue();
		}
	}
}
