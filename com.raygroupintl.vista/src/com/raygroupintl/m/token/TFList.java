package com.raygroupintl.m.token;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.TSyntaxError;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFList implements ITokenFactory {
	private ITokenFactory elementFactory;

	public TFList() {
	}
	
	public TFList(ITokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
	
	public void setElementFactory(ITokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
		
	protected ITokenFactory getFactory() {
		return null;
	}

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
			if (this.elementFactory == null) {
				this.elementFactory = this.getFactory();
			}
			int index = fromIndex;
			List<IToken> list = null;
			while (index < endIndex) {
				IToken token = this.elementFactory.tokenize(line, index);
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
	
	public static TFList getInstance(ITokenFactory elementFactory) {
		return new TFList(elementFactory);
	}
}
