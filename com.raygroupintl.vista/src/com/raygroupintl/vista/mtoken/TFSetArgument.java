package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFChoice;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerial;

public class TFSetArgument extends TFSerial {
	private MVersion version;
	
	private TFSetArgument(MVersion version) {
		this.version = version;
	}
		
	private static class TFSetLeft extends TFParallelCharBased {
		private MVersion version;
		
		private TFSetLeft(MVersion version) {
			this.version = version;
		}
			
		@Override
		protected ITokenFactory getFactory(char ch) {
			switch(ch) {
			case '$':
				return TFIntrinsic.getInstance(this.version);
			case '@':
				return TFIndirection.getInstance(this.version);
			default:
				return TFGlvn.getInstance(this.version);
			}
		}
		
		public static TFSetLeft getInstance(MVersion version) {
			return new TFSetLeft(version);
		}
	}

	private static class TFSetDestination extends TFParallelCharBased {
		private MVersion version;
		
		private TFSetDestination(MVersion version) {
			this.version = version;
		}
			
		@Override
		protected ITokenFactory getFactory(char ch) {
			TFSetLeft tfSL = TFSetLeft.getInstance(this.version);
			if (ch == '(') {
				TFDelimitedList tfDL = TFDelimitedList.getInstance(tfSL, ',');
				return TFInParantheses.getInstance(tfDL);
			} else {
				return new TFSetLeft(this.version);
			}
		}		
		
		public static TFSetDestination getInstance(MVersion version) {
			return new TFSetDestination(version);
		}
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		TFExpr expr = TFExpr.getInstance(this.version);
		return new ITokenFactory[]{ 
				TFSetDestination.getInstance(this.version), 
				TFConstChar.getInstance('='), 
				(this.version == MVersion.CACHE) ? TFChoice.getInstance(expr, '#', TFCacheClassMethod.getInstance()) : expr
		}; 
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
	
	public static TFSetArgument getInstance(MVersion version) {
		return new TFSetArgument(version);
	}
}	

