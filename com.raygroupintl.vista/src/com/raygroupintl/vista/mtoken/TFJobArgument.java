package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFEmpty;
import com.raygroupintl.vista.token.TFNull;
import com.raygroupintl.vista.token.TFChoice;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialBase;
import com.raygroupintl.vista.token.TFSerialOR;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFJobArgument extends TFSerialBase {
	private MVersion version;
	
	private TFJobArgument(MVersion version) {
		this.version = version;
	}
	
	private static ITokenFactory getFactory0(IToken[] previousTokens, MVersion version) {
		TFLabel tfl = TFLabel.getInstance();
		TFIndirection tfi = TFIndirection.getInstance(version);
		TFChoice tf = TFChoice.getInstance(tfl, tfi);
		return tf; 
	}
	
	private static ITokenFactory getFactory1(IToken[] previousTokens, final MVersion version) {
		if (previousTokens[0] == null) {
			return new TFNull();
		} else {
			return new TFAllRequired() {								
				@Override
				protected ITokenFactory[] getFactories() {
					TFConstChar tfc = TFConstChar.getInstance('+');
					TFExpr tfe = TFExpr.getInstance(version);
					return new ITokenFactory[]{tfc, tfe};
				}
			};
		}
	}

	private static ITokenFactory getFactory2(IToken[] previousTokens) {
		return TFConstChar.getInstance('^');
	}

	private static ITokenFactory getFactory3(IToken[] previousTokens, final MVersion version) {
		if (previousTokens[2] == null) {
			return new TFNull();
		} else {
			ITokenFactory tfEnv = TFEnvironment.getInstance(version);
			ITokenFactory tfName = TFName.getInstance();
			ITokenFactory tfEnvName = TFSerialOR.getInstance(tfEnv, tfName);
			ITokenFactory tfInd = TFIndirection.getInstance(version);
			return TFChoice.getInstance(tfEnvName, tfInd);
		}
	}

	private static ITokenFactory getFactory4(IToken[] previousTokens, final MVersion version) {
		if ((previousTokens[1] != null) || (previousTokens[3] instanceof TIndirection)) {
			return new TFNull();
		} else {
			return TFActualList.getInstance(version);
		}
	}

	private static ITokenFactory getFactory5(IToken[] previousTokens, final MVersion version) {
		TFExpr e = TFExpr.getInstance(version);
		ITokenFactory processParams = TFParallelCharBased.getInstance(e, ':', TFEmpty.getInstance(':'), '(', TFDelimitedList.getInstance(e, ':', true, true));
		ITokenFactory jobParams = TFAllRequired.getInstance(TFConstChar.getInstance(':'), processParams); 
		return TFSerialRO.getInstance(jobParams, TFTimeout.getInstance(version));
	}

	protected ITokenFactorySupply getFactorySupply() {
		return new ITokenFactorySupply() {				
			@Override
			public int getCount() {
				return 6;
			}
			
			@Override
			public ITokenFactory get(IToken[] previousTokens) {
				int n = previousTokens.length;
				switch (n) {
					case 0: return TFJobArgument.getFactory0(previousTokens, TFJobArgument.this.version); 
					case 1: return TFJobArgument.getFactory1(previousTokens, TFJobArgument.this.version); 
					case 2: return TFJobArgument.getFactory2(previousTokens); 
					case 3: return TFJobArgument.getFactory3(previousTokens, TFJobArgument.this.version); 
					case 4: return TFJobArgument.getFactory4(previousTokens, TFJobArgument.this.version); 
					case 5: return TFJobArgument.getFactory5(previousTokens, TFJobArgument.this.version);
					default:
						assert(false);
						return null;
				}
			}
		};
	}

	protected int getCodeNextIsNull(IToken[] foundTokens) {
		int n = foundTokens.length;
		if (n == 2) {
			if (foundTokens[0] == null) {
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
	
	protected int getCodeStringEnds(IToken[] foundTokens) {
		if (foundTokens.length == 3) {
			return this.getErrorCode();
		}
		return 0;
	}
	
	public static TFJobArgument getInstance(MVersion version) {
		return new TFJobArgument(version);
	}
}	