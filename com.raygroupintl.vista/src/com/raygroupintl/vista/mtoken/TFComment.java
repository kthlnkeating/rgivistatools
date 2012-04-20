package com.raygroupintl.vista.mtoken;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFComment implements ITokenFactory {
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);
			if (ch == ';') {
				String commentContent = line.substring(fromIndex+1);
				return new TComment(commentContent);
			}
		}
		return null;
	}
}
