package com.raygroupintl.bnf;

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
		this.requiredFlags = new boolean[factories.length];
	}
		
	public void setFactories(TokenFactory[] factories, boolean[] requiredFlags) {
		this.factories = factories;
		this.setRequiredFlags(requiredFlags);
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
	
	public void setRequiredFlags(boolean[] requiredFlags) {
		if (requiredFlags.length != this.factories.length) throw new IllegalArgumentException();
		this.requiredFlags = requiredFlags;
		this.update();
	}
	
	public void copyFrom(TFSequenceStatic source) {
		this.setFactories(source.factories, source.requiredFlags);
	}
	
	public void setLookAhead(int index) {
		this.lookAhead = index;
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
