package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFBasic;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFFormat extends TFParallelCharBased {
	private MVersion version;
	
	private TFFormat(MVersion version) {
		this.version = version;
	}
	
	private static ITokenFactory getTFTabFormat(MVersion version) {
		return TFAllRequired.getInstance(TFConstChar.getInstance('?'), TFExpr.getInstance(version));
	}
		
	private static ITokenFactory getTFPositionXTabFormat(MVersion version) {
		return TFSerialRO.getInstance(TFBasic.getInstance('!','#'), getTFTabFormat(version));
	}
		
	@Override
	protected ITokenFactory getFactory(char ch) {
		switch(ch) {
			case '!':
			case '#':
				return getTFPositionXTabFormat(version);
			case '?':
				return getTFTabFormat(version);
			case '/':
				return null;
			default:
				return null;
		}
	}
	
	public static TFFormat getInstance(MVersion version) {
		return  new TFFormat(version);
	}
}
