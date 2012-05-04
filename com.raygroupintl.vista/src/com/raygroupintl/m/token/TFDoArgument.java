package com.raygroupintl.m.token;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFChar;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFNull;
import com.raygroupintl.bnf.TFSeq;
import com.raygroupintl.bnf.TFSeqOR;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;

public class TFDoArgument extends TFSeq {
	private MVersion version;
	
	protected TFDoArgument(MVersion version) {		
		this.version = version;
	}
	
	private static ITokenFactory getFactory0(IToken[] previousTokens, final MVersion version) {
		//return MTFSupply.getInstance(version).dentryspec_0;
		
		ITokenFactory tfl = MTFSupply.getInstance(version).label;
		ITokenFactory tfi = MTFSupply.getInstance(version).indirection;
		if (version == MVersion.CACHE) {
			MTFSupply.CacheSupply cs =  (MTFSupply.CacheSupply) MTFSupply.getInstance(MVersion.CACHE);
			ITokenFactory f = TFSeqRO.getInstance(tfl, TFSeqRequired.getInstance(TFChar.DOT, TFName.getInstance()));
			return ChoiceSupply.get(f, "@#$", tfi, cs.classmethod, cs.systemcall);
		} else {	
			ITokenFactory tf = ChoiceSupply.get(tfl, tfi);
			return tf;
		}
	}
	
	private static ITokenFactory getFactory1(IToken[] previousTokens, final MVersion version) {
		return MTFSupply.getInstance(version).lineoffset;
	}

	private static ITokenFactory getFactory2(IToken[] previousTokens) {
		return TFConstChar.getInstance('^');
	}

	private static ITokenFactory getFactory3(IToken[] previousTokens, MVersion version) {
		if (previousTokens[2] == null) {
			return new TFNull();
		} else {
			return MTFSupply.getInstance(version).doroutineind;
		}
	}

	private static ITokenFactory getFactory4(IToken[] previousTokens, MVersion version) {
		if ((previousTokens[1] != null) || (previousTokens[3] instanceof TIndirection)) {
			return new TFNull();
		} else {
			return MTFSupply.getInstance(version).actuallist;
		}
	}

	protected ITokenFactorySupply getFactorySupply(final int count) {
		return new ITokenFactorySupply() {				
			@Override
			public int getCount() {
				return count;
			}
			
			@Override
			public ITokenFactory get(int seqIndex, IToken[] previousTokens) {
				switch (seqIndex) {
					case 0: return TFDoArgument.getFactory0(previousTokens, TFDoArgument.this.version); 
					case 1: return TFDoArgument.getFactory1(previousTokens, TFDoArgument.this.version); 
					case 2: return TFDoArgument.getFactory2(previousTokens); 
					case 3: return TFDoArgument.getFactory3(previousTokens, TFDoArgument.this.version); 
					case 4: return TFDoArgument.getFactory4(previousTokens, TFDoArgument.this.version); 
					case 5: return MTFSupply.getInstance(version).postcondition;
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

