package com.raygroupintl.bnf;

import com.raygroupintl.fnds.ITokenFactory;

public class TFSeqRequired extends TFSeqStatic {
	public TFSeqRequired() {
		this.setRequiredAll();
	}
	
	public TFSeqRequired(ITokenFactory... factories) {
		super(factories);
		this.setRequiredAll();
	}
	
	public static ITokenFactory getInstance(final ITokenFactory f0, final ITokenFactory f1) {
		TFSeqStatic r = new TFSeqRequired(f0, f1);
		r.setRequiredAll();
		return r;
	}	

	public static ITokenFactory getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		TFSeqStatic r = new TFSeqRequired(f0, f1, f2);
		r.setRequiredAll();
		return r;
	}	
}
