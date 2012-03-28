package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFEmpty;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSyntaxError;

public class TFActual extends TFParallelCharBased {
	protected ITokenFactory getFactory(char ch) {
		switch (ch) {
			case '.':
				return null;
			case ',':
			case ')':
				return TFEmpty.getInstance(ch);
			default:
				return TFExpr.getInstance();
		}
	}
	
	protected ITokenFactory getFactory(char ch, char ch2) {
		if (ch == '.') {
			if (Library.isDigit(ch2)) {
				return TFNumLit.getInstance();
			} else if ((ch2 == '%') || Library.isIdent(ch2)) {
				return TFAllRequired.getInstance(TFConstChar.getInstance('.'), TFName.getInstance());
			} else {
				return TFSyntaxError.getInstance();
			}
		} else {
			return null;
		}
	}

	public static TFActual getInstance() {
		return new TFActual();
	}
}
