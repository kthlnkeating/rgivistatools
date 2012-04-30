package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFExpr extends TFSeqRO {
	private MVersion version;
	
	private TFExpr(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		
		ITokenFactory f1 = MTFSupply.getInstance(this.version).getTFExprTail(); 
		if (this.version == MVersion.CACHE) {
			ITokenFactory f0 = ChoiceSupply.get(MTFSupply.getInstance(this.version).getTFExprAtom(), '#', TFCacheClassMethod.getInstance());
			return new ITokenFactory[]{f0, f1};
		} else {
			ITokenFactory f0 = MTFSupply.getInstance(this.version).getTFExprAtom();
			return new ITokenFactory[]{f0, f1};
		}
	}

	//@Override
	//protected TExpr getToken(IToken[] foundTokens) {
	//	TExpr result = new TExpr();
	//	result.add(foundTokens[0]);
	//	if (foundTokens[1] != null) {
	//		result.addAll((TList) foundTokens[1]);
	//	}
	//	return result;
	//}
	
	public static TFExpr getInstance(MVersion version) {
		return new TFExpr(version);
	}
}
