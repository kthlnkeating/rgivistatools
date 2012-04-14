package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFParallel;

public class TFExprAtom extends TFParallel {
	private MVersion version;
	
	private TFExprAtom(MVersion version) {		
		this.version = version;
	}
	
	protected ITokenFactory[] getFactories() {
		if (this.version == MVersion.CACHE) {
			return new ITokenFactory[]{TFGlvn.getInstance(this.version), TFExprItem.getInstance(this.version), TFCacheClassMethod.getInstance()} ;
		} else {
			return new ITokenFactory[]{TFGlvn.getInstance(this.version), TFExprItem.getInstance(this.version)};
		}
	}
	
	static public TFExprAtom getInstance(MVersion version) {
		return new TFExprAtom(version);
	}
}
