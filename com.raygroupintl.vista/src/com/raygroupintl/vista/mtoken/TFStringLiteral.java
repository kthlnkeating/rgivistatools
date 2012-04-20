package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TSyntaxError;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.struct.MError;

public class TFStringLiteral implements ITokenFactory {
	@Override
	public IToken tokenize(String line, int fromIndex) {
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
				return new TSyntaxError(MError.ERR_UNMATCHED_QUOTATION, line, fromIndex);
			}
		}
		return null;
	}
	
	public static TFStringLiteral getInstance() {
		return new TFStringLiteral();
	}
}