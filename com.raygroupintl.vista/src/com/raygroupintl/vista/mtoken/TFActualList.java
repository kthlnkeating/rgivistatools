package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqROR;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFActualList extends TFSeqROR {
	private MVersion version;
	
	private TFActualList(MVersion version) {
		this.version = version;
	}
	
	@Override
	protected ITokenFactory[] getFactories() {
		TFConstChar lp = TFConstChar.getInstance('(');
		TFDelimitedList al = TFDelimitedList.getInstance(MTFSupply.getInstance(this.version).getTFActual(), ',');
		TFConstChar rp = TFConstChar.getInstance(')');
		return new ITokenFactory[]{lp, al, rp};
	}
	
	@Override
	protected IToken getToken(IToken[] foundTokens) {
		TList list = (foundTokens[1] == null) ? new TList() : (TList) foundTokens[1];
		return new TActualList(list);
	}
	
	public static TFActualList getInstance(MVersion version) {
		return new TFActualList(version);
	}
}
