package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommandName;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFLvn;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialROO;

public class TCommandFor extends TCommandName {	
	private static class TFArgument extends TFAllRequired {
		@Override
		protected ITokenFactory[] getFactories() {
			TFExpr tfExpr = TFExpr.getInstance();
			TFAllRequired tfFromTo = TFAllRequired.getInstance(TFConstChar.getInstance(':'), tfExpr);
			ITokenFactory RHS = TFSerialROO.getInstance(tfExpr, tfFromTo, tfFromTo);
			return new ITokenFactory[]{TFLvn.getInstance(), TFConstChar.getInstance('='), RHS}; 
		}
	}	
	
	public TCommandFor(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("F", "FOR");
	}		
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
}
