package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqOR;
import com.raygroupintl.bnf.TFSeqXYY;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFLabelRef extends TFSeqXYY {	
	private MVersion version;
	
	private TFLabelRef(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		TFSeqOR envName = new TFSeqOR() {
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
