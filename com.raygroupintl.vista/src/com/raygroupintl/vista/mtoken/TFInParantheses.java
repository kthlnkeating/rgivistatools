package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TEmpty;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqStatic;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFInParantheses extends TFSeqStatic {
	protected abstract ITokenFactory getInnerfactory();
	
	@Override
	protected ITokenFactory[] getFactories() {
		TFConstChar l = new TFConstChar('(');
		TFConstChar r = new TFConstChar(')');
		ITokenFactory i = this.getInnerfactory();
		return new ITokenFactory[]{l, i, r};
	}

	@Override
	protected final int getCodeStringEnds(IToken[] foundTokens) {
		return this.getErrorCode();
	}		
	
	@Override
	protected IToken getToken(IToken[] foundTokens) {
		if (foundTokens[1] == null) {
			return new TInParantheses(new TEmpty());
		} else {
			return new TInParantheses(foundTokens[1]);
		}
	}		
	
	@Override
	protected int getCodeNextIsNull(IToken[] foundTokens) {
		if (foundTokens.length == 0) {
			return RETURN_NULL;
		} else {
			return this.getErrorCode();
		}
	}	

	private static abstract class TFInParanthesesOptional extends TFInParantheses {
		@Override
		protected final int getCodeNextIsNull(IToken[] foundTokens) {
			if (foundTokens.length == 0) {
				return RETURN_NULL;
			} else if (foundTokens.length == 1) {
				return CONTINUE;
			} else {
				return this.getErrorCode();
			}
		}
		
		public static TFInParanthesesOptional getInstance(final ITokenFactory f) {
			return new TFInParanthesesOptional() {			
				@Override
				protected ITokenFactory getInnerfactory() {
					return f;
				}
			};			
		}
	}
	
	public static TFInParantheses getInstance(final ITokenFactory f) {
		return new TFInParantheses() {			
			@Override
			protected ITokenFactory getInnerfactory() {
				return f;
			}
		};
	}

	public static TFInParantheses getInstance(final ITokenFactory f, boolean innerRequired) {
		if (innerRequired) {
			return TFInParantheses.getInstance(f);
		} else {
			return TFInParanthesesOptional.getInstance(f);			
		}
	}
}
