package com.raygroupintl.m.token;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.vista.struct.MError;

public class TFStringLiteral extends TokenFactory {
	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);
			if (ch == '"') {			
				StringBuilder sb = new StringBuilder();
				int index = fromIndex;
				++index;
				while (index < endIndex) {
					ch = line.charAt(index);
					++index;
					if (ch == '"') {
						if ((index == endIndex) || (line.charAt(index) != '"')) {
							return new TStringLiteral(sb.toString());
						}
						++index;
					}				
					sb.append(ch);
				}
				throw new SyntaxErrorException(MError.ERR_UNMATCHED_QUOTATION, fromIndex);
			}
		}
		return null;
	}
	
	public static TFStringLiteral getInstance() {
		return new TFStringLiteral();
	}
}