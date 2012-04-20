package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFLvn extends TFSeqRO {
	private MVersion version;
	
	private TFLvn(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory getRequired() {
		if (this.version == MVersion.CACHE) {
			ITokenFactory methodOrProperty = TFSeqRequired.getInstance(TFConstChar.getInstance('.'), TFName.getInstance());
			ITokenFactory methodOrPropertyList = TFList.getInstance(methodOrProperty);
			return TFSeqRO.getInstance(TFName.getInstance(), methodOrPropertyList);		
		} else {		
			return TFName.getInstance();
		}
	}
	
	@Override
	protected ITokenFactory getOptional() {
		return TFActualList.getInstance(this.version);
		//return new TFInParantheses() {						
		//	@Override
		//	protected ITokenFactory getInnerfactory() {
		//		return new TFCommaDelimitedList() {								
		//			@Override
		//			protected ITokenFactory getElementFactory() {
		//				return TFExpr.getInstance(TFLvn.this.version);
		//			}
		//		};
		//	}
		//};
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

