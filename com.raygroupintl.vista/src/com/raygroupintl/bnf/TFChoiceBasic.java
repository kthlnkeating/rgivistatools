package com.raygroupintl.bnf;


public class TFChoiceBasic implements TokenFactory {
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
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			for (TokenFactory f : this.factories) {
				Token result = f.tokenize(line, fromIndex);
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
