package com.raygroupintl.bnf;

import com.raygroupintl.charlib.Predicate;

public abstract class TFChoiceOnChar implements TokenFactory {
	private TokenFactory defaultFactory = null;
	private Predicate[] predicates = {};
	private TokenFactory[] factories = {};
			
	public TFChoiceOnChar() {		
	}
			
	public TFChoiceOnChar(TokenFactory defaultFactory,  Predicate[] predicates, TokenFactory[] factories) {
		this.defaultFactory = defaultFactory;
		this.predicates = predicates;
		this.factories = factories;
	}

	public void setDefault(TokenFactory defaultFactory) {
		this.defaultFactory = defaultFactory;
	}
	
	public void setChoices(Predicate[] predicates, TokenFactory... factories) {
		this.predicates = predicates;
		this.factories = factories;
	}
	
	protected TokenFactory getFactory(char ch) {
		for (int i=0; i<this.predicates.length; ++i) {
			Predicate predicate = this.predicates[i];
			if (predicate.check(ch)) {
				return this.factories[i];
			}
		}
		return this.defaultFactory;
	}
	
	protected abstract TokenFactory getFactory(String line, int index);
	
	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		TokenFactory f = this.getFactory(line, fromIndex);
		if (f != null) {
			Token result = f.tokenize(line, fromIndex);
			return result;
		}
		return null;
	}	
	
	public boolean isInitialize() {
		return this.factories.length > 0;
	}
}
