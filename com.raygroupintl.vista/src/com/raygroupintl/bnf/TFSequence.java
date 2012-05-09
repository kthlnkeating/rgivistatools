package com.raygroupintl.bnf;

public abstract class TFSequence extends TokenFactory {
	public enum ValidateResult {
		CONTINUE, BREAK, NULL_RESULT
	}

	private SequenceAdapter adapter;

	public TFSequence() {
		super();
	}

	protected abstract TokenFactory getTokenFactory(int i, TokenStore foundTokens);

	protected abstract int getExpectedTokenCount();

	public void setAdapter(SequenceAdapter adapter) {
		this.adapter = adapter;
	}

	protected abstract ValidateResult validateNull(int seqIndex, int lineIndex, TokenStore foundTokens) throws SyntaxErrorException;

	protected ValidateResult validateNext(int seqIndex, int lineIndex, TokenStore foundTokens, Token nextToken) throws SyntaxErrorException {
		return ValidateResult.CONTINUE;
	}

	protected void validateEnd(int seqIndex, int lineIndex, TokenStore foundTokens) throws SyntaxErrorException {		
	}

	protected Token getToken(String line, int fromIndex, TokenStore foundTokens) {
		if (this.adapter == null) {
			return new TArray(foundTokens.toArray());
		} else {
			return this.adapter.convert(foundTokens.toArray());
		}
	}

	private ValidateResult validate(int seqIndex, int lineIndex, TokenStore foundTokens, Token nextToken) throws SyntaxErrorException {
		if (nextToken == null) {
			return this.validateNull(seqIndex, lineIndex, foundTokens);
		} else {
			return this.validateNext(seqIndex, lineIndex, foundTokens, nextToken);			
		}
	}

	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			int index = fromIndex;
			int factoryCount = this.getExpectedTokenCount();
			TokenStore foundTokens = new ArrayAsTokenStore(factoryCount);
			for (int i=0; i<factoryCount; ++i) {
				TokenFactory factory = this.getTokenFactory(i, foundTokens);
				Token token = factory.tokenize(line, index);				
				
				ValidateResult vr = this.validate(i, index, foundTokens, token);
				if (vr == ValidateResult.BREAK) break;
				if (vr == ValidateResult.NULL_RESULT) return null;
	
				foundTokens.addToken(token);
				if (token != null) {				
					index += token.getStringSize();					
					if ((index >= endIndex) && (i < factoryCount-1)) {
						this.validateEnd(i, index, foundTokens);
						break;
					}
				}
			}
			return this.getToken(line, fromIndex, foundTokens);
		}		
		return null;
	}
	
	@Override
	public void extractTo(String line, int fromIndex, TokenStore store) throws SyntaxErrorException {
	}
}