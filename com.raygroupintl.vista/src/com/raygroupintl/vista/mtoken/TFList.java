package com.raygroupintl.vista.mtoken;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.TSyntaxError;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFList implements ITokenFactory {
	protected abstract ITokenFactory getFactory();

	protected IToken getToken(List<IToken> list) {
		return new TList(list);
	}

	protected IToken getNullToken() {
		return null;
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
						return this.getNullToken();
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
	
	private static class TFRequiredList extends TFList {
		private ITokenFactory elementFactory;
		
		public TFRequiredList(ITokenFactory elementFactory) {
			this.elementFactory = elementFactory;
		}
		
		protected ITokenFactory getFactory() {
			return this.elementFactory;
		}
	}
	
	private static class TFOptionalList extends TFRequiredList {
		public TFOptionalList(ITokenFactory elementFactory) {
			super(elementFactory);
		}

		@Override
		protected IToken getNullToken() {
			return new TList();
		}
	}
	
	public static TFList getInstance(ITokenFactory elementFactory, boolean optional) {
		if (optional) {
			return new TFOptionalList(elementFactory);
		} else {
			return new TFRequiredList(elementFactory);
		}
	}
	
	public static TFList getInstance(ITokenFactory elementFactory) {
		return getInstance(elementFactory, false);
	}
}
