package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFChoice implements ITokenFactory {
	private ITokenFactory[] factories;
	
	public TFChoice(ITokenFactory[] factories) {
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
