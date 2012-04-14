package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TFKillArgument extends TFParallelCharBased {
	private MVersion version;
	
	private TFKillArgument(MVersion version) {
		this.version = version;
	}
	
	private static class TFExclusiveArgument extends TFParallelCharBased {
		private MVersion version;
		
		private TFExclusiveArgument(MVersion version) {
			this.version = version;
		}
		
		@Override
		protected ITokenFactory getFactory(char ch) {
			if (ch == '@') {
				return TFIndirection.getInstance(this.version);				
			} else {
				return TFName.getInstance();
			}
		}		
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		switch(ch) {
		case '(': {
			TFExclusiveArgument tfEA = new TFExclusiveArgument(this.version);
			TFDelimitedList tfDL = TFDelimitedList.getInstance(tfEA, ',');
			return TFInParantheses.getInstance(tfDL);
		}
		case '@':
			return TFIndirection.getInstance(this.version);
		default:
			return TFGlvn.getInstance(this.version);
		}
	}
	
	public static TFKillArgument getInstance(MVersion version) {
		return new TFKillArgument(version);
	}
}

