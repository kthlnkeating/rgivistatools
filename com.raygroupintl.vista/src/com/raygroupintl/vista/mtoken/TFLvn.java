package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFLvn extends TFSerialRO {
	@Override
	protected ITokenFactory getRequired() {
		return new TFName();
	}
	
	@Override
	protected ITokenFactory getOptional() {
		return new TFInParantheses() {						
			@Override
			protected ITokenFactory getInnerfactory() {
				return new TFCommaDelimitedList() {								
					@Override
					protected ITokenFactory getElementFactory() {
						return new TFExpr();
					}
				};
			}
		};
	}
	
	protected IToken getToken(IToken requiredToken) {
		return new TLocal(requiredToken);
	}
	
	protected IToken getToken(IToken requiredToken, IToken optionalToken) {
		return new TLocal(requiredToken, optionalToken);
	}				
}
