package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFParallelCharBased implements ITokenFactory {
	protected abstract ITokenFactory getFactory(char ch);
	
	protected ITokenFactory getFactory(char ch, char ch2) {
		return null;
	}

	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);			
			ITokenFactory f = this.getFactory(ch);
			if ((f == null) && (fromIndex + 1 < endIndex)) {
				char ch2 = line.charAt(fromIndex+1);
				f = this.getFactory(ch, ch2);				
			}
			if (f != null) {
				IToken result = f.tokenize(line, fromIndex);
				return result;
			}
		}
		return null;
	}
}
