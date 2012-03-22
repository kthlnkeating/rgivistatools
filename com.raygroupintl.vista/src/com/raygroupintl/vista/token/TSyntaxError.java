package com.raygroupintl.vista.token;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;

public class TSyntaxError implements IToken {	
	private int errorCode = MError.ERR_GENERAL_SYNTAX;
	private String line;
	private int fromIndex;
	private int errorLocationIndex;
	
	public TSyntaxError(String line, int index) {
		this.line = line;
		this.fromIndex = index;
		this.errorLocationIndex = index;
	}
	
	public TSyntaxError(String line, int locationIndex, int fromIndex) {
		this.line = line;
		this.fromIndex = fromIndex;
		this.errorLocationIndex = locationIndex;
	}
	
	public TSyntaxError(int errorCode, String line, int index) {
		this(line, index);
		this.errorCode = errorCode;
	}
	
	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}
	
	public int getErrorLocation() {
		return this.errorLocationIndex;
	}
	
	@Override
	public String getStringValue() {
		return this.line.substring(fromIndex);
	}

	@Override
	public int getStringSize() {
		return this.line.length() - this.fromIndex;
	}

	@Override
	public List<MError> getErrors() {
		MError e = new MError(this.errorCode);
		return Arrays.asList(new MError[]{e});
	}

	@Override
	public boolean isError() {
		return true;
	}

	@Override
	public boolean hasError() {
		return true;
	}

	@Override
	public boolean hasFatalError() {
		return true;
	}

	@Override
	public void beautify() {		
	}
	
	public static TSyntaxError getInstance(int errorCode, String line, int index) {
		TSyntaxError result = new TSyntaxError(errorCode, line, index);
		return result;
	}

	public static TSyntaxError getInstance(int errorCode, String line, int index, int fromIndex) {
		TSyntaxError result = new TSyntaxError(errorCode, line, index);
		result.setFromIndex(fromIndex);
		return result;
	}
}