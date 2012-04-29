package com.raygroupintl.bnf;

import java.util.Arrays;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;

public abstract class TFSeqStatic extends TFSeq {
	private ITokenFactorySupply supply;
	private boolean[] requiredFlags = {};
	private int firstRequired = Integer.MAX_VALUE;
	private int lastRequired = Integer.MIN_VALUE;
	private int lookAhead = 0;
	
	public TFSeqStatic() {		
	}
	
	public TFSeqStatic(ITokenFactory... factories) {
		this.supply = new TFSStatic(factories);
		if (factories.length > this.requiredFlags.length) {
			this.requiredFlags = Arrays.copyOf(this.requiredFlags, factories.length);
		}
	}
		
	public void setFactories(ITokenFactory[] factories) {
		this.supply = new TFSStatic(factories);		
		if (factories.length > this.requiredFlags.length) {
			this.requiredFlags = Arrays.copyOf(this.requiredFlags, factories.length);
		}
	}

	private void update() {
		this.firstRequired = Integer.MAX_VALUE;
		this.lastRequired = Integer.MIN_VALUE;
		int index = 0;
		for (boolean b : this.requiredFlags) {
			if (b) {
				if (this.firstRequired == Integer.MAX_VALUE) {
					this.firstRequired = index;
				}
				this.lastRequired = index;
			}
			++index;
		}		
	}
	
	public void setRequiredFlags(boolean[] flags) {
		if ((this.supply != null) && (this.supply.getCount() < flags.length)) {
			this.requiredFlags = Arrays.copyOf(flags, this.supply == null ? 0 : this.supply.getCount());
		} else {
			this.requiredFlags = flags;
		}
		this.update();
	}
	
	public void setLookAhead(int index) {
		this.lookAhead = index;
	}
	
	protected void setRequiredAll() {
		this.firstRequired = 0;
		this.lastRequired = Integer.MAX_VALUE;
		this.requiredFlags = new boolean[5];
		Arrays.fill(this.requiredFlags, true);
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
		if (this.requiredFlags[seqIndex]) {
			return this.getErrorCode();
		} else {
			return CONTINUE;
		}
	}
	
	@Override
	protected int validateEnd(int seqIndex, IToken[] foundTokens) {
		if (seqIndex >= this.lastRequired) {
			return CONTINUE;
		} else {
			return this.getErrorCode();
		}
	}
	
	@Override
	protected IToken getToken(IToken[] foundTokens) {
		for (IToken token : foundTokens) {
			if (token != null) return super.getToken(foundTokens);
		}
		return null;
	}
	
	public boolean isInitialize() {
		return this.supply != null;
	}
}
