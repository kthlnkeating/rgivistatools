package com.raygroupintl.bnf;

import java.util.List;

import com.raygroupintl.vista.struct.MError;

public interface Token {
	String getStringValue();
	int getStringSize();
	
	public List<MError> getErrors();	
	boolean hasError();
	boolean hasFatalError();
	
	void beautify();
}
