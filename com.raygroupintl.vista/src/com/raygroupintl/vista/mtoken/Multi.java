package com.raygroupintl.vista.mtoken;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MError;


public class Multi implements IToken {
	private ArrayList<IToken> tokens = new ArrayList<IToken>();
		
	public Multi() {
	}
	
	public Multi(IToken token) {
		this.tokens.add(token);
	}

	public Multi(IToken token0, IToken token1) {
		this.tokens.add(token0);
		this.tokens.add(token1);
	}

	public Multi(List<IToken> tokens) {
		this.tokens.addAll(tokens);
	}

	public Multi(Multi tokens) {
		this.tokens.addAll(tokens.tokens);
	}

	@Override
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		for (IToken token : this.tokens) {
			sb.append(token.getStringValue());
		}
		return sb.toString();
	}

	@Override
	public int getStringSize() {
		int result = 0;
		for (IToken token : this.tokens) {
			result += token.getStringSize();
		}
		return result;
	}

	@Override
	public List<MError> getErrors() {
		List<MError> result = null;
		for (IToken token : this.tokens) {
			if (token.hasError()) {
				List<MError> errors = token.getErrors();
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
		for (IToken token : this.tokens) {
			if (token.hasError()) return true;
		}
		return false;
	}

	@Override
	public boolean hasFatalError() {
		for (IToken token : this.tokens) {
			if (token.hasFatalError()) return true;
		}
		return false;
	}
	
	@Override
	public void beautify() {
		for (IToken token : this.tokens) {
			token.beautify();
		}
	}
	
	public void add(IToken token) {
		this.tokens.add(token);
	}
	
	public void addAll(List<IToken> tokens) {
		this.tokens.addAll(tokens);
	}
	
	public IToken get(int index) {
		return this.tokens.get(index);
	}
	
	public int size() {
		return this.tokens.size();
	}
	
	public String getCommandView() {
		StringBuilder sb = new StringBuilder();
		for (IToken token : this.tokens) {
			if (token instanceof Command) {
				Command commandToken = (Command) token;
				String commandString = commandToken.getCommandString();
				sb.append(commandString);
				sb.append("  ");
			}
		}
		return sb.toString();
	}

	public void add(int index, IToken token) {
		this.tokens.add(index, token);
	}

}
