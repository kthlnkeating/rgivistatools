package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFBasic;
import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.ITokenFactory;

public class TFFormat extends TFChoice {
	private MVersion version;
	
	private TFFormat(MVersion version) {
		this.version = version;
	}
	
	private static ITokenFactory getTFTabFormat(MVersion version) {
		return TFSeqRequired.getInstance(TFConstChar.getInstance('?'), TFExpr.getInstance(version));
	}
		
	private static ITokenFactory getTFPositionXTabFormat(MVersion version) {
		return TFSeqRO.getInstance(TFBasic.getInstance('!','#'), getTFTabFormat(version));
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
