package com.raygroupintl.vista.mtoken;

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
		return TFSeqRequired.getInstance(TFConstChar.getInstance('#'), MTFSupply.getInstance(version).getTFExpr());
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		switch(ch) {
			case '!':
			case '#':
			case '?':
			case '/':
				return TFFormat.getInstance(this.version);
			case '"':
				return TFStringLiteral.getInstance();
			case '*':
				return TFSeqRRO.getInstance(TFConstChar.getInstance('*'), MTFSupply.getInstance(version).getTFGlvn(), TFTimeout.getInstance(this.version));
			case '@':
				return TFSeqRO.getInstance(TFIndirection.getInstance(this.version), TFTimeout.getInstance(this.version));
			default: 
				return TFSeqROO.getInstance(MTFSupply.getInstance(version).getTFGlvn(), getTFReadcountInstance(this.version), TFTimeout.getInstance(this.version));
		}
	}

	public static TFReadArgument getInstance(MVersion version) {
		return new TFReadArgument(version);
	}
}
