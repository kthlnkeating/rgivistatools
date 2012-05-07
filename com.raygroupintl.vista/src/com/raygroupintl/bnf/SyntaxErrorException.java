package com.raygroupintl.bnf;

import com.raygroupintl.vista.struct.MError;

@SuppressWarnings("serial")
public class SyntaxErrorException extends Exception {
	private int code;
	private int location;
	
	public SyntaxErrorException(int code, int location) {
		this.code = code;
		this.location = location;
	}
	
	public SyntaxErrorException(int location) {
		this(MError.ERR_GENERAL_SYNTAX, location);
	}
	
	public int getLocation() {
		return this.location;
	}
	
	public int getCode() {
		return this.code;
	}

	public TSyntaxError getAsToken(String line, int fromIndex) {
		TSyntaxError result = new TSyntaxError(this.code, line, this.location);
		result.setFromIndex(fromIndex);
		return result;
	}
}
