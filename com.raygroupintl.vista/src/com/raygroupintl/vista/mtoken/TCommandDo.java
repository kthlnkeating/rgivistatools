package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFNull;
import com.raygroupintl.vista.token.TFParallel;
import com.raygroupintl.vista.token.TFSerialBase;
import com.raygroupintl.vista.token.TFSerialOR;
import com.raygroupintl.vista.token.TSyntaxError;

public class TCommandDo extends TCommand {	
	private static class TFArgument extends TFSerialBase {
		private static ITokenFactory getFactory0(IToken[] previousTokens) {
			TFLabel tfl = TFLabel.getInstance();
			TFIndirection tfi = TFIndirection.getInstance();
			TFParallel tf = TFParallel.getInstance(tfl, tfi);
			return tf; 
		}
		
		private static ITokenFactory getFactory1(IToken[] previousTokens) {
			if (previousTokens[0] == null) {
				return new TFNull();
			} else {
				return new TFAllRequired() {								
					@Override
					protected ITokenFactory[] getFactories() {
						TFConstChar tfc = TFConstChar.getInstance('+');
						TFExpr tfe = TFExpr.getInstance();
						return new ITokenFactory[]{tfc, tfe};
					}
				};
			}
		}

		private static ITokenFactory getFactory2(IToken[] previousTokens) {
			return TFConstChar.getInstance('^');
		}

		private static ITokenFactory getFactory3(IToken[] previousTokens) {
			ITokenFactory tfEnv = TFEnvironment.getInstance();
			ITokenFactory tfName = TFName.getInstance();
			ITokenFactory tfEnvName = TFSerialOR.getInstance(tfEnv, tfName);
			ITokenFactory tfInd = TFIndirection.getInstance();
			return TFParallel.getInstance(tfEnvName, tfInd);
		}

		private static ITokenFactory getFactory4(IToken[] previousTokens) {
			if ((previousTokens[1] != null) || (previousTokens[3] instanceof TIndirection)) {
				return new TFNull();
			} else {
				return new TFActualList();
			}
		}

		private static ITokenFactory getFactory5(IToken[] previousTokens) {
			ITokenFactory tfColon = TFConstChar.getInstance(':');
			ITokenFactory tfExpr = TFExpr.getInstance();
			return TFAllRequired.getInstance(tfColon, tfExpr);
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
						case 0: return TFArgument.getFactory0(previousTokens); 
						case 1: return TFArgument.getFactory1(previousTokens); 
						case 2: return TFArgument.getFactory2(previousTokens); 
						case 3: return TFArgument.getFactory3(previousTokens); 
						case 4: return TFArgument.getFactory4(previousTokens); 
						case 5: return TFArgument.getFactory5(previousTokens);
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
				} else {
					return RETURN_TOKEN;
				}
			}
			if (n ==3) {
				return this.getErrorCode();
			}
			return CONTINUE;
		}
		
		protected int getCodeStringEnds(IToken[] foundTokens) {
			if (foundTokens.length == 3) {
				return this.getErrorCode();
			}
			return 0;
		}
	}	
	
	public TCommandDo(String identifier) {
		super(identifier);
	}
	
	@Override
	protected IToken getArgument(String line, int fromIndex) {
		ITokenFactory f = new TFList() {			
			@Override
			protected ITokenFactory getFactory() {
				return new TFArgument();
			}
		};
		IToken result = f.tokenize(line, fromIndex);
		if (result == null) {
			return TSyntaxError.getInstance(MError.ERR_GENERAL_SYNTAX, line, fromIndex, fromIndex);			
		}
		int index = fromIndex + result.getStringSize();
		if (index < line.length()) {
			char ch = line.charAt(index);
			if (ch != ' ') {
				return TSyntaxError.getInstance(MError.ERR_GENERAL_SYNTAX, line, index, fromIndex);
			}
		}
		return result;
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("D", "DO");
	}		
}

