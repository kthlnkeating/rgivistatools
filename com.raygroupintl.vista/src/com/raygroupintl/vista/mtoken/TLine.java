package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.token.TArray;

public class TLine extends TArray {	
	public TLine(IToken[] tokens) {
		super(tokens);
	}
	/*
	@Override
	public List<MError> getErrors() {
		if (this.size() == 0) {
			MError[] errors = new MError[]{new MError(MError.ERR_NULL_LINE)};
			return Arrays.asList(errors);
		}
		return super.getErrors();
	}
	
	public int getLevel() {
		if (this.hasLevelToken) {
			int index = this.hasTagToken ? 1 : 0;
			IToken levelToken = this.get(index);
			String s = levelToken.getStringValue();
			int count = 0;
			for (int i=0; i<s.length(); ++i) {
				if (s.charAt(i) == '.') {
					++count;
				}
			}
			return count;
		}
		return 0;	
	}
	*/
	/*
	public String getTagView() {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		if (this.hasTagToken) {
			IToken tagToken = this.get(0);
			++index;
			sb.append(tagToken.getStringValue());
		}
		int count = this.getLevel();		
		sb.append(" LEVEL:" + String.valueOf(count) + " ");
		if (this.hasLevelToken) ++index;
		if (this.size() > index) {
			sb.append(" ...");
		}
		return sb.toString();
	}
	*/
	/*
	public String getCommandView() {
		StringBuilder sb = new StringBuilder();
		if (this.hasTagToken) {
			IToken token = this.get(0);
			sb.append(token.getStringValue());
		}
		if (this.hasLevelToken) {
			IToken token = this.get(this.hasTagToken ? 1 : 0);
			sb.append(token.getStringValue());
		}
		sb.append(this.getCommandView());
		return sb.toString();
	}
	*/
}
