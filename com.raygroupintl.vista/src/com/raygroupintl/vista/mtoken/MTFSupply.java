package com.raygroupintl.vista.mtoken;

import java.util.EnumMap;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFConstString;
import com.raygroupintl.bnf.TFEmpty;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.bnf.TFSyntaxError;
import com.raygroupintl.fnds.ICharPredicate;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.m.struct.IdentifierStartPredicate;
import com.raygroupintl.struct.CharPredicate;
import com.raygroupintl.struct.CharsPredicate;
import com.raygroupintl.struct.DigitPredicate;
import com.raygroupintl.struct.LetterPredicate;

public class MTFSupply {
	private static final class TFExtrinsic extends TFSeqRequired {
		private MVersion version; 
		
		private TFExtrinsic(MVersion version) {			
			this.version = version;
		}
		
		@Override
		protected final ITokenFactory[] getFactories() {
			return new ITokenFactory[]{new TFConstString("$$"), TFDoArgument.getInstance(this.version, true)};
		}

		@Override
		protected final IToken getToken(IToken[] foundTokens) {
			return new TExtrinsic(foundTokens[1]);
		}
	}

	static class TFGvnNaked extends TFExprListInParantheses {
		private TFGvnNaked(MVersion version) {
			super(version);
		}
		
		@Override
		protected ITokenFactory[] getFactories() {
			TFConstChar c = TFConstChar.getInstance('^');
			TFExprListInParantheses r = TFExprListInParantheses.getInstance(this.version);
			return new ITokenFactory[]{c, r};
		}
		
		@Override
		protected IToken getToken(IToken[] foundTokens) {
			return new TGlobalNaked(foundTokens[1]);
		}		
	}
	
	static class TFGvnSsvn extends TFSeqRequired {
		private MVersion version;
		
		private TFGvnSsvn(MVersion version) {
			this.version = version;
		}
		
		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory c = new TFConstString("^$");
			ITokenFactory i = new TFIdent();
			ITokenFactory p = new TFInParantheses() {				
				@Override
				protected ITokenFactory getInnerfactory() {
					return TFDelimitedList.getInstance(TFExpr.getInstance(version), ',');
				}
			};
			return new ITokenFactory[]{c, i, p};
		}		
		
		@Override
		protected IToken getToken(IToken[] foundTokens) {			
			return TSsvn.getInstance((TIdent) foundTokens[1], foundTokens[2]);
		}		
	}
	
	class TFUnaryOperatedExprItem extends TFSeqRequired {
		@Override
		protected ITokenFactory[] getFactories() {
			ITokenFactory[] result = {new TFConstChars("+-\'"), getTFExprAtom()};
			return result;
		}		
	}
	
	private MVersion version;
	
	private MTFSupply(MVersion version) {
		this.version = version;
	}
	
	private ITokenFactory exprAtom;
	private ITokenFactory actual;
	private ITokenFactory exprItem;
	private ITokenFactory glvn;
	private ITokenFactory gvnAll;
	
	public ITokenFactory getTFExprItem() {
		if (exprItem == null) {
			ICharPredicate[] predsDollar = {new CharPredicate('$'), new CharPredicate('&'), new LetterPredicate()};
			ITokenFactory fDollar = ChoiceSupply.get('$', null, predsDollar, 
					new TFExtrinsic(this.version), new TFExternal(this.version), TFIntrinsic.getInstance(this.version));

			ICharPredicate[] preds = {
					new CharPredicate('"'), new CharPredicate('$'),
					new CharsPredicate('\'', '+', '-'), new CharPredicate('.'), new CharPredicate('('),
					new DigitPredicate()};
			exprItem = ChoiceSupply.get(null, preds, 
							new TFStringLiteral(), fDollar, 
							new TFUnaryOperatedExprItem(), TFNumLit.getInstance(),
							TFInParantheses.getInstance(TFExpr.getInstance(this.version)),
							TFNumLit.getInstance());
		}			
		return exprItem;
	}
	
	public ITokenFactory getTFGvnAll() {
		if (gvnAll == null) {
			ICharPredicate[] preds = {new CharPredicate('$'), new CharPredicate('('), new CharsPredicate('%', '|', '['), new LetterPredicate()};
			gvnAll = ChoiceSupply.get('^', TFSyntaxError.getInstance(), preds, 
						new TFGvnSsvn(this.version), new TFGvnNaked(this.version), 
						TFGvn.getInstance(this.version), TFGvn.getInstance(this.version));
		}
		return gvnAll;
	}
	
	public ITokenFactory getTFGlvn() {
		if (glvn == null) {
			ICharPredicate[] preds = {new IdentifierStartPredicate(), new CharPredicate('^'), new CharPredicate('@')};
			glvn = ChoiceSupply.get(null, preds, TFLvn.getInstance(this.version), getTFGvnAll(), TFIndirection.getInstance(this.version));
		}
		return glvn;
	}

	public ITokenFactory getTFExprAtom() {
		if (exprAtom == null) {
			if (version == MVersion.CACHE) {
				exprAtom = ChoiceSupply.get(getTFGlvn(), getTFExprItem(), TFCacheClassMethod.getInstance()) ;
			} else {
				exprAtom = ChoiceSupply.get(getTFGlvn(), getTFExprItem());
			}			
		}
		return exprAtom;
	}
	
	public ITokenFactory getTFActual() {
		if (actual == null) {
			ICharPredicate[] predsDot = {new DigitPredicate(), new IdentifierStartPredicate(), new CharPredicate('@')};
			ITokenFactory[] fsDot = {
					TFNumLit.getInstance(),
					TFSeqRequired.getInstance(TFConstChar.getInstance('.'), TFName.getInstance()),
					TFSeqRequired.getInstance(TFConstChar.getInstance('.'), TFIndirection.getInstance(this.version))
			};				
			ITokenFactory fDot = ChoiceSupply.get('.', TFSyntaxError.getInstance(), predsDot, fsDot);
			ITokenFactory f0 = TFEmpty.getInstance(',');
			ITokenFactory f1 = TFEmpty.getInstance(')');				
			actual = ChoiceSupply.get(TFExpr.getInstance(this.version), ".,)", fDot, f0, f1);
		}
		return actual;
	}

	private static EnumMap<MVersion, MTFSupply> SUPPLIES = new EnumMap<MVersion, MTFSupply>(MVersion.class);

	public static MTFSupply getInstance(MVersion version) {
		MTFSupply r = SUPPLIES.get(version);
		if (r == null) {
			r = new MTFSupply(version);
			SUPPLIES.put(version, r);
		}
		return r;
	}	
}
