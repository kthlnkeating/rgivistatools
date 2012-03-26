package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommand;
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

public class TCommandOpen extends TCommand {	
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
	
	private static class TFArgument extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			if (ch == '@') {
				return TFIndirection.getInstance();
			} else {
				return TFSerialRO.getInstance(TFExpr.getInstance(), TFAllRequired.getInstance(TFConstChar.getInstance(':'), new TFOpenParameters()));
			}
		}
	}
	
	public TCommandOpen(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("O", "OPEN");
	}		
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
 	
	@Override
	public IToken getArgument(String line, int fromIndex) {
		return this.getNewArgument(line, fromIndex);
	}
}