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
	private MVersion version;
	
	private TFLine(MVersion version) {
		this.version = version;
	}
	
	private static final int NUM_LOGICAL_TOKENS = 5; // label, formals, space, level, commands
	
	private static ITokenFactory getTFFormal() {
		TFName argument = TFName.getInstance();
		TFDelimitedList arguments = TFDelimitedList.getInstance(argument, ',');
		return TFInParantheses.getInstance(arguments, false);		
	}
			
	private static class TFCommandOrComment extends TFParallelCharBased {
		private MVersion version;
		
		private TFCommandOrComment(MVersion version) {
			this.version = version;
		}

		@Override
		protected ITokenFactory getFactory(char ch) {
			if (Library.isIdent(ch)) {
				return TFCommand.getInstance(this.version);
			} else if (ch == ';') {
				return new TFComment();
			} else {
				return new TFSyntaxError();
			}
		}		
	}
	
	private static ITokenFactory getTFCommands(MVersion version) {
		ITokenFactory f = new TFCommandOrComment(version);
		return TFList.getInstance(f);
	}
 	
	
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{
				TFLabel.getInstance(),
				getTFFormal(),
				TFConstChars.getInstance(" \t"),
				TFBasic.getInstance('.', ' '),
				getTFCommands(this.version)
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
	
	public static TFLine getInstance(MVersion version) {
		return new TFLine(version);
	}
}
