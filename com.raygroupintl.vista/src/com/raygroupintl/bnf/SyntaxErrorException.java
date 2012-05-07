package com.raygroupintl.bnf;

import com.raygroupintl.vista.struct.MError;

@SuppressWarnings("serial")
public class SyntaxErrorException extends Exception {
	private int location;
	
	public SyntaxErrorException(int location) {
		this.location = location;
	}
	
	public int getLocation() {
		return this.location;
	}

	public TSyntaxError getAsToken(String line, int fromIndex) {
		TSyntaxError result = new TSyntaxError(MError.ERR_GENERAL_SYNTAX, line, this.location);
		result.setFromIndex(fromIndex);
		return result;
	}
}
