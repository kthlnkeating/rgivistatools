package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFChoice;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFEmpty;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TList;

public abstract class TFDelimitedList extends TFSerialRO {
	protected abstract ITokenFactory getElementFactory();
	
	protected abstract ITokenFactory getDelimitedFactory();
	
	@Override
	protected ITokenFactory getRequired() {
		return this.getElementFactory();
	}

	@Override
	protected ITokenFactory getOptional() {
		TFAllRequired r = TFAllRequired.getInstance(this.getDelimitedFactory(), this.getElementFactory());
		return TFList.getInstance(r);
	}

	protected IToken getToken(TList listToken) {
		return listToken;
	}
	
	@Override
	protected IToken getTokenRequired(IToken requiredToken) {
		return this.getToken(new TList(requiredToken));
	}
	
	@Override
	protected IToken getTokenBoth(IToken requiredToken, IToken optionalToken) {
		TList result = (TList) optionalToken;
		result.add(0, requiredToken);
		return  this.getToken(result);
	}
	
	public static TFDelimitedList getInstance(final ITokenFactory tfElement, final char ch) {
		return new TFDelimitedList() {			
			@Override
			protected ITokenFactory getElementFactory() {
				return tfElement;
			}
			
			@Override
			protected ITokenFactory getDelimitedFactory() {
				return TFConstChar.getInstance(ch);
			}
		};
	}

	public static ITokenFactory getInstance(final ITokenFactory f, final char ch, final boolean inParan) {
		final TFDelimitedList fList = getInstance(f, ch);
		if (inParan) {
			return TFInParantheses.getInstance(fList);
		} else {
			return fList;
		}
	}

	public static ITokenFactory getInstance(final ITokenFactory f, final char ch, final boolean inParan, boolean optional) {
		if (optional) {
			char[] selectionChars = {ch, ')'};
			ITokenFactory fOptional = TFChoice.getInstance(f, String.valueOf(selectionChars), TFEmpty.getInstance(ch), TFEmpty.getInstance(')'));
			return getInstance(fOptional, ch, inParan);
		} else {
			return getInstance(f, ch, inParan);
		}
	}

}
