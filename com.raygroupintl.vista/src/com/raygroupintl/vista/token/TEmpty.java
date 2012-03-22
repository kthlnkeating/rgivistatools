package com.raygroupintl.vista.token;

import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;

public class TEmpty implements IToken {
	@Override
	public String getStringValue() {
		return "";
	}

	@Override
	public int getStringSize() {
		return 0;
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

	@Override
	public boolean isError() {
		return false;
	}

	@Override
	public void beautify() {
	}
}
