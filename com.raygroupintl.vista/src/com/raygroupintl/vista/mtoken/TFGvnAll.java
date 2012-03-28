package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFConstString;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSyntaxError;

public class TFGvnAll extends TFParallelCharBased {	
	static class TFGvnNaked extends TFExprListInParantheses {
		@Override
		protected ITokenFactory[] getFactories() {
			TFConstChar c = TFConstChar.getInstance('^');
			TFExprListInParantheses r = TFExprListInParantheses.getInstance();
			return new ITokenFactory[]{c, r};
		}
		
		@Override
		protected IToken getToken(IToken[] foundTokens) {
			return new TGlobalNaked(foundTokens[1]);
		}		
	}
	
	static class TFGvnSsvn extends TFAllRequired {
		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory c = new TFConstString("^$");
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
			return TSsvn.getInstance((TIdent) foundTokens[1], foundTokens[2]);
		}		
	}
	
	@Override
	protected ITokenFactory getFactory(char ch) {				
		return null;
	}
			
	@Override
	protected ITokenFactory getFactory(char ch, char ch2) {
		if (ch == '^') {
			switch (ch2) {
				case '$': 
					return new TFGvnSsvn();  
				case '(': 
					return new TFGvnNaked();
				case '%':
				case '|':
					return new TFGvn();							
				default: 
					if (Library.isIdent(ch2)) {
						return new TFGvn();
					} else {
						return new TFSyntaxError();
					}
			}
		} else {
			return null;
		}
	}
	
	public static TFGvnAll getInstance() {
		return new TFGvnAll();
	}
}
