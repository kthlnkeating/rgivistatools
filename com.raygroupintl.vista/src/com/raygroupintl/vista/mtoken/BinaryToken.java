package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;

public class BinaryToken implements IToken {
	private IToken leftToken;
	private IToken rightToken;
	private String delimiter;
	
	public BinaryToken(IToken leftToken, IToken rightToken, String delimiter) {
		this.leftToken = leftToken;
		this.rightToken = rightToken;
		this.delimiter = delimiter;
	}
		
	@Override
	public String getStringValue() {		
		return this.leftToken.getStringValue() + this.delimiter + this.rightToken.getStringValue();
	}

	@Override
	public int getStringSize() {
		return this.leftToken.getStringSize() + this.delimiter.length() + this.rightToken.getStringSize();
	}

	@Override
	public List<MError> getErrors() {
		List<MError> leftErrors = this.leftToken.getErrors();
		List<MError> rightErrors = this.rightToken.getErrors();
		if (leftErrors == null) {
			return rightErrors;
		}
		if (rightErrors == null) {
			return leftErrors;
		}
		leftErrors.addAll(rightErrors);
		return leftErrors;
	}
	
	@Override
	public boolean hasError() {
		return this.leftToken.hasError() || this.rightToken.hasError();
	}

	@Override
	public boolean hasFatalError() {
		return this.leftToken.hasFatalError() || this.rightToken.hasFatalError();
	}

	@Override
	public boolean isError() {
		return false;
	}

	@Override
	public void beautify() {
		this.leftToken.beautify();
		this.rightToken.beautify();
	}
}