package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;

public abstract class TCommand extends TKeyword {
	public TCommand(String identifier) {
		super(identifier);
	}
	
	@Override
	public List<MError> getErrors() {
		return null;
	}

	protected static ITokenFactory getTFPostCondition(IToken[] previousTokens) {
		ITokenFactory tfColon = TFConstChar.getInstance(':');
		ITokenFactory tfExpr = TFExpr.getInstance();
		return TFAllRequired.getInstance(tfColon, tfExpr);
	}
	
	public abstract ITokenFactory getArgumentFactory();
}

