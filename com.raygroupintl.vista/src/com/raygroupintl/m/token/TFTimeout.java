package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.ITokenFactory;

public class TFTimeout extends TFSeqRequired {
	private MVersion version;
	
	private TFTimeout(MVersion version) {
		this.version = version;
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{TFConstChar.getInstance(':'), MTFSupply.getInstance(version).expr};
	}
	
	public static TFTimeout getInstance(MVersion version) {
		return new TFTimeout(version);
	}
}
