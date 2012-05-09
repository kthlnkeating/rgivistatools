package com.raygroupintl.bnf;

import java.util.List;

public final class TFList extends TokenFactory {
	private TokenFactory elementFactory;
	private boolean addErrorToList;
	
	public TFList() {
	}
	
	public TFList(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
	
	public void setElementFactory(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
		
	public void setAddErrorToList(boolean b) {
		this.addErrorToList = b;
	}
	
	protected Token getToken(List<Token> list) {
		return new TList(list);
	}

	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorInListException {
		ListAsTokenStore store = new ListAsTokenStore();		
		this.extractTo(line, fromIndex, store);
		if (store.get(0) == null) {
			return null;
		} else {
			return this.getToken(store.toList());			
		}
	}
	
	public void extractTo(String line, int fromIndex, TokenStore store) throws SyntaxErrorInListException {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			boolean added = false;
			int index = fromIndex;
			while (index < endIndex) {
				try {
					Token token = this.elementFactory.tokenize(line, index);
					if (token == null) {
						if (! added) store.addToken(null);
						return;					
					}
					index += token.getStringSize();
					store.addToken(token);	
					added = true;
				} catch (SyntaxErrorException se) {
					if (this.addErrorToList) {
						Token et = se.getAsToken(line, index);
						store.addToken(et);
						return;
					} else {
						throw new SyntaxErrorInListException(se, store);
					}
				}
			}
			return;
		}
		store.addToken(null);
	}
}
