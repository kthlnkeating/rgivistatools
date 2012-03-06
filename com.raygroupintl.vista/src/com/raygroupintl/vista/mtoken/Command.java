package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.struct.MNameWithMnemonic;

public class Command extends Keyword {
	private MNameWithMnemonic command;
	private IToken postConditional;
	private IToken argument;
	private int traliningSpace;

	public Command(String identifier, MNameWithMnemonic command) {
		super(identifier);
		this.command = command;
	}
	
	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return this.command;
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
				this.postConditional = pc;
				index += 1 + this.postConditional.getStringSize();
			}
			if (index < length) {
				ch = line.charAt(index);
				if (ch != ' ') return -1;
				++index;
				if (index == length) {
					this.traliningSpace = 1;
					return length;
				}
				ch = line.charAt(index);
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
			result += ':' + this.postConditional.getStringValue();
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
			result += 1 + this.postConditional.getStringSize();
		}
		if (this.argument != null) {
			result += 1 + this.argument.getStringSize();
		}
		result += this.traliningSpace;
		return result;
	}

}

