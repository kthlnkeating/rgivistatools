package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFChoiceOnChar implements ITokenFactory {
	private ITokenFactory defaultFactory;
	private String keys;
	private ITokenFactory[] factories;
			
	public TFChoiceOnChar(ITokenFactory defaultFactory, String keys, ITokenFactory[] factories) {
		this.defaultFactory = defaultFactory;
		this.keys = keys;
		this.factories = factories;
	}

	protected ITokenFactory getFactory(char ch) {
		int factoryIndex = this.keys.indexOf(ch);
		if (factoryIndex < 0) {
			return this.defaultFactory;
		} else {
			return this.factories[factoryIndex];
		}						
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
