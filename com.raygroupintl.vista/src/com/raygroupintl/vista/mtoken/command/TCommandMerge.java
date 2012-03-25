package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommand;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFGlvn;
import com.raygroupintl.vista.mtoken.TIndirection;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerial;

public class TCommandMerge extends TCommand {	
	private static class TFArgument extends TFSerial {
		@Override
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{TFGlvn.getInstance(), TFConstChar.getInstance('='), TFExpr.getInstance()}; 
		}

		@Override
		protected int getCodeNextIsNull(IToken[] foundTokens) {
			int n = foundTokens.length;
			if (n == 0) {
				return RETURN_NULL;
			}
			if (n == 1) {
				if (foundTokens[0] instanceof TIndirection) {
					return RETURN_TOKEN;
				} else {
					return this.getErrorCode();
				}
			}
			return CONTINUE;
		}
		
		@Override		
		protected int getCodeStringEnds(IToken[] foundTokens) {
			int n = foundTokens.length;
			if ((n == 0) && (foundTokens[0] instanceof TIndirection)) {
				return 0;
			} else {
				return this.getErrorCode();
			}
		}
	}	
	
	public TCommandMerge(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("M", "MERGE");
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
