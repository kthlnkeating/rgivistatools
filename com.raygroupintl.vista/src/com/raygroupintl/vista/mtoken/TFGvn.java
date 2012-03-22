package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialORO;

public class TFGvn extends TFAllRequired {	
	static class TFGvnNaked extends TFExprListInParantheses {
		@Override
		protected IToken getToken(IToken[] foundTokens) {
			return new TGlobalNaked(foundTokens[1]);
		}		
	}
	
	static class TFGvnNamed extends TFSerialORO {
		@Override
		protected ITokenFactory[] getFactories() {			
			TFEnvironment env = new TFEnvironment();
			TFName name = new TFName();
			TFExprListInParantheses exprList = new TFExprListInParantheses();
			return new ITokenFactory[]{env, name, exprList};
		}

		@Override
		protected IToken getToken(IToken[] foundTokens) {			
			return new TGlobalNamed(foundTokens);
		}		
	}
	
	static class TFGvnSsvn extends TFAllRequired {
		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory c = new TFConstChar('$');
			ITokenFactory i = new TFIdent();
			ITokenFactory p = new TFInParantheses() {				
				@Override
				protected ITokenFactory getInnerfactory() {
					return new TFExpr();
				}
			};
			return new ITokenFactory[]{c, i, p};
		}		
		
		@Override
		protected IToken getToken(IToken[] foundTokens) {			
			return new TSsvn(foundTokens[1], foundTokens[2]);
		}		
	}
	
	@Override
	protected ITokenFactory[] getFactories() {
		ITokenFactory c = new TFConstChar('^');
		ITokenFactory r = new TFParallelCharBased() {			
			@Override
			protected ITokenFactory getFactory(char ch) {
				switch (ch) {
					case '$': return new TFGvnSsvn();  
					case '(': return new TFGvnNaked();
					default: return new TFGvnNamed();
				}
			}
		};
		return new ITokenFactory[]{c, r};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		return foundTokens[1];
	}
}
