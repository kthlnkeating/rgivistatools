package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TFSerialROO;
import com.raygroupintl.vista.token.TFSerialRRO;

public class TFReadArgument extends TFParallelCharBased {
	private MVersion version;
	
	private TFReadArgument(MVersion version) {
		this.version = version;
	}
	
	private static ITokenFactory getTFReadcountInstance(MVersion version) {
		return TFAllRequired.getInstance(TFConstChar.getInstance('#'), TFExpr.getInstance(version));
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
				return TFSerialRRO.getInstance(TFConstChar.getInstance('*'), TFGlvn.getInstance(this.version), TFTimeout.getInstance(this.version));
			case '@':
				return TFSerialRO.getInstance(TFIndirection.getInstance(this.version), TFTimeout.getInstance(this.version));
			default: 
				return TFSerialROO.getInstance(TFGlvn.getInstance(this.version), getTFReadcountInstance(this.version), TFTimeout.getInstance(this.version));
		}
	}

	public static TFReadArgument getInstance(MVersion version) {
		return new TFReadArgument(version);
	}
}
