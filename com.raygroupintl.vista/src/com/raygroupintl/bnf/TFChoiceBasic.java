package com.raygroupintl.bnf;


public class TFChoiceBasic extends TokenFactory {
	private TokenFactory[] factories = {};
	
	public TFChoiceBasic() {
	}
	
	public TFChoiceBasic(TokenFactory... factories) {
		this.factories = factories;
	}
	
	public void setFactories(TokenFactory... factories) {
		this.factories = factories;
	}
	
	public int getNumChoices() {
		return this.factories.length;
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
	
	public boolean isInitialize() {
		return this.factories.length > 0;
	}
}
