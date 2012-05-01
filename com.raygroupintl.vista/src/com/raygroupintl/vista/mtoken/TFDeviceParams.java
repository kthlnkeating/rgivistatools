package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.ITokenFactory;

public class TFDeviceParams extends TFChoice {
	private MVersion version;
	
	protected TFDeviceParams(MVersion version) {		
		this.version = version;
	}
	
	private static class TFDeviceParam extends TFSeqRO {
		private MVersion version;
		
		protected TFDeviceParam(MVersion version) {		
			this.version = version;
		}
		
		@Override
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{
					MTFSupply.getInstance(version).expr,
					TFSeqRequired.getInstance(TFConstChar.getInstance('='), MTFSupply.getInstance(version).expr)};
		}
	}
		
	@Override
	protected ITokenFactory getFactory(char ch) {
		ITokenFactory f = new TFDeviceParam(this.version);
		if (ch == '(') {
			return TFDelimitedList.getInstance(f, ':', true, true);
		} else {
			return f;
		}
	}
	
	public static TFDeviceParams getInstance(MVersion version) {
		return new TFDeviceParams(version);
	}
}
