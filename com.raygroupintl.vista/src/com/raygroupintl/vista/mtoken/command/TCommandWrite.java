package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommandName;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFFormat;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TCommandWrite extends TCommandName {	
	public static class TFArgument extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			switch(ch) {
				case '!':
				case '#':
				case '?':
				case '/':
					return TFFormat.getInstance();
				case '*':
					return TFAllRequired.getInstance(TFConstChar.getInstance('*'), TFExpr.getInstance());
				case '@':
					return TFIndirection.getInstance();
				default:
					return TFExpr.getInstance();
			}
		}
	}
	
	public TCommandWrite(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("W", "WRITE");
	}		
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
}
