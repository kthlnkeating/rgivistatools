package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommand;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.mtoken.TFIntrinsic;
import com.raygroupintl.vista.mtoken.TFLvn;
import com.raygroupintl.vista.mtoken.TFName;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TCommandNew extends TCommand {	
	private static class TFArgument extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			switch(ch) {
			case '(': 
				return TFCommaDelimitedList.getInstance(TFLvn.getInstance());
			case '@':
				return TFIndirection.getInstance();
			case '$':
				return TFIntrinsic.getInstance();
			default:
				return TFName.getInstance();
			}
		}
	}
	
	public TCommandNew(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("N", "NEW");
	}		
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
}