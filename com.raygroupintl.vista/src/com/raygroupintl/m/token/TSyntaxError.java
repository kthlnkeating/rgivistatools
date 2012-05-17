package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.StringAdapter;
import com.raygroupintl.parser.Token;

public class TSyntaxError implements MToken {	
	public static class Adapter implements StringAdapter {
		private int errorCode = MError.ERR_GENERAL_SYNTAX;
		private int errorIndex;

		public Adapter(int errorCode, int errorIndex) {
			this.errorCode = errorCode;
			this.errorIndex = errorIndex;
		}		
	
		@Override
		public Token convert(String value) {
			return new TSyntaxError(this.errorCode, value, this.errorIndex);
		}
	}
	
	private int errorCode = MError.ERR_GENERAL_SYNTAX;
	private String errorText;
	private int errorIndex;
	
	public TSyntaxError(int errorCode, String errorText, int errorIndex) {
		this.errorCode = errorCode;
		this.errorText = errorText;
		this.errorIndex = errorIndex;
	}
	
	public int getErrorIndex() {
		return this.errorIndex;
	}
	
	@Override
	public String getStringValue() {
		return this.errorText;
	}

	@Override
	public int getStringSize() {
		return this.errorText.length();
	}

	@Override
	public void beautify() {		
	}
	
	@Override
	public ErrorNode getNode() {
		return new ErrorNode(this.errorCode > 0 ? this.errorCode : MError.ERR_GENERAL_SYNTAX);
	}
}