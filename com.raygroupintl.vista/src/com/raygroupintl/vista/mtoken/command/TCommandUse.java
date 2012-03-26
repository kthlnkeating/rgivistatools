package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommandName;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFDeviceParams;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFAllOptional;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TCommandUse extends TCommandName {	
	private static class TFUseParameters extends TFAllOptional {
		@Override
		protected ITokenFactory[] getFactories() {
			TFDeviceParams p = new TFDeviceParams();
			TFExpr e = TFExpr.getInstance();
			ITokenFactory c = TFConstChar.getInstance(':');
			return new ITokenFactory[]{p, c, e};
		}
	}
	
	private static class TFArgument extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			if (ch == '@') {
				return TFIndirection.getInstance();
			} else {
				return TFSerialRO.getInstance(TFExpr.getInstance(), TFAllRequired.getInstance(TFConstChar.getInstance(':'), new TFUseParameters()));
			}
		}
	}
	
	public TCommandUse(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("U", "USE");
	}		
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
}