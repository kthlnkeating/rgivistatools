package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFCharAccumulating;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerial;
import com.raygroupintl.vista.token.TFSyntaxError;

public class TFLine extends TFSerial {
	private static ITokenFactory getTFFormal() {
		TFName argument = TFName.getInstance();
		TFDelimitedList arguments = TFDelimitedList.getInstance(argument, ',');
		return TFInParantheses.getInstance(arguments, false);		
	}
			
	private static class TFCommandOrComment extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			if (Library.isIdent(ch)) {
				return new TFCommand();
			} else if (ch == ';') {
				return new TFComment();
			} else {
				return new TFSyntaxError();
			}
		}		
	}
	
	private static ITokenFactory getTFCommands() {
		ITokenFactory f = new TFCommandOrComment();
		return TFList.getInstance(f);
	}
 	
	
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{
				TFLabel.getInstance(),
				getTFFormal(),
				TFConstChar.getInstance(' '),
				TFCharAccumulating.getInstance('.', ' '),
				getTFCommands()
		};
	}

	@Override
	protected int getCodeNextIsNull(IToken[] foundTokens) {
		return 0;
	}

	@Override
	protected int getCodeStringEnds(IToken[] foundTokens) {
		return 0;
	}

	protected IToken getToken(IToken[] foundTokens) {
		return new TLine(foundTokens);
	}
	
	public static TFLine getInstance() {
		return new TFLine();
	}
}
