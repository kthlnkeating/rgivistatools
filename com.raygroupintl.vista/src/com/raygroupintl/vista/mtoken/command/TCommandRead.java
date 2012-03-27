package com.raygroupintl.vista.mtoken.command;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TCommandName;
import com.raygroupintl.vista.mtoken.TFCommaDelimitedList;
import com.raygroupintl.vista.mtoken.TFExpr;
import com.raygroupintl.vista.mtoken.TFFormat;
import com.raygroupintl.vista.mtoken.TFGlvn;
import com.raygroupintl.vista.mtoken.TFIndirection;
import com.raygroupintl.vista.mtoken.TFStringLiteral;
import com.raygroupintl.vista.mtoken.TFTimeout;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialROO;
import com.raygroupintl.vista.token.TFSerialRRO;

public class TCommandRead extends TCommandName {	
	private static ITokenFactory getTFReadcountInstance() {
		return TFAllRequired.getInstance(TFConstChar.getInstance('#'), TFExpr.getInstance());
	}
	
	private static class TFArgument extends TFParallelCharBased {
		@Override
		protected ITokenFactory getFactory(char ch) {
			switch(ch) {
				case '!':
				case '#':
				case '?':
				case '/':
					return TFFormat.getInstance();
				case '"':
					return TFStringLiteral.getInstance();
				case '*':
					return TFSerialRRO.getInstance(TFConstChar.getInstance('*'), TFGlvn.getInstance(), TFTimeout.getInstance());
				case '@':
					return TFIndirection.getInstance();
				default: 
					return TFSerialROO.getInstance(TFGlvn.getInstance(), getTFReadcountInstance(), TFTimeout.getInstance());
			}
		}
	}
	
	public TCommandRead(String identifier) {
		super(identifier);
	}

	@Override
	protected MNameWithMnemonic getNameWithMnemonic() {
		return new MNameWithMnemonic("R", "READ");
	}		
	
	@Override
	public ITokenFactory getArgumentFactory() {
		return TFCommaDelimitedList.getInstance(new TFArgument());
	}
}