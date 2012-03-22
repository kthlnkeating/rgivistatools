package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;

public abstract class TFSerial extends TFSerialBase {
	protected abstract ITokenFactory[] getFactories();

	protected final ITokenFactorySupply getFactorySupply() {
		final ITokenFactory[] factories = this.getFactories();
		ITokenFactorySupply result = new ITokenFactorySupply() {			
			@Override
			public ITokenFactory get(IToken[] previousTokens) {
				int index = previousTokens.length;
				if (index < factories.length) {				
					return factories[index];
				} else {
					return null;
				}
			}
			
			public int getCount() {
				return factories.length;
			}
		};
		return result;
	}
}
