package com.raygroupintl.bnf;

import java.util.ArrayList;
import java.util.List;


public class TFList implements TokenFactory {
	private TokenFactory elementFactory;

	public TFList() {
	}
	
	public TFList(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
	
	public void setElementFactory(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
		
	protected TokenFactory getFactory() {
		return null;
	}

	protected Token getToken(List<Token> list) {
		return new TList(list);
	}

	protected Token getNullToken() {
		return null;
	}
	
	@Override
	public Token tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			if (this.elementFactory == null) {
				this.elementFactory = this.getFactory();
			}
			int index = fromIndex;
			List<Token> list = null;
			while (index < endIndex) {
				Token token = this.elementFactory.tokenize(line, index);
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
					list = new ArrayList<Token>();
				}
				list.add(token);	
				index += token.getStringSize();
			}
			assert(index == endIndex);	
			return this.getToken(list);
		}
		return null;
	}
	
	public static TFList getInstance(TokenFactory elementFactory) {
		return new TFList(elementFactory);
	}
}
