package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;

public abstract class Command extends Keyword {
	private static class PostConditional extends Multi {
		public PostConditional(Multi input) {
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
	
	private IToken postConditional;
	private IToken argument;
	private int traliningSpace;

	public Command(String identifier) {
		super(identifier);
	}
	
	public String getCommandString() {
		return super.getStringValue();
	}
	
	public int extractDetails(String line, int fromIndex) {
		int length = line.length();
		int index = fromIndex;
		if (index < length) {
			char ch = line.charAt(fromIndex);
			if (ch == ':') {
				Multi pc = Algorithm.tokenize(line, index+1, ' ');
				this.postConditional = new PostConditional(pc);
				index += this.postConditional.getStringSize();
			} else if (ch != ' ') {
				this.postConditional = new SyntaxError(line, fromIndex);
				return length;
			}
			if (index < length) {
				++index;
				if (index == length) {
					this.traliningSpace = 1;
					return length;
				}
				ch = line.charAt(index);
				if (ch == ';') {
					this.traliningSpace = 1;
					return index;
				}				
				if (ch ==' ') {
					int notSpaceIndex = Algorithm.findOther(line, index, ' ');
					this.traliningSpace = notSpaceIndex - index + 1;
					return notSpaceIndex;
				}
				Multi arg = Algorithm.tokenize(line, index, ' ');
				this.argument = arg;
				index += this.argument.getStringSize();
				if (index < length) {
					int notSpaceIndex = Algorithm.findOther(line, index, ' ');
					this.traliningSpace = notSpaceIndex - index;
					return notSpaceIndex;
				}
			}			
		}
		return index;
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

}

