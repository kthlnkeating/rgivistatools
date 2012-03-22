package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFOperator implements ITokenFactory {
	private int getOperatorLength(String line, int fromIndex, String possible, char extra) {
		char ch = line.charAt(fromIndex);
		if (possible.indexOf(ch, 0) >= 0) {
			if (ch == extra) {
				int index = fromIndex + 1;
				if ((index < line.length()) && (line.charAt(index) == '*')) {
					return 2;
				}
			}			
			return 1;				
		}
		return 0;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();		
		if (fromIndex < endIndex) {
			int opLength = this.getOperatorLength(line, fromIndex, "-+_*/#\\", '*');
			if (opLength == 0) {
				char ch = line.charAt(fromIndex);
				int index = fromIndex;
				if (ch == '\'') {
					++index;
					if (index == endIndex) {
						return null;
					}
				}
				opLength = this.getOperatorLength(line, fromIndex, "&!=<>][?", ']');
				if (opLength > 0) {
					if (ch == '\'') {
						++opLength;
					}					
				}
			}
			if (opLength > 0) {
				return new TOperator(line.substring(fromIndex, fromIndex+opLength));
			}
		}
		return null;	
	}
	
	public static TFOperator getInstance() {
		return new TFOperator();
	}
}
