package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TBasic;

public abstract class TFCharAccumulating implements ITokenFactory {
	protected abstract boolean stopOn(char ch);
	
	protected abstract IToken getToken(String line, int fromIndex);
	
	protected abstract IToken getToken(String line, int fromIndex, int endIndex);
	
	protected abstract boolean isRightStop(char ch);
	
	protected boolean checkFirst() {
		return false;
	}
	
	@Override
	public IToken tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		boolean first = true;
		if (fromIndex < endIndex) {
			int index = fromIndex;
			do {
				char ch = line.charAt(index);
				if ((! first) || this.checkFirst()) {				
					if (this.stopOn(ch)) {
						if (this.isRightStop(ch)) {
							if (fromIndex == index) {
								return null;
							} else {
								return this.getToken(line, fromIndex, index);
							}
						} else {
							return new TSyntaxError(line, index, fromIndex);
						}
					}
				}
				++index;
				first = false;
			} while (index < endIndex);
			return this.getToken(line, fromIndex);
		}
		return null;
	}
	
	public static TFCharAccumulating getInstance(final char ch0, final char ch1) {
		return new TFCharAccumulating() {			
			@Override
			protected boolean stopOn(char ch) {
				return (ch != ch0) && (ch != ch1);
			}
			
			@Override
			protected boolean isRightStop(char ch) {
				return true;
			}
			
			@Override
			protected IToken getToken(String line, int fromIndex, int endIndex) {
				return new TBasic(line.substring(fromIndex, endIndex));
			}
			
			@Override
			protected IToken getToken(String line, int fromIndex) {
				return new TBasic(line.substring(fromIndex));
			}
		};
	}
	
	public static TFCharAccumulating getInstance(final char chIn) {
		return new TFCharAccumulating() {			
			@Override
			protected boolean stopOn(char ch) {
				return ch != chIn;
			}
			
			@Override
			protected boolean isRightStop(char ch) {
				return true;
			}
			
			@Override
			protected boolean checkFirst() {
				return true;
			}
			
			@Override
			protected IToken getToken(String line, int fromIndex, int endIndex) {
				return new TBasic(line.substring(fromIndex, endIndex));
			}
			
			@Override
			protected IToken getToken(String line, int fromIndex) {
				return new TBasic(line.substring(fromIndex));
			}
		};
	}
}
