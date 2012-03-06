package com.raygroupintl.vista.mtoken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;


public class FatalError extends Multi {
	private MError error;
	private IToken errorToken;
	public int fromIndex;
	public int endIndex;
	
	public FatalError(int code, Multi tokens) {
		super(tokens);
		this.error = new MError(code);
	}
	
	public FatalError(int code, IToken errorToken, IToken restToken) {
		super(Arrays.asList(new IToken[]{errorToken, restToken}));
		this.errorToken = errorToken;
		this.error = new MError(code);
	}

	public FatalError(int code, String line, int index) {
		super(new Basic(line.substring(index)));
		this.errorToken = new Basic(line);
		this.error = new MError(code);
	}

	public static FatalError getInstance(int code, String line, int index) {
		String subLine = line.substring(index);
		Basic token = new Basic(subLine);
		Multi tokens = new Multi(token);
		return new FatalError(code, tokens);
	}

	IToken getErrorToken() {
		return this.errorToken;
	}
	
	@Override
	public List<MError> getErrors() {
		List<MError> result = new ArrayList<MError>();
		result.add(this.error);
		List<MError> otherErrors = super.getErrors();
		if (otherErrors != null) {
			result.addAll(otherErrors);
		}
		return result;
	}

	@Override
	public boolean hasError() {
		return true;
	}

	@Override
	public boolean hasFatalError() {
		return true;
	}
	
	public IToken getFatalErrorToken() {
		return this;		
	}
	
	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}
}
