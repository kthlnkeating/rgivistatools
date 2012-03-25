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
	
	public static TFParallelCharBased getInstance(final ITokenFactory fDefault, final char ch0, final ITokenFactory f0, final char ch1, final ITokenFactory f1) {
		return new TFParallelCharBased() {			
			@Override
			protected ITokenFactory getFactory(char ch) {
				if (ch == ch0) return f0;
				else if (ch == ch1) return f1;
				return fDefault;
			}
		};
	}

	public static TFParallelCharBased getInstance(final ITokenFactory fDefault, final char chOther, final ITokenFactory f) {
		return new TFParallelCharBased() {			
			@Override
			protected ITokenFactory getFactory(char ch) {
				if (ch == chOther) {
					return f;
				} else {
					return fDefault;
				}
			}
		};
	}
}
