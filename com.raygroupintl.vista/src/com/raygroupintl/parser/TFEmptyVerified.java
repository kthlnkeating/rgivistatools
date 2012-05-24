package com.raygroupintl.parser;

import com.raygroupintl.parser.annotation.AdapterSupply;


public abstract class TFEmptyVerified extends TokenFactory {
	public TFEmptyVerified(String name) {
		super(name);
	}
	
	protected abstract boolean isExpected(char ch);
		
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			char ch = text.getChar();
			if (this.isExpected(ch)) {
				return new TEmpty();
			} else {
				throw new SyntaxErrorException();
			}
		}
		return null;
	}
	
	public static TFEmptyVerified getInstance(String name, final char chIn) {
		return new TFEmptyVerified(name) {			
			@Override
			protected boolean isExpected(char ch) {
				return chIn == ch;
			}
		};
	}
}
