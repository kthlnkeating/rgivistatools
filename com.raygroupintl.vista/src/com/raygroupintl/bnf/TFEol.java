package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFEol implements ITokenFactory {
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch0th = line.charAt(fromIndex);
			if ((ch0th == '\n') || (ch0th == '\r')) {
				int nextIndex = fromIndex + 1;
				if (nextIndex < endIndex) {
					char ch1st = line.charAt(nextIndex);
					if ((ch1st == '\n') || (ch1st == '\r')) {
						return new TBasic(line.substring(fromIndex, fromIndex+2));
					}
				}
				return new TBasic(line.substring(fromIndex, fromIndex+1));
			}
		}
		return null;
	}
}
