package com.raygroupintl.m.token;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.Token;

public class TSyntaxError implements MToken {	
	private int errorCode = MError.ERR_GENERAL_SYNTAX;
	private StringPiece errorText;
	private int errorIndex;
	
	public TSyntaxError(int errorCode, StringPiece errorText, int errorIndex) {
		this.errorCode = errorCode;
		this.errorText = errorText;
		this.errorIndex = errorIndex;
	}
	
	public int getErrorIndex() {
		return this.errorIndex;
	}
	
	@Override
	public StringPiece toValue() {
		return this.errorText;
	}

	@Override
	public List<Token> toList() {	
		List<Token> result = new ArrayList<Token>();
		result.add(this);	
		return result;
	}

	@Override
	public void beautify() {		
	}
	
	@Override
	public ErrorNode getNode() {
		return new ErrorNode(this.errorCode > 0 ? this.errorCode : MError.ERR_GENERAL_SYNTAX);
	}
}