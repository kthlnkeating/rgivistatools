package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFCharAccumulating;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFFormat extends TFParallelCharBased {
	private static ITokenFactory getTFTabFormat() {
		return TFAllRequired.getInstance(TFConstChar.getInstance('?'), TFExpr.getInstance());
	}
		
	private static ITokenFactory getTFPositionXTabFormat() {
		return TFSerialRO.getInstance(TFCharAccumulating.getInstance('!','#'), getTFTabFormat());
	}
		
	@Override
	protected ITokenFactory getFactory(char ch) {
		switch(ch) {
			case '!':
			case '#':
				return getTFPositionXTabFormat();
			case '?':
				return getTFTabFormat();
			case '/':
				return null;
			default:
				return null;
		}
	}
	
	public static TFFormat getInstance() {
		return  new TFFormat();
	}
}
