package com.raygroupintl.bnf;

public abstract class TFSeq implements TokenFactory {
	protected static final int RETURN_NULL = -2;
	protected static final int RETURN_TOKEN = -1;
	protected static final int CONTINUE = 0;
	
	private TokenAdapter adapter;
	
	public void setTokenAdapter(TokenAdapter adapter) {
		this.adapter = adapter;
	}
	
	protected abstract TokenFactorySupply getFactorySupply();

	protected abstract int validateNull(int seqIndex, int lineIndex, Token[] foundTokens)  throws SyntaxErrorException;
	
	protected int validateNext(int seqIndex, int lineIndex, Token[] foundTokens, Token nextToken) throws SyntaxErrorException {
		return CONTINUE;
	}
	
	protected void validateEnd(int seqIndex, int lineIndex, Token[] foundTokens) throws SyntaxErrorException {		
	}
	
	protected Token getToken(String line, int fromIndex, Token[] foundTokens) {
		if (this.adapter == null) {
			return new TArray(foundTokens);
		} else {
			return this.adapter.convert(line, fromIndex, foundTokens);
		}
	}
	
	private int validate(int seqIndex, int lineIndex, Token[] foundTokens, Token nextToken) throws SyntaxErrorException {
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
			TokenFactorySupply supply = this.getFactorySupply();
			int factoryCount = supply.getCount();
			Token[] foundTokens = new Token[factoryCount];
			for (int i=0; i<factoryCount; ++i) {
				TokenFactory factory = supply.get(i, foundTokens);
				Token token = factory.tokenize(line, index);				
				
				int code = this.validate(i, index, foundTokens, token);
				if (code == RETURN_TOKEN) {
					break;
				}
				if (code == RETURN_NULL) {
					return null;
				}					

				foundTokens[i] = token;
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
}
