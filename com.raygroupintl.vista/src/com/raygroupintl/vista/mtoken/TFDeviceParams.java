package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFDeviceParams extends TFParallelCharBased {
	private MVersion version;
	
	protected TFDeviceParams(MVersion version) {		
		this.version = version;
	}
	
	private static class TFDeviceParam extends TFSerialRO {
		private MVersion version;
		
		protected TFDeviceParam(MVersion version) {		
			this.version = version;
		}
		
		protected ITokenFactory getRequired() {
			return TFExpr.getInstance(this.version);	
		}
		
		protected ITokenFactory getOptional() {
			return TFAllRequired.getInstance(TFConstChar.getInstance('='), TFExpr.getInstance(this.version));	
		}
	}
		
	@Override
	protected ITokenFactory getFactory(char ch) {
		ITokenFactory f = new TFDeviceParam(this.version);
		if (ch == '(') {
			return TFInParantheses.getInstance(TFDelimitedList.getInstance(f, ':'));
		} else {
			return f;
		}
	}
}
