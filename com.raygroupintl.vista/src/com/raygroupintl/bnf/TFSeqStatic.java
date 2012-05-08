package com.raygroupintl.bnf;

import java.util.Arrays;

import com.raygroupintl.vista.struct.MError;

public final class TFSeqStatic extends TFSeq {
	private TokenFactorySupply supply;
	private boolean[] requiredFlags = {};
	private int firstRequired = Integer.MAX_VALUE;
	private int lastRequired = Integer.MIN_VALUE;
	private int lookAhead = 0;
	
	public TFSeqStatic() {		
	}
	
	public TFSeqStatic(TokenFactory... factories) {
		this.supply = new TFSStatic(factories);
		if (factories.length > this.requiredFlags.length) {
			this.requiredFlags = Arrays.copyOf(this.requiredFlags, factories.length);
		}
	}
		
	public void setFactories(TokenFactory[] factories) {
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
	
	@Override
	protected final TokenFactorySupply getFactorySupply() {
		return this.supply;
	}

	@Override
	protected int validateNull(int seqIndex, int lineIndex, Token[] foundTokens) throws SyntaxErrorException {
		if ((seqIndex < this.firstRequired) || (seqIndex > this.lastRequired)) {
			return CONTINUE;
		}		
		if (seqIndex == this.firstRequired) {
			for (int i=this.lookAhead; i<seqIndex; ++i) {
				if (foundTokens[i] != null) {
					throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX, lineIndex);
				}
			}
			return RETURN_NULL;
		}
		if (this.requiredFlags[seqIndex]) {
			throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX, lineIndex);
		} else {
			return CONTINUE;
		}
	}
	
	@Override
	protected void validateEnd(int seqIndex, int lineIndex, Token[] foundTokens) throws SyntaxErrorException {
		if (seqIndex < this.lastRequired) {
			throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX, lineIndex);
		}
	}
	
	@Override
	protected Token getToken(String line, int fromIndex, Token[] foundTokens) {
		for (Token token : foundTokens) {
			if (token != null) return super.getToken(line, fromIndex, foundTokens);
		}
		return null;
	}
}
