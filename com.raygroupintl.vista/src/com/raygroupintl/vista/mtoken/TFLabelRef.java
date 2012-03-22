package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialOR;
import com.raygroupintl.vista.token.TFSerialXYY;

public class TFLabelRef extends TFSerialXYY {	
	@Override
	protected ITokenFactory[] getFactories() {
		TFSerialOR envName = new TFSerialOR() {
			@Override
			protected ITokenFactory getRequired() {
				return new TFName();
			}
			
			@Override
			protected ITokenFactory getOptional() {
				return new TFEnvironment();			
			}
		};
		return new ITokenFactory[]{new TFLabel(), new TFConstChar('^'), envName};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		return new TLabelRef(foundTokens);
	}
}
