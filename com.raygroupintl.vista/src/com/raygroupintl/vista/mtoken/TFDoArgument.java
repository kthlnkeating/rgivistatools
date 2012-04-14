package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFChar;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFConstString;
import com.raygroupintl.vista.token.TFNull;
import com.raygroupintl.vista.token.TFParallel;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialBase;
import com.raygroupintl.vista.token.TFSerialOR;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TFSerialROR;

public class TFDoArgument extends TFSerialBase {
	private MVersion version;
	
	protected TFDoArgument(MVersion version) {		
		this.version = version;
	}
	
	private static TFSerialROR getCacheSystemCall() {
		ITokenFactory system = new TFConstString("$SYSTEM", true);
		ITokenFactory methods = TFList.getInstance(TFAllRequired.getInstance(TFChar.DOT, TFName.getInstance()));
		ITokenFactory arguments = TFActualList.getInstance(MVersion.CACHE);
		return TFSerialROR.getInstance(system, methods, arguments);
	}
	
	private static ITokenFactory getFactory0(IToken[] previousTokens, final MVersion version) {
		TFLabel tfl = TFLabel.getInstance();
		TFIndirection tfi = TFIndirection.getInstance(version);
		if (version == MVersion.CACHE) {
			ITokenFactory f = TFSerialRO.getInstance(tfl, TFAllRequired.getInstance(TFChar.DOT, TFName.getInstance()));
			return TFParallelCharBased.getInstance(f, '@', tfi, '#', TFCacheClassMethod.getInstance(), '$', getCacheSystemCall());
		} else {		
			TFParallel tf = TFParallel.getInstance(tfl, tfi);
			return tf;
		}
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

	private static ITokenFactory getRoutineSpecification(MVersion version) {
		ITokenFactory tfEnv = TFEnvironment.getInstance(version);
		if (version == MVersion.CACHE) {
			ITokenFactory tfNameOrObj = TFName.getInstance();
			ITokenFactory objMethod = TFAllRequired.getInstance(TFConstChar.getInstance('.'), TFName.getInstance());			
			ITokenFactory tfName = TFSerialRO.getInstance(tfNameOrObj, objMethod);
			return TFSerialOR.getInstance(tfEnv, tfName);
		} else {
			ITokenFactory tfName = TFName.getInstance();
			return TFSerialOR.getInstance(tfEnv, tfName);
		}
	}
	
	private static ITokenFactory getFactory3(IToken[] previousTokens, MVersion version) {
		if (previousTokens[2] == null) {
			return new TFNull();
		} else {
			//ITokenFactory tfEnv = TFEnvironment.getInstance();
			//ITokenFactory tfName = TFName.getInstance();
			ITokenFactory tfEnvName = getRoutineSpecification(version); //TFSerialOR.getInstance(tfEnv, tfName);
			ITokenFactory tfInd = TFIndirection.getInstance(version);
			return TFParallel.getInstance(tfEnvName, tfInd);
		}
	}

	private static ITokenFactory getFactory4(IToken[] previousTokens, MVersion version) {
		if ((previousTokens[1] != null) || (previousTokens[3] instanceof TIndirection)) {
			return new TFNull();
		} else {
			return TFActualList.getInstance(version);
		}
	}

	protected ITokenFactorySupply getFactorySupply(final int count) {
		return new ITokenFactorySupply() {				
			@Override
			public int getCount() {
				return count;
			}
			
			@Override
			public ITokenFactory get(IToken[] previousTokens) {
				int n = previousTokens.length;
				switch (n) {
					case 0: return TFDoArgument.getFactory0(previousTokens, TFDoArgument.this.version); 
					case 1: return TFDoArgument.getFactory1(previousTokens, TFDoArgument.this.version); 
					case 2: return TFDoArgument.getFactory2(previousTokens); 
					case 3: return TFDoArgument.getFactory3(previousTokens, TFDoArgument.this.version); 
					case 4: return TFDoArgument.getFactory4(previousTokens, TFDoArgument.this.version); 
					case 5: return TCommandName.getTFPostCondition(previousTokens, version);
					default:
						assert(false);
						return null;
				}
			}
		};
	}

	@Override
	protected ITokenFactorySupply getFactorySupply() {
		return this.getFactorySupply(6);
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
	
	private static class TFDoArgumentNoPostCondition extends TFDoArgument {
		protected TFDoArgumentNoPostCondition(MVersion version) {
			super(version);
		}
		
		
		@Override
		protected ITokenFactorySupply getFactorySupply() {
			return getFactorySupply(5);
		}
	}
		
	public static TFDoArgument getInstance(MVersion version) {
		return new TFDoArgument(version);
	}
	
	public static TFDoArgument getInstance(MVersion version, boolean noPostCondition) {
		if (noPostCondition) {
			return new TFDoArgumentNoPostCondition(version);
		} else {
			return new TFDoArgument(version);
		}
	}	
}	

