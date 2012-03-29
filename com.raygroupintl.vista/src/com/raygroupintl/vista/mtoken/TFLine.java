package com.raygroupintl.vista.mtoken;

import java.util.Arrays;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFBasic;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerial;
import com.raygroupintl.vista.token.TFSyntaxError;
import com.raygroupintl.vista.token.TSyntaxError;

public class TFLine extends TFSerial {
	private static final int NUM_LOGICAL_TOKENS = 5; // label, formals, space, level, commands
	
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
				TFConstChars.getInstance(" \t"),
				TFBasic.getInstance('.', ' '),
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

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		return new TLine(foundTokens);
	}
	
	@Override
	protected IToken getTokenWhenSyntaxError(IToken[] found, TSyntaxError error, int fromIndex) {
		int n = found.length;
		assert(n < NUM_LOGICAL_TOKENS);
		found = Arrays.copyOf(found, NUM_LOGICAL_TOKENS);
		found[n] = error;
		return new TLine(found);
	}
	
	public static TFLine getInstance() {
		return new TFLine();
	}
}
