package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialROR;
import com.raygroupintl.vista.token.TList;

public class TFActualList extends TFSerialROR {
	@Override
	protected ITokenFactory[] getFactories() {
		TFConstChar lp = TFConstChar.getInstance('(');
		TFDelimitedList al = TFDelimitedList.getInstance(TFActual.getInstance(), ',');
		TFConstChar rp = TFConstChar.getInstance(')');
		return new ITokenFactory[]{lp, al, rp};
	}
	
	@Override
	protected IToken getToken(IToken[] foundTokens) {
		TList list = (foundTokens[1] == null) ? new TList() : (TList) foundTokens[1];
		return new TActualList(list);
	}
	
	public static TFActualList getInstance() {
		return new TFActualList();
	}
}
