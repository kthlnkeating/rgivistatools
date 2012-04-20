package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;

public abstract class TFSeqStatic extends TFSeq {
	private ITokenFactorySupply supply;
	
	protected abstract ITokenFactory[] getFactories();
	
	private void updateSupply() {
		final ITokenFactory[] factories = this.getFactories();
		this.supply = new ITokenFactorySupply() {			
			@Override
			public ITokenFactory get(int seqIndex, IToken[] previousTokens) {
				if (seqIndex < factories.length) {				
					return factories[seqIndex];
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
