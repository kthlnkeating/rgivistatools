package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommand;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFGvn;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.mtoken.TFName;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialORO;

public class TCommandLock extends TCommand {
	public TCommandLock(String identifier) {
		super(identifier);
	}
	
	@Override
	protected ITokenFactory getArgumentFactory() {
		ITokenFactory tfNRef = TFParallelCharBased.getInstance(TFName.getInstance(), '^', TFGvn.getInstance(), '@', TFIndirection.getInstance());		
		ITokenFactory tfNRefOrList = TFParallelCharBased.getInstance(tfNRef, '(', TFCommaDelimitedList.getInstance(tfNRef));
		return TFSerialORO.getInstance(TFConstChars.getInstance("+-"), tfNRefOrList, TFExpr.getInstance());
	}
 	
	@Override
	public IToken getArgument(String line, int fromIndex) {
		return this.getNewArgument(line, fromIndex);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("L", "LOCK");
	}		
}

