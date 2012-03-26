package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.mtoken.TBasic;

public class TFAnyBut extends TFCharAccumulating {
	@Override
	protected boolean stopOn(char ch) {
		return ch == ' ';
	}

	@Override
	protected IToken getToken(String line, int fromIndex) {
		return new TBasic(line.substring(fromIndex));
	}

	@Override
	protected IToken getToken(String line, int fromIndex, int endIndex) {
		return new TBasic(line.substring(fromIndex, endIndex));
	}

	@Override
	protected boolean isRightStop(char ch) {
		return true;
	}
	
	protected boolean checkFirst() {
		return true;
	}
	
	public static TFAnyBut getInstance() {
		return new TFAnyBut();
	}
}
