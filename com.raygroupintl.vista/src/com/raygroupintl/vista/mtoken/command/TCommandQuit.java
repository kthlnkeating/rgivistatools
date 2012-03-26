package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommand;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TCommandQuit extends TCommand {
	public TCommandQuit(String identifier) {
		super(identifier);
	}
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFParallelCharBased.getInstance(TFExpr.getInstance(), '@', TFIndirection.getInstance());		
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("Q", "QUIT");
	}		
}