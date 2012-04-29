package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFSeq;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;

public class TFExpr extends TFSeqRO {
	private MVersion version;
	
	private TFExpr(MVersion version) {
		this.version = version;
	}
	
	private static class TFExprTail extends TFSeq {
		private MVersion version;
		
		private TFExprTail(MVersion version) {
			this.version = version;
		}
		
		protected ITokenFactorySupply getFactorySupply() {
			return new ITokenFactorySupply() {			
				@Override
				public int getCount() {
					return 2;
				}
				
				private ITokenFactory choose1st(IToken token0th) {
					String value = token0th.getStringValue();
					if (value.charAt(value.length()-1) == '?') {
						return TFPattern.getInstance(TFExprTail.this.version);
					} else {
						return MTFSupply.getInstance((TFExprTail.this.version)).getTFExprAtom();
					}
				}
				
				@Override
				public ITokenFactory get(int seqIndex, IToken[] previousTokens) {
					switch (seqIndex) {
						case 0: 
							return TFOperator.getInstance();
						case 1:
							return this.choose1st(previousTokens[0]);
						case 2:
							return null;
						default: 
							assert(false);
							return null;
					}					
				}
			};
		}
	
		protected int validateNull(int seqIndex, IToken[] foundTokens) {
			switch (seqIndex) {
			case 0: 
				return RETURN_NULL;
			case 1:
				return this.getErrorCode();
			default: 
				assert(false);
				return CONTINUE;
			}
		}
		
		protected int validateEnd(int seqIndex, IToken[] foundTokens) {
			assert(seqIndex == 0);
			return this.getErrorCode();
		}
	
		public static TFExprTail getInstance(MVersion version) {
			return new TFExprTail(version);
		}
	}
	
	@Override
	protected ITokenFactory[] getFactories() {
		TFExprTail et = TFExprTail.getInstance(this.version); 
		ITokenFactory f1 = TFList.getInstance(et);
		if (this.version == MVersion.CACHE) {
			ITokenFactory f0 = ChoiceSupply.get(MTFSupply.getInstance(this.version).getTFExprAtom(), '#', TFCacheClassMethod.getInstance());
			return new ITokenFactory[]{f0, f1};
		} else {
			ITokenFactory f0 = MTFSupply.getInstance(this.version).getTFExprAtom();
			return new ITokenFactory[]{f0, f1};
		}
	}

	@Override
	protected TExpr getToken(IToken[] foundTokens) {
		TExpr result = new TExpr();
		result.add(foundTokens[0]);
		if (foundTokens[1] != null) {
			result.addAll((TList) foundTokens[1]);
		}
		return result;
	}
	
	public static TFExpr getInstance(MVersion version) {
		return new TFExpr(version);
	}
}
