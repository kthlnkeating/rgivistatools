package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFLvn extends TFSeqRO {
	private MVersion version;
	
	private TFLvn(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		ITokenFactory f1 = TFActualList.getInstance(this.version);
		if (this.version == MVersion.CACHE) {
			ITokenFactory methodOrProperty = TFSeqRequired.getInstance(TFConstChar.getInstance('.'), TFName.getInstance());
			ITokenFactory methodOrPropertyList = TFList.getInstance(methodOrProperty);
			return new ITokenFactory[]{TFSeqRO.getInstance(TFName.getInstance(), methodOrPropertyList), f1};		
		} else {		
			return new ITokenFactory[]{TFName.getInstance(), f1};
		}
	}

	protected IToken getToken(IToken requiredToken) {
		if (this.version == MVersion.CACHE) {
			return requiredToken;
		} else {
			return new TLocal(requiredToken);
		}
	}
	
	protected IToken getToken(IToken requiredToken, IToken optionalToken) {
		return new TLocal(requiredToken, optionalToken);
	}				

	public static TFLvn getInstance(MVersion version) {
		return new TFLvn(version);
	}
}

