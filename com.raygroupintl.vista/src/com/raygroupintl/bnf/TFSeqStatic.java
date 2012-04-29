package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;

public abstract class TFSeqStatic extends TFSeq {
	private ITokenFactorySupply supply;
	private int firstRequired = Integer.MAX_VALUE;
	private int lastRequired = Integer.MIN_VALUE;
	private int lookAhead = 0;
	
	public TFSeqStatic() {		
	}
	
	public TFSeqStatic(ITokenFactory[] factories) {
		this.supply = new TFSStatic(factories);		
	}
		
	public void setFactories(ITokenFactory[] factories) {
		this.supply = new TFSStatic(factories);		
	}
	
	public void setRequiredFlags(boolean[] flags) {
		this.firstRequired = Integer.MAX_VALUE;
		this.lastRequired = Integer.MIN_VALUE;
		int index = 0;
		for (boolean b : flags) if (b) {
			if (this.firstRequired == Integer.MAX_VALUE) {
				this.firstRequired = index;
			}
			this.lastRequired = index;
		}
		++index;
	}
	
	protected ITokenFactory[] getFactories() {
		return null;
	}
	
	@Override
	protected final ITokenFactorySupply getFactorySupply() {
		if (this.supply == null) {
			final ITokenFactory[] factories = this.getFactories();
			this.supply = new TFSStatic(factories);
		}
		return this.supply;
	}

	@Override
	protected int validateNull(int seqIndex, IToken[] foundTokens) {
		if ((seqIndex < this.firstRequired) || (seqIndex > this.lastRequired)) {
			return CONTINUE;
		}		
		if (seqIndex == this.firstRequired) {
			for (int i=this.lookAhead; i<seqIndex; ++i) {
				if (foundTokens[i] != null) {
					return this.getErrorCode();
				}
			}
			return RETURN_NULL;
		} 
		return this.getErrorCode();
	}
	
	@Override
	protected int validateEnd(int seqIndex, IToken[] foundTokens) {
		if (seqIndex > this.lastRequired) {
			return CONTINUE;
		} else {
			return this.getErrorCode();
		}
	}
}
