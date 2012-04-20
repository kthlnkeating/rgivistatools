package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TPair;
import com.raygroupintl.fnds.IToken;

public class TLocal extends TPair {
	public TLocal(IToken name) {
		super(name);
	}

	public TLocal(IToken name, IToken subscript) {
		super(name, subscript);
	}
}
