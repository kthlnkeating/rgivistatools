package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFConstChar;

public abstract class TFCommaDelimitedList extends TFDelimitedList {
	@Override
	protected ITokenFactory getDelimitedFactory() {
		return new TFConstChar(',');
	}
}
