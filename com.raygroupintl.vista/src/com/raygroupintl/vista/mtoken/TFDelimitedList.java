package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFEmpty;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFDelimitedList extends TFSeqRO {
	protected abstract ITokenFactory getElementFactory();
	
	protected abstract ITokenFactory getDelimitedFactory();
	
	@Override
	protected ITokenFactory getRequired() {
		return this.getElementFactory();
	}

	@Override
	protected ITokenFactory getOptional() {
		TFSeqRequired r = TFSeqRequired.getInstance(this.getDelimitedFactory(), this.getElementFactory());
		return TFList.getInstance(r);
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		if (foundTokens[1] == null) {
			return new TList(foundTokens[0]);	
		} else {		
			TList result = (TList) foundTokens[1];
			result.add(0, foundTokens[0]);
			return result;
		}
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
			ITokenFactory fOptional = ChoiceSupply.get(f, String.valueOf(selectionChars), TFEmpty.getInstance(ch), TFEmpty.getInstance(')'));
			return getInstance(fOptional, ch, inParan);
		} else {
			return getInstance(f, ch, inParan);
		}
	}

}
