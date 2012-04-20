package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFEmpty;
import com.raygroupintl.bnf.TFNull;
import com.raygroupintl.bnf.TFSeq;
import com.raygroupintl.bnf.TFSeqOR;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;

public class TFJobArgument extends TFSeq {
	private MVersion version;
	
	private TFJobArgument(MVersion version) {
		this.version = version;
	}
	
	private static ITokenFactory getFactory0(IToken[] previousTokens, MVersion version) {
		TFLabel tfl = TFLabel.getInstance();
		TFIndirection tfi = TFIndirection.getInstance(version);
		ITokenFactory tf = ChoiceSupply.get(tfl, tfi);
		return tf; 
	}
	
	private static ITokenFactory getFactory1(IToken[] previousTokens, final MVersion version) {
		if (previousTokens[0] == null) {
			return new TFNull();
		} else {
			return new TFSeqRequired() {								
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
			ITokenFactory tfEnvName = TFSeqOR.getInstance(tfEnv, tfName);
			ITokenFactory tfInd = TFIndirection.getInstance(version);
			return ChoiceSupply.get(tfEnvName, tfInd);
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
		ITokenFactory processParams = ChoiceSupply.get(e, ":(", TFEmpty.getInstance(), TFDelimitedList.getInstance(e, ':', true, true));
		ITokenFactory jobParams = TFSeqRequired.getInstance(TFConstChar.getInstance(':'), processParams); 
		return TFSeqRO.getInstance(jobParams, TFTimeout.getInstance(version));
	}

	protected ITokenFactorySupply getFactorySupply() {
		return new ITokenFactorySupply() {				
			@Override
			public int getCount() {
				return 6;
			}
			
			@Override
			public ITokenFactory get(int seqIndex, IToken[] previousTokens) {
				switch (seqIndex) {
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

	protected int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 2) {
			if (foundTokens[0] == null) {
				return RETURN_NULL;
			} 
		}
		if (seqIndex == 3) {
			if (foundTokens[2] != null) {
				return this.getErrorCode();
			}
		}
		return CONTINUE;
	}
	
	protected int validateEnd(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 2) {
			return this.getErrorCode();
		}
		return 0;
	}
	
	public static TFJobArgument getInstance(MVersion version) {
		return new TFJobArgument(version);
	}
}	