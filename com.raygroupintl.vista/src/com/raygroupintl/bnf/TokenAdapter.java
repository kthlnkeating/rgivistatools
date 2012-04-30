package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;

public interface TokenAdapter {
	IToken convert(IToken[] tokens);
}
