package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ITokenFactory;

public class TFSeqORO extends TFSeqStatic {
	public TFSeqORO() {
		boolean[] flags = {false, true, false};
		this.setRequiredFlags(flags);		
	}
	
	public TFSeqORO(ITokenFactory... factories) {
		super(factories);
		boolean[] flags = {false, true, false};
		this.setRequiredFlags(flags);
	}
	
	public static TFSeqORO getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSeqORO(f0, f1, f2); 
	}
}
