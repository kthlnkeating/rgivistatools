package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFSeq;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;

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
				public ITokenFactory get(IToken[] previousTokens) {
					int n = previousTokens.length;
					switch (n) {
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
	
		protected int getCodeNextIsNull(IToken[] foundTokens) {
			int n = foundTokens.length;
			switch (n) {
			case 0: 
				return RETURN_NULL;
			case 1:
				return this.getErrorCode();
			default: 
				assert(false);
				return CONTINUE;
			}
		}
		
		protected int getCodeStringEnds(IToken[] foundTokens) {
			assert(foundTokens.length == 1);
			return this.getErrorCode();
		}
	
		public static TFExprTail getInstance(MVersion version) {
			return new TFExprTail(version);
		}
	}
	
	@Override
	protected ITokenFactory getRequired() {
		if (this.version == MVersion.CACHE) {
			return ChoiceSupply.get(MTFSupply.getInstance(this.version).getTFExprAtom(), '#', TFCacheClassMethod.getInstance());
		} else {
			return MTFSupply.getInstance(this.version).getTFExprAtom();
		}
	}
	
	@Override
	protected ITokenFactory getOptional() {
		TFExprTail et = TFExprTail.getInstance(this.version); 
		return TFList.getInstance(et);
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
