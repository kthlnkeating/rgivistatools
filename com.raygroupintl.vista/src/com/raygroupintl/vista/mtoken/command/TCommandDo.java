package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommandName;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFDoArgument;
import com.raygroupintl.vista.struct.MNameWithMnemonic;

public class TCommandDo extends TCommandName {	
	public TCommandDo(String identifier) {
		super(identifier);
	}
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(TFDoArgument.getInstance());
	}
 	
	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("D", "DO");
	}		
}

