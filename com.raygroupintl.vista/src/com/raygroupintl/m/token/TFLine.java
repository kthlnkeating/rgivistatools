package com.raygroupintl.m.token;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TFBasic;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFList;
import com.raygroupintl.bnf.TFSeqStatic;
import com.raygroupintl.bnf.TFSyntaxError;
import com.raygroupintl.fnds.ICharPredicate;
import com.raygroupintl.struct.CharPredicate;
import com.raygroupintl.struct.LetterPredicate;
import com.raygroupintl.vista.struct.MError;

public class TFLine extends TFSeqStatic {
	private MTFSupply supply;
	
	private TFLine(MTFSupply supply) {
		this.supply = supply;
	}
	
	private TokenFactory getTFCommands(MTFSupply supply) {
		ICharPredicate[] preds = {new LetterPredicate(), new CharPredicate(';')};
		TokenFactory f = ChoiceSupply.get(new TFSyntaxError(MError.ERR_GENERAL_SYNTAX), preds, this.supply.command, new TFComment());
		TFList result = TFList.getInstance(f);
		result.setAddErrorToList(true);
		return result;
	}
 	
	
	@Override
	protected TokenFactory[] getFactories() { // label, formals, space, level, commands
		return new TokenFactory[]{
				this.supply.label,
				this.supply.lineformal,
				TFConstChars.getInstance(" \t"),
				TFBasic.getInstance('.', ' '),
				getTFCommands(this.supply)
		};
	}
	
	@Override
	protected Token getToken(String line, int fromIndex, Token[] foundTokens) {
		return new TLine(foundTokens);
	}
	
	public static TFLine getInstance(MTFSupply supply) {
		return new TFLine(supply);
	}
}
