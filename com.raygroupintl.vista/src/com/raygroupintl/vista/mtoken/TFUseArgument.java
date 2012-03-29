package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFEmpty;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFUseArgument extends TFParallelCharBased {
	private static class TFUseDeviceParam extends TFParallelCharBased {
		protected ITokenFactory getFactory(char ch) {
			switch (ch) {
				case ':':
				case ')':
					return TFEmpty.getInstance(ch);
				default:
					return TFExpr.getInstance();
			}
		}
	}
		
	private static class TFUseDeviceParams extends TFParallelCharBased {			
		@Override
		protected ITokenFactory getFactory(char ch) {
			ITokenFactory f = new TFUseDeviceParam();
			if (ch == '(') {
				return TFInParantheses.getInstance(TFDelimitedList.getInstance(f, ':'));
			} else {
				return f;
			}
		}
	}

	/*	private static class TFUseParameters extends TFAllOptional {
		@Override
		protected ITokenFactory[] getFactories() {
			TFDeviceParams p = new TFDeviceParams();
			TFExpr e = TFExpr.getInstance();
			ITokenFactory c = TFConstChar.getInstance(':');
			return new ITokenFactory[]{p, c, e};
		}
	}
	*/	

	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '@') {
			return TFIndirection.getInstance();
		} else {
			return TFSerialRO.getInstance(TFExpr.getInstance(), TFAllRequired.getInstance(TFConstChar.getInstance(':'), new TFUseDeviceParams()));
		}
	}
}