package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialROO;
import com.raygroupintl.vista.token.TFSerialRRO;

public class TFReadArgument extends TFParallelCharBased {
	private static ITokenFactory getTFReadcountInstance() {
		return TFAllRequired.getInstance(TFConstChar.getInstance('#'), TFExpr.getInstance());
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		switch(ch) {
			case '!':
			case '#':
			case '?':
			case '/':
				return TFFormat.getInstance();
			case '"':
				return TFStringLiteral.getInstance();
			case '*':
				return TFSerialRRO.getInstance(TFConstChar.getInstance('*'), TFGlvn.getInstance(), TFTimeout.getInstance());
			case '@':
				return TFIndirection.getInstance();
			default: 
				return TFSerialROO.getInstance(TFGlvn.getInstance(), getTFReadcountInstance(), TFTimeout.getInstance());
		}
	}
}
