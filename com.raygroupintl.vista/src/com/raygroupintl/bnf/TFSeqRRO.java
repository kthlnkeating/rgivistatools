package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ITokenFactory;

public class TFSeqRRO extends TFSeqStatic {
	public TFSeqRRO() {
		boolean[] flags = {true, true, false};
		this.setRequiredFlags(flags);		
	}
	
	public TFSeqRRO(ITokenFactory... factories) {
		super(factories);
		boolean[] flags = {true, true, false};
		this.setRequiredFlags(flags);
	}
	
	public static TFSeqRRO getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSeqRRO(f0, f1, f2);
	}
}
