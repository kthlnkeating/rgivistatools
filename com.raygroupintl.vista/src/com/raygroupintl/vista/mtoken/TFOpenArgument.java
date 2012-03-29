package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllOptional;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFOpenArgument extends TFParallelCharBased {
	private static class TFOpenParameters extends TFAllOptional {
		@Override
		protected ITokenFactory[] getFactories() {
			TFDeviceParams p = new TFDeviceParams();
			TFExpr e = TFExpr.getInstance();
			ITokenFactory f = TFParallelCharBased.getInstance(e, '(', TFCommaDelimitedList.getInstance(e));
			ITokenFactory c = TFConstChar.getInstance(':');
			return new ITokenFactory[]{p, c, e, c, f};
		}
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '@') {
			return TFIndirection.getInstance();
		} else {
			return TFSerialRO.getInstance(TFExpr.getInstance(), TFAllRequired.getInstance(TFConstChar.getInstance(':'), new TFOpenParameters()));
		}
	}
}
