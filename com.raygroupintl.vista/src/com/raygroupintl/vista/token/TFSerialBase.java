package com.raygroupintl.vista.token;

import java.util.Arrays;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.struct.MError;

public abstract class TFSerialBase implements ITokenFactory {
	protected static final int RETURN_NULL = -2;
	protected static final int RETURN_TOKEN = -1;
	protected static final int CONTINUE = 0;
	
	protected int getErrorCode() {
		return MError.ERR_GENERAL_SYNTAX;
	}
	
	protected abstract ITokenFactorySupply getFactorySupply();

	protected abstract int getCodeNextIsNull(IToken[] foundTokens);
	
	protected int getCodeNextIsFound(IToken[] foundTokens, IToken nextToken) {
		return CONTINUE;
	}
	
	protected abstract int getCodeStringEnds(IToken[] foundTokens);
	
	protected IToken getToken(IToken[] foundTokens) {
		return new TArray(foundTokens);
	}
	
	private int validate(IToken[] foundTokens, IToken nextToken) {
		if (nextToken == null) {
			return this.getCodeNextIsNull(foundTokens);
		} else {
			return this.getCodeNextIsFound(foundTokens, nextToken);			
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
			IToken[] foundTokens = new IToken[0];
			ITokenFactorySupply supply = this.getFactorySupply();
			ITokenFactory factory = supply.get(foundTokens);			
			while (factory != null) {
				IToken token = factory.tokenize(line, index);				
				
				if ((token != null) && (token instanceof TSyntaxError)) {
					return this.getTokenWhenSyntaxError(foundTokens, (TSyntaxError) token, fromIndex);
				}

				int code = this.validate(foundTokens, token);
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
				factory = supply.get(foundTokens);								
				if (token == null) continue;				
				index += token.getStringSize();					
				
				if ((index >= endIndex) && (factory != null)) {
					int endCode = this.getCodeStringEnds(foundTokens);
					if (endCode > 0) {
						return TSyntaxError.getInstance(endCode, line, index, fromIndex);
					}
					break;
				}
			}
			int count = supply.getCount();
			if (foundTokens.length < count) {
				foundTokens = Arrays.copyOf(foundTokens, count);
			}
			return this.getToken(foundTokens);
		}		
		return null;
	}	
}
