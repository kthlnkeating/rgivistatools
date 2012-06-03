package com.raygroupintl.parser;

import com.raygroupintl.parser.annotation.ObjectSupply;


public class TFChoiceBasic extends TokenFactory {
	private TokenFactory[] factories = {};
	
	public TFChoiceBasic(String name) {
		super(name);
	}
	
	public TFChoiceBasic(String name, TokenFactory... factories) {
		super(name);
		this.factories = factories;
	}
	
	public void setFactories(TokenFactory... factories) {
		this.factories = factories;
	}
	
	@Override
	public Token tokenize(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			for (TokenFactory f : this.factories) {
				Token result = f.tokenize(text, objectSupply);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
}
