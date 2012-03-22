package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFParallel;

public class TFExprAtom extends TFParallel {
	private TFExprAtom() {		
	}
	
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{new TFGlvn(), new TFExprItem()};
	}
	
	static public TFExprAtom getInstance() {
		return new TFExprAtom();
	}
}
