package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenArray;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TArray;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFConstString;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFIndirection extends TFSerialRO {
	private MVersion version;
	
	private TFIndirection(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory getRequired() {
		return TFAllRequired.getInstance(TFConstChar.getInstance('@'), TFExprAtom.getInstance(this.version));
	}
	
	@Override
	protected ITokenFactory getOptional() {
		return new TFAllRequired() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{new TFConstString("@("), TFExprList.getInstance(TFIndirection.this.version), new TFConstChar(')')};
			}
		};
	}
	
	@Override
	protected IToken getTokenRequired(IToken requiredToken) {
		TArray t = (TArray) requiredToken;
		return new TIndirection(t.get(1));
	}
	
	@Override
	protected IToken getTokenBoth(IToken requiredToken, IToken optionalToken) {
		TArray tReqArray = (TArray) requiredToken;
		ITokenArray tOptArray = (ITokenArray) optionalToken;
		IToken subscripts = tOptArray.get(1);
		return new TIndirection(tReqArray.get(1), subscripts);
	}

	public static TFIndirection getInstance(MVersion version) {
		return new TFIndirection(version);
	}
}
