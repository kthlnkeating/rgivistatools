package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ITokenFactory;

public class TFSeqROR extends TFSeqStatic {
	public TFSeqROR() {
		boolean[] flags = {true, false, true};
		this.setRequiredFlags(flags);		
	}
	
	public TFSeqROR(ITokenFactory... factories) {
		super(factories);
		boolean[] flags = {true, false, true};
		this.setRequiredFlags(flags);
	}
	
	public static TFSeqROR getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSeqROR(f0, f1, f2); 
	}	
}
