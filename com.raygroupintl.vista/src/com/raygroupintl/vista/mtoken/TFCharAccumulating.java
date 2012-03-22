package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TSyntaxError;

public abstract class TFCharAccumulating implements ITokenFactory {
	protected abstract boolean stopOn(char ch);
	
	protected abstract IToken getToken(String line, int fromIndex);
	
	protected abstract IToken getToken(String line, int fromIndex, int endIndex);
	
	protected abstract boolean isRightStop(char ch);
	
	protected boolean checkFirst() {
		return false;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		boolean first = true;
		if (fromIndex < endIndex) {
			int index = fromIndex;
			do {
				char ch = line.charAt(index);
				if ((! first) || this.checkFirst()) {				
					if (this.stopOn(ch)) {
						if (this.isRightStop(ch)) {
							return this.getToken(line, fromIndex, index);
						} else {
							return new TSyntaxError(line, index, fromIndex);
						}
					}
				}
				++index;
				first = false;
			} while (index < endIndex);
			return this.getToken(line, fromIndex);
		}
		return null;
	}
}
