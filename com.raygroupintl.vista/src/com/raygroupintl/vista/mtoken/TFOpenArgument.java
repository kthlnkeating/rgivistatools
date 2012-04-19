package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllOptional;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.ChoiceSupply;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFOpenArgument extends TFParallelCharBased {
	private MVersion version;
	
	private TFOpenArgument(MVersion version) {
		this.version = version;
	}
	
	private static class TFOpenParameters extends TFAllOptional {
		private MVersion version;
		
		private TFOpenParameters(MVersion version) {
			this.version = version;
		}

		@Override
		protected ITokenFactory[] getFactories() {
			TFDeviceParams p = new TFDeviceParams(this.version);
			TFExpr e = TFExpr.getInstance(this.version);
			ITokenFactory f = ChoiceSupply.get(e, '(', TFCommaDelimitedList.getInstance(e));
			ITokenFactory c = TFConstChar.getInstance(':');
			return new ITokenFactory[]{p, c, e, c, f};
		}
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '@') {
			return TFIndirection.getInstance(this.version);
		} else {
			return TFSerialRO.getInstance(TFExpr.getInstance(this.version), TFAllRequired.getInstance(TFConstChar.getInstance(':'), new TFOpenParameters(this.version)));
		}
	}
	
	public static TFOpenArgument getInstance(MVersion version) {
		return new TFOpenArgument(version);
	}
	
}
