package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.bnf.TFSeqStatic;
import com.raygroupintl.fnds.ITokenFactory;

public class TFOpenArgument extends TFChoice {
	private MVersion version;
	
	private TFOpenArgument(MVersion version) {
		this.version = version;
	}
	
	private static class TFOpenParameters extends TFSeqStatic {
		private MVersion version;
		
		private TFOpenParameters(MVersion version) {
			this.version = version;
		}

		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory p = MTFSupply.getInstance(version).deviceparams;
			ITokenFactory e = MTFSupply.getInstance(version).expr;
			ITokenFactory f = ChoiceSupply.get(e, '(', TFCommaDelimitedList.getInstance(e));
			ITokenFactory c = TFConstChar.getInstance(':');
			return new ITokenFactory[]{p, c, e, c, f};
		}
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '@') {
			return MTFSupply.getInstance(version).indirection;
		} else {
			return TFSeqRO.getInstance(MTFSupply.getInstance(version).expr, TFSeqRequired.getInstance(TFConstChar.getInstance(':'), new TFOpenParameters(this.version)));
		}
	}
	
	public static TFOpenArgument getInstance(MVersion version) {
		return new TFOpenArgument(version);
	}
	
}
