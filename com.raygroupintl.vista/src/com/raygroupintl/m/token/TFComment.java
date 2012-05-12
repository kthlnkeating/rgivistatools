package com.raygroupintl.m.token;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;

public class TFComment extends TokenFactory {
	@Override
	public Token tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);
			if (ch == ';') {
				//int index = fromIndex + 1;
				//while (index < endIndex) {
				//	char chLoop = line.charAt(index);
				//	if ((chLoop == '\n') || (chLoop == '\r')) break;
				//	++index;
				//}
				int index = endIndex;
				String commentContent = line.substring(fromIndex, index);
				return new TComment(commentContent);
			}
		}
		return null;
	}
}
