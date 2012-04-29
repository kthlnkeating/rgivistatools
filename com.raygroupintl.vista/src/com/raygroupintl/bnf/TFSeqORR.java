package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFSeqORR extends TFSeqStatic {
	public TFSeqORR() {
		boolean[] flags = {false, true, true};
		this.setRequiredFlags(flags);		
	}
	
	public TFSeqORR(ITokenFactory... factories) {
		super(factories);
		boolean[] flags = {false, true, true};
		this.setRequiredFlags(flags);
	}
}
