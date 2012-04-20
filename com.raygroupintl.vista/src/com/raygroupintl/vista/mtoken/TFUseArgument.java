package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFEmpty;
import com.raygroupintl.bnf.TFEmptyVerified;
import com.raygroupintl.bnf.TFSeqROO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.ITokenFactory;

public class TFUseArgument extends TFChoice {
	private MVersion version;
	
	private TFUseArgument(MVersion version) {
		this.version = version;
	}
	
	private static class TFUseDeviceParam extends TFChoice {
		private MVersion version;
	
		private TFUseDeviceParam(MVersion version) {
			this.version = version;
		}
		
		protected ITokenFactory getFactory(char ch) {
			switch (ch) {
				case ':':
				case ')':
					return TFEmpty.getInstance();
				default:
					return TFExpr.getInstance(this.version);
			}
		}
	}
		
	private static class TFUseDeviceParams extends TFChoice {			
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
				return TFEmpty.getInstance(); 
			} else {
				return f;
			}
		}
		
		public static TFUseDeviceParams getInstance(MVersion version) {
			return new TFUseDeviceParams(version);
		}
	}

	@Override
	protected ITokenFactory getFactory(char ch) {
		if (ch == '@') {
			return TFIndirection.getInstance(this.version);
		} else {
			return TFSeqROO.getInstance(TFExpr.getInstance(this.version), 
					TFSeqRequired.getInstance(TFConstChar.getInstance(':'), TFUseDeviceParams.getInstance(this.version)),
					TFSeqRequired.getInstance(TFConstChar.getInstance(':'), TFUseDeviceParams.getInstance(this.version)));
		}
	}
	
	public static TFUseArgument getInstance(MVersion version) {
		return new TFUseArgument(version);
	}
}