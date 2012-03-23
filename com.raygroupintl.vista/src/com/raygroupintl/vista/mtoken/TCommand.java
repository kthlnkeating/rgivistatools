package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TList;
import com.raygroupintl.vista.token.TPair;
import com.raygroupintl.vista.token.TSyntaxError;

public abstract class TCommand extends TKeyword {
	private static class PostConditional extends TList {
		public PostConditional(TList input) {
			super(input);
		}
		
		@Override
		public String getStringValue() {
			return ':' + super.getStringValue();
		}
		
		@Override
		public int getStringSize() {
			return 1 + super.getStringSize();
		}		
	}
	
	protected IToken postConditional;
	protected IToken argument;
	private int traliningSpace;

	public TCommand(String identifier) {
		super(identifier);
	}
	
	public String getCommandString() {
		return super.getStringValue();
	}
	
	@Override
	public List<MError> getErrors() {
		if ((this.argument == null) && (this.postConditional == null)) {
			return null;
		}
		if (this.argument == null) {
			return this.postConditional.getErrors();
		}
		if (this.postConditional == null) {
			return this.argument.getErrors();
		}
		TPair p = new TPair(this.argument, this.postConditional);
		return p.getErrors();
	}

	@Override
	public boolean isError() {
		return false;
	}

	protected int extractPostConditional(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);
			if (ch == ':') {
				TList pc = Algorithm.tokenize(line, fromIndex+1, ' ');
				this.postConditional = new PostConditional(pc);
				int index = fromIndex + this.postConditional.getStringSize();
				if (index < endIndex) {
					++index;
					this.traliningSpace = 1;
				}
				return index;
			} else if (ch != ' ') {
				this.postConditional = new TSyntaxError(line, fromIndex);
				return endIndex;				
			} else {
				this.traliningSpace = 1;
				return fromIndex + 1;
			}
		} else {
			return endIndex;
		}
	}
	
	protected IToken getArgument(String line, int fromIndex) {
		TList arg = Algorithm.tokenize(line, fromIndex, ' ');
		return arg;
	}
	
	protected int extractArgument(String line, int fromIndex) {
		this.argument = this.getArgument(line, fromIndex);
		int index = fromIndex + this.argument.getStringSize();
		int endIndex = line.length();
		if (index < endIndex) {
			int notSpaceIndex = Algorithm.findOther(line, index, ' ');
			this.traliningSpace = notSpaceIndex - index;
			return notSpaceIndex;
		} else {
			this.traliningSpace = 0;
			return endIndex;
		}		
	}
	
	public int extractDetails(String line, int fromIndex) {
		this.traliningSpace = 0;
		int endIndex = line.length();
		int index = this.extractPostConditional(line, fromIndex);
		if (index < endIndex) {
			char ch = line.charAt(index);
			if (ch == ';') return index;
			if (ch == ' ') {
				int notSpaceIndex = Algorithm.findOther(line, index, ' ');
				this.traliningSpace = notSpaceIndex - index + 1;
				return notSpaceIndex;
			}
			return this.extractArgument(line, index);
		} else {
			return endIndex;
		}
	}
	
	@Override
	public String getStringValue() {
		String result = super.getStringValue();
		if (this.postConditional != null) {
			result += this.postConditional.getStringValue();
		}
		if (this.argument != null) {
			result += " " + this.argument.getStringValue();
		}
		for (int i=0; i<this.traliningSpace; ++i) result += ' ';
		return result;
	}

	@Override
	public int getStringSize() {
		int result = super.getStringSize();
		if (this.postConditional != null) {
			result += this.postConditional.getStringSize();
		}
		if (this.argument != null) {
			result += 1 + this.argument.getStringSize();
		}
		result += this.traliningSpace;
		return result;
	}
	
	protected static ITokenFactory getTFPostCondition(IToken[] previousTokens) {
		ITokenFactory tfColon = TFConstChar.getInstance(':');
		ITokenFactory tfExpr = TFExpr.getInstance();
		return TFAllRequired.getInstance(tfColon, tfExpr);
	}
	
	protected ITokenFactory getArgumentFactory() {
		return null;
	}
		
	protected IToken getNewArgument(String line, int fromIndex) {
		ITokenFactory f = this.getArgumentFactory();
		IToken result = f.tokenize(line, fromIndex);
		if (result == null) {
			return TSyntaxError.getInstance(MError.ERR_GENERAL_SYNTAX, line, fromIndex, fromIndex);			
		}
		int index = fromIndex + result.getStringSize();
		if (index < line.length()) {
			char ch = line.charAt(index);
			if (ch != ' ') {
				return TSyntaxError.getInstance(MError.ERR_GENERAL_SYNTAX, line, index, fromIndex);
			}
		}
		return result;
	}
}

