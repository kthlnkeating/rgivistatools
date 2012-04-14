package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialOR;
import com.raygroupintl.vista.token.TFSerialXYY;

public class TFLabelRef extends TFSerialXYY {	
	private MVersion version;
	
	private TFLabelRef(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		TFSerialOR envName = new TFSerialOR() {
			@Override
			protected ITokenFactory getRequired() {
				return new TFName();
			}
			
			@Override
			protected ITokenFactory getOptional() {
				return TFEnvironment.getInstance(TFLabelRef.this.version);			
			}
		};
		return new ITokenFactory[]{new TFLabel(), new TFConstChar('^'), envName};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		return new TLabelRef(foundTokens);
	}
	
	public static TFLabelRef getInstance(MVersion version) {
		return new TFLabelRef(version);
	}
}
