package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommand;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TCommandExecute extends TCommand {
	public TCommandExecute(String identifier) {
		super(identifier);
	}
	
	@Override
	protected ITokenFactory getArgumentFactory() {
		ITokenFactory tf = TFParallelCharBased.getInstance(TFExpr.getInstance(), '@', TFIndirection.getInstance());
		ITokenFactory pc = getTFPostCondition(null);
		return TFSerialRO.getInstance(tf, pc);
	}
 	
	@Override
	public IToken getArgument(String line, int fromIndex) {
		return this.getNewArgument(line, fromIndex);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("X", "EXECUTE");
	}		
}

