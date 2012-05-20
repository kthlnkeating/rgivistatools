package com.raygroupintl.parser;


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
	public Token tokenize(Text text) throws SyntaxErrorException {
		if (text.onChar()) {
			for (TokenFactory f : this.factories) {
				Token result = f.tokenize(text);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
}
