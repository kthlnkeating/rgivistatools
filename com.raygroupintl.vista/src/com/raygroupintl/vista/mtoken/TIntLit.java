package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class TIntLit extends TBasic {
	private TIntLit(String value) {
		super(value);
	}
	
	public static class Factory extends TFBasic {
		@Override
		protected boolean stopOn(char ch) {
			return (ch < '0') || (ch > '9');
		}
		
		@Override
		protected boolean checkFirst() {
			return true;
		}
				
		@Override
		protected IToken getToken(String line, int fromIndex, int endIndex) {
			if (fromIndex == endIndex) {
				return null;
			} else {
				return super.getToken(line, fromIndex, endIndex);
			}
		}
		
		@Override
		protected IToken getToken(String value) {
			return new TIntLit(value);
		}
	}

	public static ITokenFactory getFactory() {
		return new Factory();
	}
}
