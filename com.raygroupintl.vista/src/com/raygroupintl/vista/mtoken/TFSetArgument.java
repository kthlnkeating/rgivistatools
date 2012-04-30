package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqStatic;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFSetArgument extends TFSeqStatic {
	private MVersion version;
	
	private TFSetArgument(MVersion version) {
		this.version = version;
	}
		
	private static class TFSetLeft extends TFChoice {
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
				return MTFSupply.getInstance(version).getTFGlvn();
			}
		}
		
		public static TFSetLeft getInstance(MVersion version) {
			return new TFSetLeft(version);
		}
	}

	private static class TFSetDestination extends TFChoice {
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
		ITokenFactory expr = MTFSupply.getInstance(version).getTFExpr();
		return new ITokenFactory[]{ 
				TFSetDestination.getInstance(this.version), 
				TFConstChar.getInstance('='), 
				(this.version == MVersion.CACHE) ? ChoiceSupply.get(expr, '#', TFCacheClassMethod.getInstance()) : expr
		}; 
	}

	@Override
	protected int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return RETURN_NULL;
		}
		if (seqIndex == 1) {
			if (foundTokens[0] instanceof TIndirection) {
				return RETURN_TOKEN;
			} else {
				return this.getErrorCode();
			}
		}
		return CONTINUE;
	}
	
	@Override		
	protected int validateEnd(int seqIndex, IToken[] foundTokens) {
		if ((seqIndex == 0) && (foundTokens[0] instanceof TIndirection)) {
			return 0;
		} else {
			return this.getErrorCode();
		}
	}
	
	public static TFSetArgument getInstance(MVersion version) {
		return new TFSetArgument(version);
	}
}	

