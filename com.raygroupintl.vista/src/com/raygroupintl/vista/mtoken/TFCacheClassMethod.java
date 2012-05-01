package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFChar;
import com.raygroupintl.bnf.TFConstString;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.ITokenFactory;

public class TFCacheClassMethod extends TFSeqRequired {
	private static TFCacheClassMethod INSTANCE;
	
	private TFCacheClassMethod() {		
	}
	
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[] {
				new TFConstString("##class"),
				TFChar.LEFT_PAR,
				TFSeqRO.getInstance(
						TFName.getInstance(),
						TFList.getInstance(TFSeqRequired.getInstance(TFChar.DOT, TFName.getInstance()))),
				TFChar.RIGHT_PAR,
				TFChar.DOT,
				TFName.getInstance(),
				MTFSupply.getInstance(MVersion.CACHE).actuallist				
		};
	}
	
	public static TFCacheClassMethod getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TFCacheClassMethod();
		}
		return INSTANCE;
	}
}
