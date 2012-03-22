package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;

public class LeftOptionalTwoTokens extends DelimitedPair {
	private IToken leftToken;
	private IToken rightToken;
		
	@Override
	protected IToken getLeftToken() {
		return this.leftToken;
	}

	@Override
	protected IToken getRightToken() {
		return this.rightToken;
	}

	@Override
	protected String getDelimiter() {
		return null;
	}

	@Override
	public List<MError> getErrors() {
		List<MError> result = null;
		
		IToken rightToken = this.getRightToken();
		if (rightToken == null) {
			result = Arrays.asList(new MError[]{new MError(MError.ERR_GENERAL_SYNTAX)});
		} else {
			result = rightToken.getErrors();
		}
		
		IToken leftToken = this.getLeftToken();
		if (leftToken != null) {
			List<MError> errors = leftToken.getErrors();
			if (result == null) {
				result = errors;
			} else if (errors != null) {
				result.addAll(errors);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean hasError() {
		IToken rightToken = this.getRightToken();
		if ((rightToken == null) || rightToken.hasError()) {
			return true;
		}
		IToken leftToken = this.getLeftToken();
		return (leftToken != null) && leftToken.hasError();
	}

	@Override
	public boolean hasFatalError() {
		IToken rightToken = this.getRightToken();
		if ((rightToken == null) || rightToken.hasFatalError()) {
			return true;
		}
		IToken leftToken = this.getLeftToken();
		return (leftToken != null) && leftToken.hasFatalError();
	}
	@Override
	public boolean isError() {
		return false;
	}

}
