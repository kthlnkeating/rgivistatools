package com.raygroupintl.bnf;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.vista.struct.MError;

public class TArray implements Token, TokenArray {
	private Token[] tokens;
	
	public TArray(Token[] tokens) {
		this.tokens = tokens;
	}

	public TArray(TArray token) {
		this.tokens = token.tokens;
	}

	@Override
	public String getStringValue() {	
		String result = "";
		for (Token t : this.tokens) if (t != null) {
			result += t.getStringValue();
		}		
		return result;
	}

	@Override
	public int getStringSize() {
		int result = 0;
		for (Token t : this.tokens) if (t != null) {
			result +=  t.getStringSize();
		}
		return result;
	}

	@Override
	public List<MError> getErrors() {
		List<MError> result = null;
		for (Token t : this.tokens) if (t != null) {
			List<MError> errors = t.getErrors();
			if (errors != null) {
				if (result == null) {
					result = new ArrayList<MError>(errors);
				} else {
					result.addAll(errors);
				}
			}
		}
		return result;
	}
	
	@Override
	public boolean hasError() {
		for (Token t : this.tokens) if (t != null) {
			if (t.hasError()) return true;
		}
		return false;
	}

	@Override
	public boolean hasFatalError() {
		for (Token t : this.tokens) if (t != null) {
			if (t.hasFatalError()) return true;
		}
		return false;
	}

	@Override
	public void beautify() {
		for (Token t : this.tokens) if (t != null) {
			t.beautify();
		}
	}
	
	@Override
	public int getCount() {
		return this.tokens.length;
	}
	
	@Override
	public Token get(int i) {
		if (this.tokens.length > i) {
			return this.tokens[i];
		} else {
			return null;
		}
	}
}
