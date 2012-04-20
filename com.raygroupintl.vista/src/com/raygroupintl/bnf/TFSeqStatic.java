package com.raygroupintl.bnf;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;

public abstract class TFSeqStatic extends TFSeq {
	private ITokenFactorySupply supply;
	
	protected abstract ITokenFactory[] getFactories();
	
	private void updateSupply() {
		final ITokenFactory[] factories = this.getFactories();
		this.supply = new ITokenFactorySupply() {			
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
	}
	
	protected final ITokenFactorySupply getFactorySupply() {
		if (this.supply == null) {
			this.updateSupply();
		}
		return this.supply;
	}
}
