package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ITokenFactory;

public class TFSeqOR extends TFSeqStatic {
	public TFSeqOR() {
		boolean[] flags = {false, true};
		this.setRequiredFlags(flags);
	}
	
	public TFSeqOR(ITokenFactory... factories) {
		super(factories);
		boolean[] flags = {false, true};
		this.setRequiredFlags(flags);
	}

	public static ITokenFactory getInstance(final ITokenFactory optional, final ITokenFactory required) {
		return new TFSeqOR(optional, required);
	}	
}
