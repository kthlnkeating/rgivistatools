package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqStatic;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFMergeArgument extends TFSeqStatic {
	private MVersion version;
	
	private TFMergeArgument(MVersion version) {
		this.version = version;
	}
	
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{MTFSupply.getInstance(version).getTFGlvn(), TFConstChar.getInstance('='), MTFSupply.getInstance(version).getTFExpr()}; 
	}

	@Override
	protected int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return RETURN_NULL;
		}
		if (seqIndex == 1) {
			if (foundTokens[0] instanceof TIndirection) {
				return RETURN_TOKEN;
			} else {
				return this.getErrorCode();
			}
		}
		return CONTINUE;
	}
	
	@Override		
	protected int validateEnd(int seqIndex, IToken[] foundTokens) {
		return this.getErrorCode();
	}
	
	public static TFMergeArgument getInstance(MVersion version) {
		return new TFMergeArgument(version);
	}
}	
