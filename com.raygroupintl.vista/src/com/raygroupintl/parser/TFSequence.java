package com.raygroupintl.parser;

import java.lang.reflect.Constructor;

import com.raygroupintl.parser.annotation.AdapterSupply;

public class TFSequence extends TFBasic {
	public enum ValidateResult {
		CONTINUE, BREAK, NULL_RESULT
	}

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
	private SequenceAdapter adapter;
	
	public TFSequence(String name) {		
		super(name);
	}
	
	public TFSequence(String name, TokenFactory... factories) {
		this(name);
		this.factories = factories;
		this.requiredFlags = new RequiredFlags(factories.length);
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
	
	@Override
	public int getSequenceCount() {
		return this.factories.length;
	}
	
	protected ValidateResult validateNull(int seqIndex, TSequence foundTokens, boolean noException) throws SyntaxErrorException {
		int firstRequired = this.requiredFlags.getFirstRequiredIndex();
		int lastRequired = this.requiredFlags.getLastRequiredIndex();
		
		if ((seqIndex < firstRequired) || (seqIndex > lastRequired)) {
			return ValidateResult.CONTINUE;
		}		
		if (seqIndex == firstRequired) {
			if (noException) return ValidateResult.NULL_RESULT;
			if (! foundTokens.isAllNull()) {
				throw new SyntaxErrorException();
			}
			return ValidateResult.NULL_RESULT;
		}
		if (this.requiredFlags.isRequired(seqIndex)) {
			if (noException) return ValidateResult.NULL_RESULT;
			throw new SyntaxErrorException();
		} else {
			return ValidateResult.CONTINUE;
		}
	}
	
	protected boolean validateEnd(int seqIndex, TSequence foundTokens, boolean noException) throws SyntaxErrorException {
		if (seqIndex < this.requiredFlags.getLastRequiredIndex()) {
			if (noException) return false;
			throw new SyntaxErrorException();
		}
		return true;
	}
	
	protected final TSequence getToken(TSequence foundTokens) {
		if (foundTokens.isAllNull()) {
			return null;
		} else {
			if (this.adapter != null) {
				return this.adapter.convert(foundTokens);
			} else {
				return foundTokens;
			}
		}		
	}

	@Override
	public final TSequence tokenize(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			TSequence foundTokens = adapterSupply.newSequence(this.factories.length);
			return this.tokenize(text, adapterSupply, 0, foundTokens, false);
		}		
		return null;
	}
	
	@Override
	public final TSequence tokenizeRaw(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			TSequence foundTokens = adapterSupply.newSequence(this.factories.length);
			return this.tokenizeRaw(text, adapterSupply, 0, foundTokens, false);
		}		
		return null;
	}
	
	final TSequence tokenizeCommon(Text text, AdapterSupply adapterSupply, int firstSeqIndex, TSequence foundTokens, boolean noException) throws SyntaxErrorException {
		int factoryCount = this.factories.length;
		for (int i=firstSeqIndex; i<factoryCount; ++i) {
			TokenFactory factory = this.factories[i];
			Token token = null;
			try {
				token = factory.tokenize(text, adapterSupply);				
			} catch (SyntaxErrorException e) {
				if (noException) return null;
				throw e;
			}
			
			if (token == null) {
				ValidateResult vr = this.validateNull(i, foundTokens, noException);
				if (vr == ValidateResult.BREAK) break;
				if (vr == ValidateResult.NULL_RESULT) return null;
			}

			foundTokens.addToken(token);
			if (token != null) {				
				if (text.onEndOfText() && (i < factoryCount-1)) {
					if (! this.validateEnd(i, foundTokens, noException)) return null;
					break;
				}
			}
		}
		if (foundTokens.isAllNull()) return null;
		return foundTokens;
	}
	
	final TSequence tokenize(Text text, AdapterSupply adapterSupply, int firstSeqIndex, TSequence foundTokens, boolean noException) throws SyntaxErrorException {
		TSequence result = this.tokenizeCommon(text, adapterSupply, firstSeqIndex, foundTokens, noException);
		if (result == null) {
			return null;
		} else {		
			return this.getToken(result);	
		}
	}
	
	final TSequence tokenizeRaw(Text text, AdapterSupply adapterSupply, int firstSeqIndex, TSequence foundTokens, boolean noException) throws SyntaxErrorException {
		TSequence result = this.tokenizeCommon(text, adapterSupply, firstSeqIndex, foundTokens, noException);
		return result;	
	}
	
	@Override
	protected Token convert(Token token) {
		if ((this.adapter != null) && (token instanceof TSequence)) {
			return this.adapter.convert(token); 
		} else {
			return token;
		}
	}

	private static SequenceAdapter getAdapter(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = getConstructor(cls, Token.class, TSequence.class);
		return new SequenceAdapter() {			
			@Override
			public TSequence convert(Token token) {
				try{
					return (TSequence) constructor.newInstance(token);
				} catch (Exception e) {	
					return null;
				}
			}
		};
		
	}
		
	@Override
	public void setTargetType(Class<? extends Token> cls) {
		this.adapter = getAdapter(cls);
	}
	
	public TokenFactory getFactory(int index) {
		return this.factories[index];
	}
}
