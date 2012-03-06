package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class Tag extends Base {
	private IToken identifier;
	private IToken arguments;
	
	public Tag() {		
	}
	
	public Tag(IToken identifier) {
		this.identifier = identifier;
	}
	
	public Tag(IToken identifier, IToken arguments) {
		this.identifier = identifier;
		this.arguments = arguments;
	}
	
	public void setIdentifier(IToken identifier) {
		this.identifier = identifier;
	}
			
	public void setArguments(IToken arguments) {
		this.arguments = arguments;
	}
			
	@Override
	public String getStringValue() {
		String result = "";
		if (this.identifier != null) {
			result += this.identifier.getStringValue();
		}
		if (this.arguments != null) {
			result += this.arguments.getStringValue();
		}		
		return result;
	}
	
	@Override
	public int getStringSize() {
		int result = 0;
		if (this.identifier != null) {
			result += this.identifier.getStringSize();
		}
		if (this.arguments != null) {
			result += this.arguments.getStringSize();
		}		
		return result;
	}
	
	public static Tag getInstance(String line, int fromIndex) {
		Tag result = new Tag();
		int length = line.length();
		if (fromIndex < length) {
			int index = (line.charAt(fromIndex) == '%') ? fromIndex+1 : fromIndex;
			int toIndex = Algorithm.findOther(line, index, Algorithm.CharType.LETTER_DIGIT);
			if (toIndex > fromIndex) {
				IToken identifier = new Identifier(line.substring(fromIndex, toIndex));
				result.setIdentifier(identifier);
				if (toIndex < length) {
					char ch = line.charAt(toIndex);
					if (ch == '(') {
						ITokenFactory f = FactorySupply.findFactory('(');
						IToken arguments = f.tokenize(line, toIndex);
						result.setArguments(arguments);
					}
				}
			}
		}		
		return result;
	}
	
	@Override
	public void beautify() {		
	}

}
