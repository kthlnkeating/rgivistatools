package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFBasic;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFEol;
import com.raygroupintl.bnf.TFNull;
import com.raygroupintl.bnf.TFSeqStatic;
import com.raygroupintl.bnf.TFSyntaxError;
import com.raygroupintl.bnf.TSyntaxError;
import com.raygroupintl.fnds.ICharPredicate;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.struct.CharPredicate;
import com.raygroupintl.struct.EOLPredicate;
import com.raygroupintl.struct.LetterPredicate;

public class TFLine extends TFSeqStatic {
	private MVersion version;
	
	private TFLine(MVersion version) {
		this.version = version;
	}
	
	private static ITokenFactory getTFFormal() {
		TFName argument = TFName.getInstance();
		TFDelimitedList arguments = TFDelimitedList.getInstance(argument, ',');
		return TFInParantheses.getInstance(arguments, false);		
	}
			
	private static ITokenFactory getTFCommands(MVersion version) {
		ICharPredicate[] preds = {new LetterPredicate(), new CharPredicate(';')}; //, new EOLPredicate()};
		ITokenFactory f = ChoiceSupply.get(TFSyntaxError.getInstance(), preds, TFCommand.getInstance(version), new TFComment()); //, new TFNull());
		return TFList.getInstance(f);
	}
 	
	
	@Override
	protected ITokenFactory[] getFactories() { // label, formals, space, level, commands
		return new ITokenFactory[]{
				TFLabel.getInstance(),
				getTFFormal(),
				TFConstChars.getInstance(" \t"),
				TFBasic.getInstance('.', ' '),
				getTFCommands(this.version)//,
				//new TFEol()
		};
	}

	@Override
	protected int validateNull(int seqIndex, IToken[] foundTokens) {
		return 0;
	}

	@Override
	protected int validateEnd(int seqIndex, IToken[] foundTokens) {
		return 0;
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		return new TLine(foundTokens);
	}
	
	@Override
	protected IToken getTokenWhenSyntaxError(int seqIndex, IToken[] found, TSyntaxError error, int fromIndex) {
		found[seqIndex] = error;
		return new TLine(found);
	}
	
	public static TFLine getInstance(MVersion version) {
		return new TFLine(version);
	}
}
