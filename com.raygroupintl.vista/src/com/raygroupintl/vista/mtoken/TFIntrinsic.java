package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.token.TArray;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialBase;
import com.raygroupintl.vista.token.TList;

public class TFIntrinsic extends TFSerialBase {	
	@Override
	protected ITokenFactorySupply getFactorySupply() {
		return new ITokenFactorySupply() {			
			@Override
			public int getCount() {
				return 4;
			}
			
			@Override
			public ITokenFactory get(IToken[] previousTokens) {
				int n = previousTokens.length;
				switch (n) {
					case 0:
						return TFAllRequired.getInstance(TFConstChar.getInstance('$'), TFIdent.getInstance());
					case 1:
						return TFConstChar.getInstance('(');
					case 2: {
						String name = ((TArray) previousTokens[0]).get(1).getStringValue().toUpperCase();
						TFExpr expr = TFExpr.getInstance();
						if (name.equals("S") || name.equals("SELECT")) {
							TFAllRequired f = TFAllRequired.getInstance(expr, TFConstChar.getInstance(':'), expr);
							return TFDelimitedList.getInstance(f, ',');
						} else {
							return TFDelimitedList.getInstance(expr, ',');
						}
					}
					case 3:
						return TFConstChar.getInstance(')');
					default:
						return null;						
				}
			}
		};
	}

	@Override
	protected int getCodeNextIsNull(IToken[] foundTokens) {
		int n = foundTokens.length;
		if (n == 0) {
			return RETURN_NULL;
		} else if (n == 1) {
			return RETURN_TOKEN;
		} else {
			return MError.ERR_UNMATCHED_PARANTHESIS;
		}
	}
	
	@Override
	protected int getCodeStringEnds(IToken[] foundTokens) {
		int n = foundTokens.length;
		if (n == 1) {
			return 0;
		} else {
			return MError.ERR_UNMATCHED_PARANTHESIS;	
		}
	}
	
	@Override
	protected IToken getToken(IToken[] foundTokens) {
		TIdent name = (TIdent) ((TArray) foundTokens[0]).get(1);		
		if (foundTokens[1] == null) {
			return TIntrinsicVar.getInstance(name);			
		} else {
			TList arguments = (TList) foundTokens[2];
			return TIntrinsicFunc.getInstance(name, new TActualList(arguments));
		}		
	}
	
	public static TFIntrinsic getInstance() {
		return new TFIntrinsic();
	}
}
