package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.mtoken.TCommand;
import com.raygroupintl.vista.mtoken.TFActualList;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFDelimitedList;
import com.raygroupintl.vista.mtoken.TFEnvironment;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFInParantheses;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.mtoken.TFLabel;
import com.raygroupintl.vista.mtoken.TFName;
import com.raygroupintl.vista.mtoken.TFTimeout;
import com.raygroupintl.vista.mtoken.TIndirection;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFNull;
import com.raygroupintl.vista.token.TFParallel;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialBase;
import com.raygroupintl.vista.token.TFSerialOR;
import com.raygroupintl.vista.token.TFSerialRO;

public class TCommandJob extends TCommand {	
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
			if (previousTokens[2] == null) {
				return new TFNull();
			} else {
				ITokenFactory tfEnv = TFEnvironment.getInstance();
				ITokenFactory tfName = TFName.getInstance();
				ITokenFactory tfEnvName = TFSerialOR.getInstance(tfEnv, tfName);
				ITokenFactory tfInd = TFIndirection.getInstance();
				return TFParallel.getInstance(tfEnvName, tfInd);
			}
		}

		private static ITokenFactory getFactory4(IToken[] previousTokens) {
			if ((previousTokens[1] != null) || (previousTokens[3] instanceof TIndirection)) {
				return new TFNull();
			} else {
				return new TFActualList();
			}
		}

		private static ITokenFactory getFactory5(IToken[] previousTokens) {
			TFExpr e = TFExpr.getInstance();
			TFDelimitedList re = TFDelimitedList.getInstance(e, ':');
			ITokenFactory processParams = TFParallelCharBased.getInstance(e, '(', TFInParantheses.getInstance(re));
			ITokenFactory jobParams = TFAllRequired.getInstance(TFConstChar.getInstance(':'), processParams); 
			return TFSerialRO.getInstance(jobParams, TFTimeout.getInstance());
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
	}	
	
	public TCommandJob(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("J", "JOB");
	}		
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
 	
	@Override
	public IToken getArgument(String line, int fromIndex) {
		return this.getNewArgument(line, fromIndex);
	}
}
