package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqROO;
import com.raygroupintl.bnf.TFSeqRRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.ITokenFactory;

public class TFReadArgument extends TFChoice {
	private MVersion version;
	
	private TFReadArgument(MVersion version) {
		this.version = version;
	}
	
	private static ITokenFactory getTFReadcountInstance(MVersion version) {
		return TFSeqRequired.getInstance(TFConstChar.getInstance('#'), MTFSupply.getInstance(version).expr);
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		switch(ch) {
			case '!':
			case '#':
			case '?':
			case '/':
				return MTFSupply.getInstance(version).format;
			case '"':
				return TFStringLiteral.getInstance();
			case '*':
				return TFSeqRRO.getInstance(TFConstChar.getInstance('*'), MTFSupply.getInstance(version).glvn,  MTFSupply.getInstance(version).timeout);
			case '@':
				return TFSeqRO.getInstance(MTFSupply.getInstance(version).indirection,  MTFSupply.getInstance(version).timeout);
			default: 
				return TFSeqROO.getInstance(MTFSupply.getInstance(version).glvn, getTFReadcountInstance(this.version),  MTFSupply.getInstance(version).timeout);
		}
	}

	public static TFReadArgument getInstance(MVersion version) {
		return new TFReadArgument(version);
	}
}
