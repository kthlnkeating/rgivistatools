package com.raygroupintl.bnf;

import com.raygroupintl.charlib.Predicate;

public abstract class TFChoiceOnChar extends TokenFactory {
	private TokenFactory defaultFactory = null;
	private Predicate[] predicates = {};
	private TokenFactory[] factories = {};
			
	public TFChoiceOnChar(String name) {		
		super(name);
	}
			
	public TFChoiceOnChar(String name, TokenFactory defaultFactory,  Predicate[] predicates, TokenFactory[] factories) {
		super(name);
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
	
	protected abstract TokenFactory getFactory(Text text);
	
	@Override
	public Token tokenize(Text text) throws SyntaxErrorException {
		TokenFactory f = this.getFactory(text);
		if (f != null) {
			Token result = f.tokenize(text);
			return result;
		}
		return null;
	}	
	
	public boolean isInitialize() {
		return this.factories.length > 0;
	}
}
