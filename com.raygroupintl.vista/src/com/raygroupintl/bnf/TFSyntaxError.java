package com.raygroupintl.bnf;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.struct.MError;

public class TFSyntaxError implements ITokenFactory {	
	protected int getErrorCode() {
		return MError.ERR_GENERAL_SYNTAX;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int errorCode = this.getErrorCode();
		return new TSyntaxError(errorCode, line, fromIndex);
	}
	
	public static TFSyntaxError getInstance() {
		return new TFSyntaxError();
	}

	public static TFSyntaxError getInstance(final int errorCode) {
		return new TFSyntaxError() {
			@Override
			public int getErrorCode() {
				return errorCode;
			}
		};
	}
}
