package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.token.TSyntaxError;

public abstract class DelimitedPair extends Base {

	protected abstract IToken getLeftToken();
	
	protected abstract IToken getRightToken();

	protected abstract String getDelimiter();
	
	private static void updateSB(StringBuilder sb, IToken token) {
		if (token != null) {
			String str = token.getStringValue();
			sb.append(str);
		}		
	}
	
	@Override
	public String getStringValue() {		
		StringBuilder sb = new StringBuilder();
		
		IToken left = this.getLeftToken();
		DelimitedPair.updateSB(sb, left);
		
		String delimiter = this.getDelimiter();
		if (delimiter != null) {
			sb.append(delimiter);
		}
		
		IToken right = this.getRightToken();
		DelimitedPair.updateSB(sb, right);
		
		return sb.toString();
	}

	@Override
	public int getStringSize() {
		int result = 0;
		
		IToken l = this.getLeftToken();
		if (l != null) {
			result += l.getStringSize();
		}
		
		String delimiter = this.getDelimiter();
		if (delimiter != null) {
			result += delimiter.length();
		}
		
		IToken r = this.getRightToken();
		if (r != null)  {
			result += r.getStringSize();
		}
		
		return result;
	}

	@Override
	public List<MError> getErrors() {
		IToken l = this.getLeftToken();
		List<MError> lErrors = (l == null) ? null : l.getErrors();
		
		IToken r = this.getLeftToken();
		List<MError> rErrors = (r == null) ? null : r.getErrors();
		
		if (lErrors == null) return rErrors;
		if (rErrors == null) return lErrors;
		
		List<MError> result = lErrors;
		result.addAll(rErrors);
		return result;
	}
	
	@Override
	public void beautify() {
		IToken l = this.getLeftToken();
		if (l != null) {
			l.beautify();
		}
		
		IToken r = this.getRightToken();
		if (r != null) {
			r.beautify();
		}
	}
	
	public static abstract class Factory implements ITokenFactory {
		protected abstract ITokenFactory getLeftFactory();
		
		protected abstract ITokenFactory getRightFactory();

		protected abstract char getDelimiter();
		
		protected abstract IToken getInstance(IToken left, IToken right, boolean delimiterExists);
		
		protected abstract boolean isLeftTokenOptional();

		protected abstract boolean isDelimiterOptional(IToken leftToken);
		
		protected abstract boolean isRightTokenOptional(IToken leftToken, boolean delimiterExists);

		@Override
		public IToken tokenize(String line, int fromIndex) {
			int endIndex = line.length();
			if (fromIndex < endIndex) {
				int index = fromIndex;
				ITokenFactory leftFactory = this.getLeftFactory();
				IToken leftToken = leftFactory.tokenize(line, index);
				if (leftToken == null) {
					if (! this.isLeftTokenOptional()) return null;
				} else {
					if (leftToken instanceof TSyntaxError) {
						return leftToken;
					}
					index += leftToken.getStringSize();
					if (index >= endIndex) {
						assert(index == endIndex);
						if (this.isDelimiterOptional(leftToken) && this.isRightTokenOptional(leftToken, false)) {
							return leftToken;
						} else {
							return new TSyntaxError(line, index, fromIndex);
						}						
					}				
				}
				char ch = line.charAt(index);
				if (ch != this.getDelimiter()) {
					if (leftToken == null) {
						return null;
					}					
					if (this.isDelimiterOptional(leftToken) && this.isRightTokenOptional(leftToken, false)) {
						return leftToken;
					} else {
						return new TSyntaxError(line, index, fromIndex);						
					}
				}
				++index;
				if (index >= endIndex) {
					assert(index == endIndex);
					if (this.isRightTokenOptional(leftToken, true)) {
						return this.getInstance(leftToken, null, true);
					} else {
						return new TSyntaxError(line, index, fromIndex);
					}						
				}				
				ITokenFactory rightFactory = this.getRightFactory();
				IToken rightToken = rightFactory.tokenize(line, index);
				if (rightToken == null) {
					if (this.isRightTokenOptional(leftToken, true)) {
						return this.getInstance(leftToken, null, true);
					} else {
						return new TSyntaxError(line, index, fromIndex);
					}
				}				
				if (rightToken instanceof TSyntaxError) {
					((TSyntaxError) rightToken).setFromIndex(fromIndex);
					return rightToken;
				}
				return this.getInstance(leftToken, rightToken, true);
			}
			return null;
		}
	}
}
