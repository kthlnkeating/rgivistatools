package com.raygroupintl.bnf;

import java.util.Arrays;

import com.raygroupintl.vista.struct.MError;

public final class TFSequenceStatic extends TFSequence {
	private TokenFactory[] factories = {};
	private boolean[] requiredFlags = {};
	private int firstRequired = Integer.MAX_VALUE;
	private int lastRequired = Integer.MIN_VALUE;
	private int lookAhead = 0;
	
	public TFSequenceStatic() {		
	}
	
	public TFSequenceStatic(TokenFactory... factories) {
		this.factories = factories;
		if (factories.length > this.requiredFlags.length) {
			this.requiredFlags = Arrays.copyOf(this.requiredFlags, factories.length);
		}
	}
		
	public void setFactories(TokenFactory[] factories) {
		this.factories = factories;
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
		if ((this.factories.length > 0) && (this.factories.length < flags.length)) {
			this.requiredFlags = Arrays.copyOf(flags, this.factories.length);
		} else {
			this.requiredFlags = flags;
		}
		this.update();
	}
	
	public void copyFrom(TFSequenceStatic source) {
		this.setFactories(source.factories);
		this.setRequiredFlags(source.requiredFlags);
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
	protected ValidateResult validateNull(int seqIndex, TokenStore foundTokens) throws SyntaxErrorException {
		if ((seqIndex < this.firstRequired) || (seqIndex > this.lastRequired)) {
			return ValidateResult.CONTINUE;
		}		
		if (seqIndex == this.firstRequired) {
			for (int i=this.lookAhead; i<seqIndex; ++i) {
				if (foundTokens.get(i) != null) {
					throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX, foundTokens);
				}
			}
			return ValidateResult.NULL_RESULT;
		}
		if (this.requiredFlags[seqIndex]) {
			throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX, foundTokens);
		} else {
			return ValidateResult.CONTINUE;
		}
	}
	
	@Override
	protected void validateEnd(int seqIndex, TokenStore foundTokens) throws SyntaxErrorException {
		if (seqIndex < this.lastRequired) {
			throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX, foundTokens);
		}
	}
	
	@Override
	protected Token getToken(TokenStore foundTokens) {
		for (Token token : foundTokens) {
			if (token != null) return super.getToken(foundTokens);
		}
		return null;
	}

	@Override
	protected TokenFactory getTokenFactory(int i, TokenStore foundTokens) {
		return this.factories[i];
	}

	@Override
	protected int getExpectedTokenCount() {
		return this.factories.length;
	}
}
