package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommand;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFDelimitedList;
import com.raygroupintl.vista.mtoken.TFGlvn;
import com.raygroupintl.vista.mtoken.TFInParantheses;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.mtoken.TFName;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TCommandKill extends TCommand {	
	private static class TFExclusiveArgument extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			if (ch == '@') {
				return TFIndirection.getInstance();				
			} else {
				return TFName.getInstance();
			}
		}		
	}
	
	private static class TFArgument extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			switch(ch) {
			case '(': {
				TFExclusiveArgument tfEA = new TFExclusiveArgument();
				TFDelimitedList tfDL = TFDelimitedList.getInstance(tfEA, ',');
				return TFInParantheses.getInstance(tfDL);
			}
			case '@':
				return TFIndirection.getInstance();
			default:
				return TFGlvn.getInstance();
			}
		}
	}
	
	public TCommandKill(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("K", "KILL");
	}		
	
	@Override
	protected ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
 	
	@Override
	public IToken getArgument(String line, int fromIndex) {
		return this.getNewArgument(line, fromIndex);
	}
}