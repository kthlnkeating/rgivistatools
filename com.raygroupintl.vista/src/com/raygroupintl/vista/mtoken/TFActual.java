package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallel;

public class TFActual extends TFParallel {
	private static class TFActualName extends TFParallel {
		@Override
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFName(), new TFIndirection()};
		}		
	}
		
	private static class TFByReference extends TFAllRequired {
		@Override
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstChar('.'), new TFActualName()};
		}		
	}
		
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{new TFByReference(), new TFExpr()};
	}
}
