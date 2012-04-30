package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFEmpty implements ITokenFactory {
	private ITokenFactory expected;
	
	public TFEmpty() {		
	}
	
	public TFEmpty(ITokenFactory expected) {
		this.expected = expected;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		if (fromIndex < line.length()) {
			if (this.expected == null) {
				return new TEmpty();
			} else {
				IToken t = this.expected.tokenize(line, fromIndex);
				if ((t != null) && ! (t instanceof TSyntaxError)) {
					return new TEmpty();
				}
			}
		}
		return null;
	}
	
	public static TFEmpty getInstance() {
		return new TFEmpty();	
	}
}
