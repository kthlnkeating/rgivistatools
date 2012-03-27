package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommandName;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFGvn;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.mtoken.TFName;
import com.raygroupintl.vista.mtoken.TFTimeout;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialORO;

public class TCommandLock extends TCommandName {
	public TCommandLock(String identifier) {
		super(identifier);
	}
	
	@Override
	public ITokenFactory getArgumentFactory() {
		ITokenFactory tfNRef = TFParallelCharBased.getInstance(TFName.getInstance(), '^', TFGvn.getInstance(), '@', TFIndirection.getInstance());		
		ITokenFactory tfNRefOrList = TFParallelCharBased.getInstance(tfNRef, '(', TFCommaDelimitedList.getInstance(tfNRef));
		ITokenFactory e = TFSerialORO.getInstance(TFConstChars.getInstance("+-"), tfNRefOrList, TFTimeout.getInstance());
		return TFCommaDelimitedList.getInstance(e);
	}
 	
	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("L", "LOCK");
	}		
}

