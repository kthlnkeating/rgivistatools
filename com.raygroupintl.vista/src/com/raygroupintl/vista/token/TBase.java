package com.raygroupintl.vista.token;

import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;


public abstract class TBase implements IToken {
	public TBase() {
		super();
	}

	@Override
	public int getStringSize() {
		String v = this.getStringValue();
		return v.length();
	}

	@Override
	public boolean hasError() {
		return this.getErrors() != null;
	}

	@Override
	public boolean hasFatalError() {
		List<MError> errors = this.getErrors();
		if (errors != null) {
			for (MError e : errors) {
				if (e.isFatal()) return true;
			}
		}
		return false;
	}
}