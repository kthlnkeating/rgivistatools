package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFCommaDelimitedList extends TFDelimitedList {
	@Override
	protected ITokenFactory getDelimitedFactory() {
		return new TFConstChar(',');
	}
	
	public static TFCommaDelimitedList getInstance(final ITokenFactory tfElement) {
		return new TFCommaDelimitedList() {			
			@Override
			protected ITokenFactory getElementFactory() {
				return tfElement;
			}
		};
	}		
}
