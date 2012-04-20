package com.raygroupintl.bnf;

import java.util.Arrays;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.struct.MError;

public abstract class TFSeq implements ITokenFactory {
	protected static final int RETURN_NULL = -2;
	protected static final int RETURN_TOKEN = -1;
	protected static final int CONTINUE = 0;
	
	protected int getErrorCode() {
		return MError.ERR_GENERAL_SYNTAX;
	}
	
	protected abstract ITokenFactorySupply getFactorySupply();

	protected abstract int validateNull(int seqIndex, IToken[] foundTokens);
	
	protected int validateNext(int seqIndex, IToken[] foundTokens, IToken nextToken) {
		return CONTINUE;
	}
	
	protected abstract int validateEnd(int seqIndex, IToken[] foundTokens);
	
	protected IToken getToken(IToken[] foundTokens) {
		return new TArray(foundTokens);
	}
	
	private int validate(int seqIndex, IToken[] foundTokens, IToken nextToken) {
		if (nextToken == null) {
			return this.validateNull(seqIndex, foundTokens);
		} else {
			return this.validateNext(seqIndex, foundTokens, nextToken);			
		}
	}
	
	protected IToken getTokenWhenSyntaxError(IToken[] found, TSyntaxError error, int fromIndex) {
		error.setFromIndex(fromIndex);
		return error;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			int index = fromIndex;
			ITokenFactorySupply supply = this.getFactorySupply();
			int factoryCount = supply.getCount();
			IToken[] foundTokens = new IToken[0];
			for (int i=0; i<factoryCount; ++i) {
				ITokenFactory factory = supply.get(i, foundTokens);
				assert(factory != null);
				IToken token = factory.tokenize(line, index);				
				
				if ((token != null) && (token instanceof TSyntaxError)) {
					return this.getTokenWhenSyntaxError(foundTokens, (TSyntaxError) token, fromIndex);
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

				foundTokens = Arrays.copyOf(foundTokens, foundTokens.length+1);
				foundTokens[foundTokens.length-1] = token;
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
			if (foundTokens.length < factoryCount) {
				foundTokens = Arrays.copyOf(foundTokens, factoryCount);
			}
			return this.getToken(foundTokens);
		}		
		return null;
	}	
}
