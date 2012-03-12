package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;

public abstract class TwoTokens implements IToken {

	protected abstract IToken getLeftToken();
	
	protected abstract IToken getRightToken();

	protected abstract String getDelimiter();
	
	private static void updateSB(StringBuilder sb, IToken token) {
		if (token != null) {
			String str = token.getStringValue();
			assert(str != null);
			sb.append(str);
		}		
	}
	
	@Override
	public String getStringValue() {		
		StringBuilder sb = new StringBuilder();
		IToken left = this.getLeftToken();
		TwoTokens.updateSB(sb, left);
		String delimiter = this.getDelimiter();
		assert(delimiter != null);
		sb.append(delimiter);
		IToken right = this.getRightToken();
		TwoTokens.updateSB(sb, right);
		return sb.toString();
	}

	@Override
	public int getStringSize() {
		int result = 0;
		IToken left = this.getLeftToken();
		if (left != null) result += left.getStringSize();
		String delimiter = this.getDelimiter();
		assert(delimiter != null);
		result += delimiter.length();
		IToken right = this.getRightToken();
		if (right != null)  result += right.getStringSize();
		return result;
	}

	@Override
	public List<MError> getErrors() {
		IToken left = this.getLeftToken();
		IToken right = this.getRightToken();
		if ((left == null) || (right == null)) {
			return Arrays.asList(new MError[]{new MError(MError.ERR_GENERAL_SYNTAX)});
		}
		List<MError> leftErrors = left.getErrors();
		List<MError> rightErrors = right.getErrors();
		if ((leftErrors == null) && (rightErrors == null)) {
			return null;
		}
		List<MError> errors = leftErrors;
		if (errors == null) {
			errors = rightErrors;
		} else if (rightErrors != null) {
			errors.addAll(rightErrors);
		}
		return errors;
	}
	
	@Override
	public boolean hasError() {
		IToken left = this.getLeftToken();
		if (left == null) {
			return true;
		}
		IToken right = this.getRightToken();
		if (right == null) {
			return true;
		}
		return right.hasError() || left.hasError();
	}

	@Override
	public boolean hasFatalError() {
		IToken left = this.getLeftToken();
		if (left == null) {
			return true;
		}
		IToken right = this.getRightToken();
		if (right == null) {
			return true;
		}
		return right.hasFatalError() || left.hasFatalError();
	}

	@Override
	public void beautify() {
		IToken left = this.getLeftToken();
		if (left != null) {
			left.beautify();
		}
		IToken right = this.getRightToken();
		if (right != null) {
			right.beautify();
		}
	}

}
