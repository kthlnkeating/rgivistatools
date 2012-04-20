package com.raygroupintl.bnf;

import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;

public abstract class TCopy implements IToken {
	private IToken source;
	
	public TCopy(IToken source) {
		this.source = source;
	}
	
	@Override
	public String getStringValue() {
		return this.source.getStringValue();
	}

	@Override
	public int getStringSize() {
		return this.source.getStringSize();
	}

	@Override
	public List<MError> getErrors() {
		return this.source.getErrors();
	}

	@Override
	public boolean hasError() {
		return this.source.hasError();
	}

	@Override
	public boolean hasFatalError() {
		return this.source.hasFatalError();
	}

	@Override
	public void beautify() {
		this.source.beautify();
	}
}
