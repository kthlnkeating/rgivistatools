package com.raygroupintl.m.token;

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
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{MTFSupply.getInstance(TFLabelRef.this.version).environment, new TFName()};
			}
		};
		return new ITokenFactory[]{MTFSupply.getInstance(version).label, new TFConstChar('^'), envName};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		return new TLabelRef(foundTokens);
	}
	
	public static TFLabelRef getInstance(MVersion version) {
		return new TFLabelRef(version);
	}
}
