package com.raygroupintl.m.token;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFName implements ITokenFactory {
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);
			if ((ch == '%') || Library.isIdent(ch)) {
				int index = fromIndex + 1;
				while (index < endIndex) {
					ch = line.charAt(index);
					if(! (Library.isDigit(ch) || Library.isIdent(ch))) {
						break;
					}
					++index;
				}
				String value = line.substring(fromIndex, index);
				return new TName(value);
			}
		}
		return null;			
	}
	
	public static TFName getInstance() {
		return new TFName();
	}
}
