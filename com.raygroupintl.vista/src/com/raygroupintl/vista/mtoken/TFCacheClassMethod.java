package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFChar;
import com.raygroupintl.vista.token.TFConstString;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFCacheClassMethod extends TFAllRequired {
	private static TFCacheClassMethod INSTANCE;
	
	private TFCacheClassMethod() {		
	}
	
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[] {
				new TFConstString("##class"),
				TFChar.LEFT_PAR,
				TFSerialRO.getInstance(
						TFName.getInstance(),
						TFList.getInstance(TFAllRequired.getInstance(TFChar.DOT, TFName.getInstance()))),
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
