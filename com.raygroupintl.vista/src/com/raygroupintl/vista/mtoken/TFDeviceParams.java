package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFDeviceParams extends TFParallelCharBased {
	private static class TFDeviceParam extends TFSerialRO {
		protected ITokenFactory getRequired() {
			return TFExpr.getInstance();	
		}
		
		protected ITokenFactory getOptional() {
			return TFAllRequired.getInstance(TFConstChar.getInstance('='), TFExpr.getInstance());	
		}
	}
		
	@Override
	protected ITokenFactory getFactory(char ch) {
		ITokenFactory f = new TFDeviceParam();
		if (ch == '(') {
			return TFInParantheses.getInstance(TFDelimitedList.getInstance(f, ':'));
		} else {
			return f;
		}
	}
}
