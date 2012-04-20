package com.raygroupintl.bnf;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenArray;
import com.raygroupintl.vista.struct.MError;

public class TArray implements IToken, ITokenArray {
	private IToken[] tokens;
	
	public TArray(IToken[] tokens) {
		this.tokens = tokens;
	}

	public TArray(TArray token) {
		this.tokens = token.tokens;
	}

	@Override
	public String getStringValue() {	
		String result = "";
		for (IToken t : this.tokens) if (t != null) {
			result += t.getStringValue();
		}		
		return result;
	}

	@Override
	public int getStringSize() {
		int result = 0;
		for (IToken t : this.tokens) if (t != null) {
			result +=  t.getStringSize();
		}
		return result;
	}

	@Override
	public List<MError> getErrors() {
		List<MError> result = null;
		for (IToken t : this.tokens) if (t != null) {
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
		for (IToken t : this.tokens) if (t != null) {
			if (t.hasError()) return true;
		}
		return false;
	}

	@Override
	public boolean hasFatalError() {
		for (IToken t : this.tokens) if (t != null) {
			if (t.hasFatalError()) return true;
		}
		return false;
	}

	@Override
	public void beautify() {
		for (IToken t : this.tokens) if (t != null) {
			t.beautify();
		}
	}
	
	@Override
	public int getCount() {
		return this.tokens.length;
	}
	
	@Override
	public IToken get(int i) {
		if (this.tokens.length > i) {
			return this.tokens[i];
		} else {
			return null;
		}
	}
}
