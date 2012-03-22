package com.raygroupintl.vista.fnds;

import java.util.List;

import com.raygroupintl.vista.struct.MError;

public interface IToken {
	String getStringValue();
	int getStringSize();
	
	public List<MError> getErrors();	
	boolean hasError();
	boolean hasFatalError();
	
	boolean isError();
	
	void beautify();
}
