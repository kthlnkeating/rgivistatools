package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.vista.struct.MError;

public class Expression extends Basic {
	public Expression(String line, int index) {
		super(line.substring(index));
	}
	
	@Override
	public List<MError> getErrors() {
		return null;
	}

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	public boolean hasFatalError() {
		return false;
	}

}
