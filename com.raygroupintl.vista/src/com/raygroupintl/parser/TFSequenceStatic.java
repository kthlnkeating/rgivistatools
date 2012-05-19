package com.raygroupintl.parser;

public class TFSequenceStatic extends TFSequence {
	private final static class RequiredFlags {
		private boolean[] flags;
		private int firstRequired = Integer.MAX_VALUE;
		private int lastRequired = Integer.MIN_VALUE;

		public RequiredFlags() {
			this(0);
		}

		public RequiredFlags(int size) {
			flags = new boolean[size];
		}

		public void set(boolean[] flags) {
			this.flags = flags;
			this.firstRequired = Integer.MAX_VALUE;			
			this.lastRequired = Integer.MIN_VALUE;			
			int index = 0;
			for (boolean b : flags) {
				if (b) {
					if (this.firstRequired == Integer.MAX_VALUE) {
						this.firstRequired = index;
					}
					this.lastRequired = index;
				}
				++index;
			}		
		}
		
		public int getFirstRequiredIndex() {
			return this.firstRequired;
		}
		
		public int getLastRequiredIndex() {
			return this.lastRequired;
		}
		
		public boolean isRequired(int i) {
			return this.flags[i];
		}		
	}
	
	private TokenFactory[] factories = {};
	private RequiredFlags requiredFlags = new RequiredFlags();
	private int lookAhead = 0;
	
	public TFSequenceStatic(String name) {		
		super(name);
	}
	
	public TFSequenceStatic(String name, SequenceAdapter adapter) {		
		super(name, adapter);
	}
	
	public TFSequenceStatic(String name, SequenceAdapter adapter, TokenFactory... factories) {
		super(name, adapter);
		this.factories = factories;
		this.requiredFlags = new RequiredFlags(factories.length);
	}
		
	private TFSequenceStatic(String name, SequenceAdapter adapter, RequiredFlags flags, TokenFactory... factories) {
		super(name, adapter);
		this.factories = factories;
		this.requiredFlags = flags;
	}
		
	public TFSequenceStatic(String name, TokenFactory... factories) {
		super(name);
		this.factories = factories;
		this.requiredFlags = new RequiredFlags(factories.length);
	}
			
	@Override
	public void copyWoutAdapterFrom(TFBasic rhs) {
		if (rhs instanceof TFSequenceStatic) {
			TFSequenceStatic rhsCasted = (TFSequenceStatic) rhs;
			this.factories = rhsCasted.factories;
			this.requiredFlags = rhsCasted.requiredFlags;
		} else {
			throw new IllegalArgumentException("Illegal attemp to copy from " + rhs.getClass().getName() + " to " + TFSequenceStatic.class.getName());
		}
	}

	@Override
	public TFBasic getCopy(String name) {
		return new TFSequenceStatic(name, this.adapter, this.requiredFlags, this.factories);
	}

	public void setFactories(TokenFactory[] factories, boolean[] requiredFlags) {
		if (requiredFlags.length != factories.length) throw new IllegalArgumentException();
		this.factories = factories;
		this.requiredFlags.set(requiredFlags);
	}

	public void setRequiredFlags(boolean... requiredFlags) {
		if (requiredFlags.length != this.factories.length) throw new IllegalArgumentException();
		this.requiredFlags.set(requiredFlags);
	}
	
	public void setLookAhead(int index) {
		this.lookAhead = index;
	}
	
	@Override
	protected ValidateResult validateNull(int seqIndex, TokenStore foundTokens) throws SyntaxErrorException {
		int firstRequired = this.requiredFlags.getFirstRequiredIndex();
		int lastRequired = this.requiredFlags.getLastRequiredIndex();
		
		if ((seqIndex < firstRequired) || (seqIndex > lastRequired)) {
			return ValidateResult.CONTINUE;
		}		
		if (seqIndex == firstRequired) {
			for (int i=this.lookAhead; i<seqIndex; ++i) {
				if (foundTokens.get(i) != null) {
					throw new SyntaxErrorException(foundTokens);
				}
			}
			return ValidateResult.NULL_RESULT;
		}
		if (this.requiredFlags.isRequired(seqIndex)) {
			throw new SyntaxErrorException(foundTokens);
		} else {
			return ValidateResult.CONTINUE;
		}
	}
	
	@Override
	protected void validateEnd(int seqIndex, TokenStore foundTokens) throws SyntaxErrorException {
		if (seqIndex < this.requiredFlags.getLastRequiredIndex()) {
			throw new SyntaxErrorException(foundTokens);
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
