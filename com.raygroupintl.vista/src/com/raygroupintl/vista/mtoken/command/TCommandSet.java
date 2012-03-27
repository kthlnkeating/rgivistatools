package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommandName;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFDelimitedList;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFGlvn;
import com.raygroupintl.vista.mtoken.TFInParantheses;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.mtoken.TFIntrinsic;
import com.raygroupintl.vista.mtoken.TIndirection;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerial;

public class TCommandSet extends TCommandName {	
	private static class TFSetLeft extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			switch(ch) {
			case '$':
				return TFIntrinsic.getInstance();
			case '@':
				return TFIndirection.getInstance();
			default:
				return TFGlvn.getInstance();
			}
		}
		
		public static TFSetLeft getInstance() {
			return new TFSetLeft();
		}
	}
	
	private static class TFSetDestination extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			TFSetLeft tfSL = TFSetLeft.getInstance();
			if (ch == '(') {
				TFDelimitedList tfDL = TFDelimitedList.getInstance(tfSL, ',');
				return TFInParantheses.getInstance(tfDL);
			} else {
				return new TFSetLeft();
			}
		}		
		
		public static TFSetDestination getInstance() {
			return new TFSetDestination();
		}
	}
		
	private static class TFArgument extends TFSerial {
		@Override
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{TFSetDestination.getInstance(), TFConstChar.getInstance('='), TFExpr.getInstance()}; 
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
			if ((n == 1) && (foundTokens[0] instanceof TIndirection)) {
				return 0;
			} else {
				return this.getErrorCode();
			}
		}
	}	
	
	public TCommandSet(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("S", "SET");
	}		
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
}
