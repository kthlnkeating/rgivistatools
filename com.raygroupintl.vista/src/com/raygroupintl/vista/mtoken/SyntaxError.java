package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.vista.struct.MError;


public class SyntaxError extends Basic {	
	public SyntaxError(String line, int index) {
		super(line.substring(index));
	}
	
	@Override
	public List<MError> getErrors() {
		MError e = new MError(MError.ERR_GENERAL_SYNTAX);
		return Arrays.asList(new MError[]{e});
	}

	@Override
	public boolean hasError() {
		return true;
	}

	@Override
	public boolean hasFatalError() {
		return true;
	}
}