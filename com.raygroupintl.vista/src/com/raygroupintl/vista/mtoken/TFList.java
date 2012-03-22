package com.raygroupintl.vista.mtoken;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TList;
import com.raygroupintl.vista.token.TSyntaxError;

public abstract class TFList implements ITokenFactory {
	protected abstract ITokenFactory getFactory();

	protected IToken getToken(List<IToken> list) {
		return new TList(list);
	}

	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			ITokenFactory f = this.getFactory();
			int index = fromIndex;
			List<IToken> list = null;
			while (index < endIndex) {
				IToken token = f.tokenize(line, index);
				if (token == null) {
					if (list == null) {
						return null;
					} else {
						return this.getToken(list);
					}
				}
				if (token instanceof TSyntaxError) {
					((TSyntaxError) token).setFromIndex(fromIndex);
					return token;
				}
				if (list == null) {
					list = new ArrayList<IToken>();
				}
				list.add(token);	
				index += token.getStringSize();
			}
			assert(index == endIndex);	
			return this.getToken(list);
		}
		return null;
	}
	
	public static TFList getInstance(final ITokenFactory elementToken) {
		return new TFList() {			
			@Override
			protected ITokenFactory getFactory() {
				return elementToken;
			}
		};
	}
}
