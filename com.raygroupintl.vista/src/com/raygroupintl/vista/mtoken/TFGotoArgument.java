package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFNull;
import com.raygroupintl.vista.token.TFParallel;
import com.raygroupintl.vista.token.TFSerialBase;
import com.raygroupintl.vista.token.TFSerialOR;

public class TFGotoArgument extends TFSerialBase {
	private static ITokenFactory getFactory0(IToken[] previousTokens) {
		TFLabel tfl = TFLabel.getInstance();
		TFIndirection tfi = TFIndirection.getInstance();
		TFParallel tf = TFParallel.getInstance(tfl, tfi);
		return tf; 
	}
	
	private static ITokenFactory getFactory1(IToken[] previousTokens) {
		return new TFAllRequired() {								
			@Override
			protected ITokenFactory[] getFactories() {
				TFConstChar tfc = TFConstChar.getInstance('+');
				TFExpr tfe = TFExpr.getInstance();
				return new ITokenFactory[]{tfc, tfe};
			}
		};
	}

	private static ITokenFactory getFactory2(IToken[] previousTokens) {
		return TFConstChar.getInstance('^');
	}

	private static ITokenFactory getFactory3(IToken[] previousTokens) {
		if (previousTokens[2] == null) {
			return new TFNull();
		} else {
			ITokenFactory tfEnv = TFEnvironment.getInstance();
			ITokenFactory tfName = TFName.getInstance();
			ITokenFactory tfEnvName = TFSerialOR.getInstance(tfEnv, tfName);
			ITokenFactory tfInd = TFIndirection.getInstance();
			return TFParallel.getInstance(tfEnvName, tfInd);
		}
	}

	private static ITokenFactorySupply getFactorySupply(final int count) {
		return new ITokenFactorySupply() {				
			@Override
			public int getCount() {
				return count;
			}
			
			@Override
			public ITokenFactory get(IToken[] previousTokens) {
				int n = previousTokens.length;
				switch (n) {
					case 0: return TFGotoArgument.getFactory0(previousTokens); 
					case 1: return TFGotoArgument.getFactory1(previousTokens); 
					case 2: return TFGotoArgument.getFactory2(previousTokens); 
					case 3: return TFGotoArgument.getFactory3(previousTokens); 
					case 4: return TCommandName.getTFPostCondition(previousTokens);
					default:
						assert(false);
						return null;
				}
			}
		};
	}

	protected ITokenFactorySupply getFactorySupply() {
		return  getFactorySupply(5);
	}

	@Override
	protected int getCodeNextIsNull(IToken[] foundTokens) {
		int n = foundTokens.length;
		if (n == 2) {
			if ((foundTokens[0] == null) && (foundTokens[1] == null)) {
				return RETURN_NULL;
			} 
		}
		if (n == 3) {
			if (foundTokens[2] != null) {
				return this.getErrorCode();
			}
		}
		return CONTINUE;
	}
	
	@Override		
	protected int getCodeStringEnds(IToken[] foundTokens) {
		if (foundTokens.length == 3) {
			return this.getErrorCode();
		}
		return 0;
	}
	
	private static class TFGotoArgumentNoPostCondition extends TFGotoArgument {
		@Override
		protected ITokenFactorySupply getFactorySupply() {
			return  getFactorySupply(4);
		}
	}
		
	public static TFGotoArgument getInstance() {
		return new TFGotoArgument();
	}
	
	public static TFGotoArgument getInstance(boolean noPostCondition) {
		if (noPostCondition) {
			return new TFGotoArgumentNoPostCondition();
		} else {
			return new TFGotoArgument();
		}
	}
	
	
}