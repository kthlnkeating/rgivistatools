package com.raygroupintl.vista.mtoken;

import java.util.Arrays;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TList;

public abstract class TFDelimitedList extends TFSerialRO {
	protected abstract ITokenFactory getElementFactory();
	
	protected abstract ITokenFactory getDelimitedFactory();
	
	@Override
	protected ITokenFactory getRequired() {
		return this.getElementFactory();
	}

	@Override
	protected ITokenFactory getOptional() {
		return new TFAllRequired() {			
			@Override
			protected IToken getToken(IToken[] foundTokens) {
				return new TList(Arrays.asList(foundTokens));
			}
			
			@Override
			protected ITokenFactory[] getFactories() {
				ITokenFactory d = TFDelimitedList.this.getDelimitedFactory();
				ITokenFactory e = TFDelimitedList.this.getElementFactory();
				return new ITokenFactory[]{d, e};
			}
		};
	}

	protected IToken getToken(TList listToken) {
		return listToken;
	}
	
	@Override
	protected IToken getTokenRequired(IToken requiredToken) {
		return this.getToken(new TList(requiredToken));
	}
	
	@Override
	protected IToken getTokenBoth(IToken requiredToken, IToken optionalToken) {
		TList result = (TList) optionalToken;
		result.add(0, requiredToken);
		return  this.getToken(result);
	}
}
