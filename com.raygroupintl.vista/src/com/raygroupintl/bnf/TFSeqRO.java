package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ITokenFactory;

public class TFSeqRO extends TFSeqStatic {
	public TFSeqRO() {
		boolean[] flags = {true, false};
		this.setRequiredFlags(flags);
	}		
	
	public TFSeqRO(ITokenFactory... factories) {
		super(factories);
		boolean[] flags = {true, false};
		this.setRequiredFlags(flags);
	}		
	
	public static ITokenFactory getInstance(final ITokenFactory required, final ITokenFactory optional) {
		return new TFSeqRO(required, optional); 
	}	
}
