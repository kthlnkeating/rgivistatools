package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFEmpty;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TFSerialROO;

public class TFUseArgument extends TFParallelCharBased {
	private MVersion version;
	
	private TFUseArgument(MVersion version) {
		this.version = version;
	}
	
	private static class TFUseDeviceParam extends TFParallelCharBased {
		private MVersion version;
	
		private TFUseDeviceParam(MVersion version) {
			this.version = version;
		}
		
		protected ITokenFactory getFactory(char ch) {
			switch (ch) {
				case ':':
				case ')':
					return TFEmpty.getInstance(ch);
				default:
					return TFExpr.getInstance(this.version);
			}
		}
		
		//public static TFUseDeviceParam getInstance(MVersion version) {
		//	return new TFUseDeviceParam(version);
		//}
	}
		
	private static class TFUseDeviceParams extends TFParallelCharBased {			
		private MVersion version;
		
		private TFUseDeviceParams(MVersion version) {
			this.version = version;
		}
		
		@Override
		protected ITokenFactory getFactory(char ch) {
			ITokenFactory f = new TFUseDeviceParam(this.version);
			if (ch == '(') {
				return TFInParantheses.getInstance(TFDelimitedList.getInstance(f, ':'));
			} else if (ch == ':') {
				return TFEmpty.getInstance(':'); 
			} else {
				return f;
			}
		}
		
		public static TFUseDeviceParams getInstance(MVersion version) {
			return new TFUseDeviceParams(version);
		}
	}

	/*	private static class TFUseParameters extends TFAllOptional {
		@Override
		protected ITokenFactory[] getFactories() {
			TFDeviceParams p = new TFDeviceParams();
			TFExpr e = TFExpr.getInstance();
			ITokenFactory c = TFConstChar.getInstance(':');
			return new ITokenFactory[]{p, c, e};
		}
	}
	*/	

	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '@') {
			return TFIndirection.getInstance(this.version);
		} else {
			return TFSerialROO.getInstance(TFExpr.getInstance(this.version), 
					TFAllRequired.getInstance(TFConstChar.getInstance(':'), TFUseDeviceParams.getInstance(this.version)),
					TFAllRequired.getInstance(TFConstChar.getInstance(':'), TFUseDeviceParams.getInstance(this.version)));
		}
	}
	
	public static TFUseArgument getInstance(MVersion version) {
		return new TFUseArgument(version);
	}
}