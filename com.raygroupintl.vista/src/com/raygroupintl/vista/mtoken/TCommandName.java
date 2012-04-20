package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.vista.struct.MError;

public abstract class TCommandName extends TKeyword {
	public TCommandName(String identifier) {
		super(identifier);
	}
	
	@Override
	public List<MError> getErrors() {
		return null;
	}

	protected static ITokenFactory getTFPostCondition(IToken[] previousTokens, MVersion version) {
		ITokenFactory tfColon = TFConstChar.getInstance(':');
		ITokenFactory tfExpr = TFExpr.getInstance(version);
		return TFSeqRequired.getInstance(tfColon, tfExpr);
	}
	
	public abstract ITokenFactory getArgumentFactory();
}

