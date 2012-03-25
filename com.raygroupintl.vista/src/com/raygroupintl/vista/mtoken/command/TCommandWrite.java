package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommand;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFCharAccumulating;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TCommandWrite extends TCommand {	
	private static ITokenFactory getTFTabFormat() {
		return TFAllRequired.getInstance(TFConstChar.getInstance('?'), TFExpr.getInstance());
	}
 	
	private static ITokenFactory getTFPositionXTabFormat() {
		return TFSerialRO.getInstance(TFCharAccumulating.getInstance('!','#'), getTFTabFormat());
	}
 	
	private static class TFArgument extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			switch(ch) {
				case '!':
				case '#':
					return getTFPositionXTabFormat();
				case '?':
					return getTFTabFormat();
				case '/':
					return null;
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
	protected ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
 	
	@Override
	public IToken getArgument(String line, int fromIndex) {
		return this.getNewArgument(line, fromIndex);
	}
}
