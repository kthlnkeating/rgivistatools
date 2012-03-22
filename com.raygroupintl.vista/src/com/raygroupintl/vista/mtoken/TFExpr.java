package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.token.TFSerialBase;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TList;

public class TFExpr extends TFSerialRO {
	private static class TFExprTail extends TFSerialBase {
		protected ITokenFactorySupply getFactorySupply() {
			return new ITokenFactorySupply() {			
				@Override
				public int getCount() {
					return 2;
				}
				
				private ITokenFactory choose1st(IToken token0th) {
					String value = token0th.getStringValue();
					if (value.charAt(value.length()-1) == '?') {
						return TFPattern.getInstance();
					} else {
						return TFExprAtom.getInstance();
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
	
		public static TFExprTail getInstance() {
			return new TFExprTail();
		}
	}
	
	@Override
	protected ITokenFactory getRequired() {
		return TFExprAtom.getInstance();
	}
	
	@Override
	protected ITokenFactory getOptional() {
		TFExprTail et = TFExprTail.getInstance(); 
		return TFList.getInstance(et);
	}
	
	@Override
	protected TExpr getTokenRequired(IToken requiredToken) {
		TExpr result = new TExpr();
		result.add(requiredToken);
		return result;
	}
	
	@Override
	protected TExpr getTokenBoth(IToken requiredToken, IToken optionalToken) {
		TExpr expr = this.getTokenRequired(requiredToken);
		expr.addAll((TList) optionalToken);
		return expr;
	}
	
	public static TFExpr getInstance() {
		return new TFExpr();
	}
}
