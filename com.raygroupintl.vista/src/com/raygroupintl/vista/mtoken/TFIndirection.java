package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFConstString;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenArray;
import com.raygroupintl.fnds.ITokenFactory;

public class TFIndirection extends TFSeqRO {
	private MVersion version;
	
	private TFIndirection(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		ITokenFactory f0 = TFSeqRequired.getInstance(TFConstChar.getInstance('@'), MTFSupply.getInstance(this.version).getTFExprAtom());
		ITokenFactory f1 = TFSeqRequired.getInstance(new TFConstString("@("), TFExprList.getInstance(TFIndirection.this.version), new TFConstChar(')'));
		return new ITokenFactory[]{f0, f1};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		if (foundTokens[1] == null) {
			TArray t = (TArray) foundTokens[0];
			return new TIndirection(t.get(1));			
		} else {		
			TArray tReqArray = (TArray) foundTokens[0];
			ITokenArray tOptArray = (ITokenArray) foundTokens[1];
			IToken subscripts = tOptArray.get(1);
			return new TIndirection(tReqArray.get(1), subscripts);
		}
	}

	public static TFIndirection getInstance(MVersion version) {
		return new TFIndirection(version);
	}
}
