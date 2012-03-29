package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TFKillArgument extends TFParallelCharBased {
	private static class TFExclusiveArgument extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			if (ch == '@') {
				return TFIndirection.getInstance();				
			} else {
				return TFName.getInstance();
			}
		}		
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		switch(ch) {
		case '(': {
			TFExclusiveArgument tfEA = new TFExclusiveArgument();
			TFDelimitedList tfDL = TFDelimitedList.getInstance(tfEA, ',');
			return TFInParantheses.getInstance(tfDL);
		}
		case '@':
			return TFIndirection.getInstance();
		default:
			return TFGlvn.getInstance();
		}
	}
}

