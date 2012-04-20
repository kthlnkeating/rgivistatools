package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFChar;
import com.raygroupintl.bnf.TFConstString;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.vista.fnds.ITokenFactory;

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
				TFActualList.getInstance(MVersion.CACHE)					
		};
	}
	
	public static TFCacheClassMethod getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TFCacheClassMethod();
		}
		return INSTANCE;
	}
}
