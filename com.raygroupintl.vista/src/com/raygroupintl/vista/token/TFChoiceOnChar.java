package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.ICharPredicate;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

abstract class TFChoiceOnChar implements ITokenFactory {
	private ITokenFactory defaultFactory;
	private ICharPredicate[] predicates;
	private ITokenFactory[] factories;
			
	public TFChoiceOnChar(ITokenFactory defaultFactory,  ICharPredicate[] predicates, ITokenFactory[] factories) {
		this.defaultFactory = defaultFactory;
		this.predicates = predicates;
		this.factories = factories;
	}

	protected ITokenFactory getFactory(char ch) {
		for (int i=0; i<this.predicates.length; ++i) {
			ICharPredicate predicate = this.predicates[i];
			if (predicate.check(ch)) {
				return this.factories[i];
			}
		}
		return this.defaultFactory;
	}
	
	protected abstract ITokenFactory getFactory(String line, int index);
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		ITokenFactory f = this.getFactory(line, fromIndex);
		if (f != null) {
			IToken result = f.tokenize(line, fromIndex);
			return result;
		}
		return null;
	}		
}
