package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

class TFChoiceBasic implements ITokenFactory {
	private ITokenFactory[] factories;
	
	public TFChoiceBasic(ITokenFactory[] factories) {
		this.factories = factories;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			for (ITokenFactory f : this.factories) {
				IToken result = f.tokenize(line, fromIndex);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}		
}
