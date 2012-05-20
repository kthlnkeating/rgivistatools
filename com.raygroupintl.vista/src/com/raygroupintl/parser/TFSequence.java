package com.raygroupintl.parser;

import java.lang.reflect.Constructor;
import java.util.List;

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
	
	private static final SequenceAdapter DEFAULT_ADAPTER = new SequenceAdapter() {		
		@Override
		public Token convert(List<Token> tokens) {
			return new TSequence(tokens);
		}
	}; 
		
	private TokenFactory[] factories = {};
	private RequiredFlags requiredFlags = new RequiredFlags();
	private SequenceAdapter adapter;
	private int lookAhead = 0;
	
	public TFSequence(String name) {		
		this(name, DEFAULT_ADAPTER);
	}
	
	public TFSequence(String name, SequenceAdapter adapter) {		
		super(name);
		this.adapter = adapter == null ? DEFAULT_ADAPTER : adapter;
	}
	
	public TFSequence(String name, SequenceAdapter adapter, TokenFactory... factories) {
		this(name, adapter);
		this.factories = factories;
		this.requiredFlags = new RequiredFlags(factories.length);
	}
		
	public TFSequence(String name, TokenFactory... factories) {
		this(name);
		this.factories = factories;
		this.requiredFlags = new RequiredFlags(factories.length);
	}
	
	@Override
	protected TokenFactory getLeadingFactory() {
		return this.factories[0];
	}
				
	@Override
	public void copyWoutAdapterFrom(TFBasic rhs) {
		if (rhs instanceof TFSequence) {
			TFSequence rhsCasted = (TFSequence) rhs;
			this.factories = rhsCasted.factories;
			this.requiredFlags = rhsCasted.requiredFlags;
		} else {
			throw new IllegalArgumentException("Illegal attemp to copy from " + rhs.getClass().getName() + " to " + TFSequence.class.getName());
		}
	}

	@Override
	public TFBasic getCopy(String name) {
		throw new UnsupportedOperationException("GetCopy is not implemented for " + TFSequence.class.getName());
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
	
	public void setAdapter(SequenceAdapter adapter) {
		this.adapter = adapter;
	}

	public int getSequenceCount() {
		return this.factories.length;
	}
	
	protected ValidateResult validateNext(int seqIndex, TokenStore foundTokens, Token nextToken, boolean noException) throws SyntaxErrorException {
		return ValidateResult.CONTINUE;
	}

	protected ValidateResult validateNull(int seqIndex, TokenStore foundTokens, boolean noException) throws SyntaxErrorException {
		int firstRequired = this.requiredFlags.getFirstRequiredIndex();
		int lastRequired = this.requiredFlags.getLastRequiredIndex();
		
		if ((seqIndex < firstRequired) || (seqIndex > lastRequired)) {
			return ValidateResult.CONTINUE;
		}		
		if (seqIndex == firstRequired) {
			if (noException) return ValidateResult.NULL_RESULT;
			for (int i=this.lookAhead; i<seqIndex; ++i) {
				if (foundTokens.get(i) != null) {
					throw new SyntaxErrorException(foundTokens);
				}
			}
			return ValidateResult.NULL_RESULT;
		}
		if (this.requiredFlags.isRequired(seqIndex)) {
			if (noException) return ValidateResult.NULL_RESULT;
			throw new SyntaxErrorException(foundTokens);
		} else {
			return ValidateResult.CONTINUE;
		}
	}
	
	protected boolean validateEnd(int seqIndex, TokenStore foundTokens, boolean noException) throws SyntaxErrorException {
		if (seqIndex < this.requiredFlags.getLastRequiredIndex()) {
			if (noException) return false;
			throw new SyntaxErrorException(foundTokens);
		}
		return true;
	}
	
	private ValidateResult validate(int seqIndex,TokenStore foundTokens, Token nextToken, boolean noException) throws SyntaxErrorException {
		if (nextToken == null) {
			return this.validateNull(seqIndex, foundTokens, noException);
		} else {
			return this.validateNext(seqIndex, foundTokens, nextToken, noException);			
		}
	}

	protected Token getToken(TokenStore foundTokens) {
		//for (Token token : foundTokens) {
		//	if (token != null) return this.adapter.convert(foundTokens.toList());
		//}
		for (int i=0; i<foundTokens.size(); ++i) {
			if (foundTokens.get(i) != null) return this.adapter.convert(foundTokens.toList());
		}		
		return null;
	}

	@Override
	public final Token tokenize(Text text) throws SyntaxErrorException {
		if (text.onChar()) {
			TokenStore foundTokens = new ArrayAsTokenStore(this.factories.length);
			return this.tokenize(text, 0, foundTokens, false);
		}		
		return null;
	}
	
	final Token tokenize(Text text, int firstSeqIndex, TokenStore foundTokens, boolean noException) throws SyntaxErrorException {
		int factoryCount = this.factories.length;
		for (int i=firstSeqIndex; i<factoryCount; ++i) {
			TokenFactory factory = this.factories[i];
			Token token = null;
			try {
				token = factory.tokenize(text);				
			} catch (SyntaxErrorException e) {
				if (noException) return null;
				e.addStore(foundTokens);
				throw e;
			}
				
			ValidateResult vr = this.validate(i, foundTokens, token, noException);
			if (vr == ValidateResult.BREAK) break;
			if (vr == ValidateResult.NULL_RESULT) return null;

			foundTokens.addToken(token);
			if (token != null) {				
				if (text.onEndOfText() && (i < factoryCount-1)) {
					if (! this.validateEnd(i, foundTokens, noException)) return null;
					break;
				}
			}
		}
		return this.getToken(foundTokens);	
	}
	
		
	@Override
	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = this.getConstructor(cls, List.class, TSequence.class);
		this.adapter = new SequenceAdapter() {			
			@Override
			public Token convert(List<Token> tokens) {
				try{
					return (Token) constructor.newInstance(tokens);
				} catch (Exception e) {	
					return null;
				}
			}
		};
	}
	
	@Override
	public void setAdapter(Object adapter) {
		if (adapter instanceof SequenceAdapter) {
			this.adapter = (SequenceAdapter) adapter;					
		} else {
			throw new IllegalArgumentException("Wrong adapter type " + adapter.getClass().getName());
		}
	}	
}
