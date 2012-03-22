package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.token.TPair;

public class TLocal extends TPair {
	public TLocal(IToken name) {
		super(name);
	}

	public TLocal(IToken name, IToken subscript) {
		super(name, subscript);
	}
}
