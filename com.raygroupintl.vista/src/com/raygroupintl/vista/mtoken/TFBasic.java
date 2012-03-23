package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.token.TFCharAccumulating;

public abstract class TFBasic extends TFCharAccumulating {
	@Override
	protected IToken getToken(String line, int fromIndex) {
		String value = line.substring(fromIndex);
		return this.getToken(value);			
	}

	@Override
	protected abstract boolean stopOn(char ch);
	
	@Override
	protected IToken getToken(String line, int fromIndex, int endIndex) {
		String value = line.substring(fromIndex, endIndex);
		return this.getToken(value);			
	}
	
	@Override
	protected boolean isRightStop(char ch) {
		return true;
	}
	
	protected IToken getToken(String value) {
		return new TBasic(value);		
	}
}
