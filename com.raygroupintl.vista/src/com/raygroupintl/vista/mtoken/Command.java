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
	
	protected int extractPostConditional(String line, int fromIndex) {
		int endIndex = line.length();
		if (fromIndex < endIndex) {
			char ch = line.charAt(fromIndex);
			if (ch == ':') {
				Multi pc = Algorithm.tokenize(line, fromIndex+1, ' ');
				this.postConditional = new PostConditional(pc);
				int index = fromIndex + this.postConditional.getStringSize();
				if (index < endIndex) {
					++index;
					this.traliningSpace = 1;
				}
				return index;
			} else if (ch != ' ') {
				this.postConditional = new SyntaxError(line, fromIndex);
				return endIndex;				
			} else {
				this.traliningSpace = 1;
				return fromIndex + 1;
			}
		} else {
			return endIndex;
		}
	}
	
	protected int extractArgument(String line, int fromIndex) {
		Multi arg = Algorithm.tokenize(line, fromIndex, ' ');
		this.argument = arg;
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

}

