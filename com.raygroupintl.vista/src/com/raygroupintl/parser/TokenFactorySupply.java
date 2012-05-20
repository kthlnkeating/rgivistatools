package com.raygroupintl.parser;

public abstract class TokenFactorySupply extends TokenFactory {
	public TokenFactorySupply(String name) {
		super(name);
	}
	
	public abstract TokenFactory getSupplyTokenFactory();
	
	public abstract TokenFactory getNextTokenFactory(Token token) throws SyntaxErrorException;
	
	public abstract Token getToken(Token supplyToken, Token nextToken);
	
	@Override
	public Token tokenize(Text text) throws SyntaxErrorException {
		TokenFactory supplyFactory = getSupplyTokenFactory();
		Token token = supplyFactory.tokenize(text);
		if (token == null) {
			return null;
		} else {
			TokenFactory tf = this.getNextTokenFactory(token);
			if (tf == null) {
				return token;
			} else {
				Token nextToken = tf.tokenize(text);
				return this.getToken(token, nextToken);
			}
		}
	}	
}
