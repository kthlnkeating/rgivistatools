package com.raygroupintl.bnf;

import com.raygroupintl.vista.struct.MError;

public abstract class TFSeq implements TokenFactory {
	protected static final int RETURN_NULL = -2;
	protected static final int RETURN_TOKEN = -1;
	protected static final int CONTINUE = 0;
	
	private TokenAdapter adapter;
	
	public void setTokenAdapter(TokenAdapter adapter) {
		this.adapter = adapter;
	}
	
	protected int getErrorCode() {
		return MError.ERR_GENERAL_SYNTAX;
	}
	
	protected abstract TokenFactorySupply getFactorySupply();

	protected abstract int validateNull(int seqIndex, Token[] foundTokens);
	
	protected int validateNext(int seqIndex, Token[] foundTokens, Token nextToken) {
		return CONTINUE;
	}
	
	protected abstract int validateEnd(int seqIndex, Token[] foundTokens);
	
	protected Token getToken(String line, int fromIndex, Token[] foundTokens) {
		if (this.adapter == null) {
			return new TArray(foundTokens);
		} else {
			return this.adapter.convert(line, fromIndex, foundTokens);
		}
	}
	
	private int validate(int seqIndex, Token[] foundTokens, Token nextToken) {
		if (nextToken == null) {
			return this.validateNull(seqIndex, foundTokens);
		} else {
			return this.validateNext(seqIndex, foundTokens, nextToken);			
		}
	}
	
	protected Token getTokenWhenSyntaxError(int seqIndex, Token[] found, TSyntaxError error, int fromIndex) {
		error.setFromIndex(fromIndex);
		return error;
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
				assert(factory != null);
				Token token = factory.tokenize(line, index);				
				
				if ((token != null) && (token instanceof TSyntaxError)) {
					return this.getTokenWhenSyntaxError(i, foundTokens, (TSyntaxError) token, fromIndex);
				}

				int code = this.validate(i, foundTokens, token);
				if (code == RETURN_TOKEN) {
					break;
				}
				if (code == RETURN_NULL) {
					return null;
				}					
				if (code > 0) {
					return TSyntaxError.getInstance(code, line, index, fromIndex);
				}

				foundTokens[i] = token;
				if (token == null) continue;				
				index += token.getStringSize();					
				
				if ((index >= endIndex) && (i < factoryCount-1)) {
					int endCode = this.validateEnd(i, foundTokens);
					if (endCode > 0) {
						return TSyntaxError.getInstance(endCode, line, index, fromIndex);
					}
					break;
				}
			}
			return this.getToken(line, fromIndex, foundTokens);
		}		
		return null;
	}	
}
