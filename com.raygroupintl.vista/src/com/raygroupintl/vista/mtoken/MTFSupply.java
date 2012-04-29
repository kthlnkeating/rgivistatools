package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TFChar;
import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFChoiceBasic;
import com.raygroupintl.bnf.TFChoiceOnChar0th;
import com.raygroupintl.bnf.TFChoiceOnChar1st;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFConstString;
import com.raygroupintl.bnf.TFEmptyVerified;
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

public abstract class MTFSupply {
	protected abstract void initialize();
	
	public abstract ITokenFactory getTFEnvironment();
	
	public abstract ITokenFactory getTFExprItem();
	
	public abstract ITokenFactory getTFGvnAll();
	
	public abstract ITokenFactory getTFGlvn();

	public abstract ITokenFactory getTFExprAtom();	
	
	public abstract ITokenFactory getTFActual();

	protected static abstract class CommonSupply extends MTFSupply {
		public ITokenFactory dot = TFChar.DOT;
		public ITokenFactory pipe = new TFConstChar('|');
		
		public ITokenFactory name = TFName.getInstance();
		
		public ITokenFactory numlit = TFNumLit.getInstance();		
	}
		
	public static class Std95Supply extends CommonSupply {	
		private MVersion version = MVersion.ANSI_STD_95;

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
		
		private static class TFEnvironment extends TFChoice {
			private MVersion version;
			
			private TFEnvironment(MVersion version) {
				this.version = version;
			}
				
			@Override
			protected ITokenFactory getFactory(char ch) {
				if (ch == '|') {
					ITokenFactory d = new TFConstChar('|');
					return TFSeqRequired.getInstance(d, TFExpr.getInstance(this.version), d);
				} else if (ch == '[') {
					ITokenFactory l = new TFConstChar('[');
					ITokenFactory f = TFCommaDelimitedList.getInstance(MTFSupply.getInstance(this.version).getTFExprAtom(), ',');
					ITokenFactory r = new TFConstChar(']');			
					return TFSeqRequired.getInstance(l, f, r);
				} else {
					return null;
				}
			}
		}
		
		private ITokenFactory environment = new TFChoiceBasic();
		
		private TFChoiceBasic exprAtom = new TFChoiceBasic();
		private TFChoiceOnChar0th actual = new TFChoiceOnChar0th();
		private TFChoiceOnChar0th exprItem = new TFChoiceOnChar0th();
		private TFChoiceOnChar0th glvn = new TFChoiceOnChar0th();
		private TFChoiceOnChar1st gvnAll = new TFChoiceOnChar1st();
		
		public ITokenFactory getTFEnvironment() {
			return environment;
		}
		
		public ITokenFactory getTFExprItem() {
			return this.exprItem;
		}
		
		public ITokenFactory getTFGvnAll() {
			return this.gvnAll;
		}
		
		public ITokenFactory getTFGlvn() {
			return this.glvn;
		}
	
		public ITokenFactory getTFExprAtom() {
			return this.exprAtom;
		}
		
		public ITokenFactory getTFActual() {
			return this.actual;
		}
	
		@Override
		protected void initialize() {
			if (version == MVersion.CACHE) {
				this.exprAtom.setFactories(new ITokenFactory[]{getTFGlvn(), getTFExprItem(), TFCacheClassMethod.getInstance()});
			} else {
				this.exprAtom.setFactories(new ITokenFactory[]{getTFGlvn(), getTFExprItem()});
			}			
			
			{
				ICharPredicate[] predsDollar = {new CharPredicate('$'), new CharPredicate('&'), new LetterPredicate()};
				ITokenFactory fDollar = ChoiceSupply.get('$', null, predsDollar, 
					new TFExtrinsic(this.version), new TFExternal(this.version), TFIntrinsic.getInstance(this.version));
	
				ICharPredicate[] preds = {
					new CharPredicate('"'), new CharPredicate('$'),
					new CharsPredicate('\'', '+', '-'), new CharPredicate('.'), new CharPredicate('('),
					new DigitPredicate()};
				this.exprItem.setChoices(preds, new TFStringLiteral(), fDollar, 
							new TFUnaryOperatedExprItem(), this.numlit,
							TFInParantheses.getInstance(TFExpr.getInstance(this.version)), this.numlit);
			}
		
			{
				ICharPredicate[] preds = {new CharPredicate('$'), new CharPredicate('('), new CharsPredicate('%', '|', '['), new LetterPredicate()};
				this.gvnAll.setLeadingChar('^');
				this.gvnAll.setDefault(TFSyntaxError.getInstance());
				this.gvnAll.setChoices( preds, new TFGvnSsvn(this.version), new TFGvnNaked(this.version), 
							TFGvn.getInstance(this.version), TFGvn.getInstance(this.version));
			}
			
		    {
				ICharPredicate[] preds = {new IdentifierStartPredicate(), new CharPredicate('^'), new CharPredicate('@')};
				this.glvn.setChoices(preds, TFLvn.getInstance(this.version), getTFGvnAll(), TFIndirection.getInstance(this.version));
			}
	
			{
				ICharPredicate[] predsDot = {new DigitPredicate(), new IdentifierStartPredicate(), new CharPredicate('@')};
				ITokenFactory[] fsDot = {
						this.numlit,
						TFSeqRequired.getInstance(dot, name),
						TFSeqRequired.getInstance(dot, TFIndirection.getInstance(this.version))
				};				
				ITokenFactory fDot = ChoiceSupply.get('.', TFSyntaxError.getInstance(), predsDot, fsDot);
				ITokenFactory f0 = TFEmptyVerified.getInstance(',');
				ITokenFactory f1 = TFEmptyVerified.getInstance(')');
				ICharPredicate[] predsAll = {new CharPredicate('.'), new CharPredicate(','), new CharPredicate(')')};
				this.actual.setDefault(TFExpr.getInstance(this.version));
				this.actual.setChoices(predsAll, fDot, f0, f1);
			}
			
			{
				environment = new TFEnvironment(version);
			}			

		}
	}

	public static class CacheSupply extends CommonSupply {	
		private MVersion version = MVersion.CACHE;

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
		
		private static class TFEnvironment extends TFChoice {
			private MVersion version;
			
			private TFEnvironment(MVersion version) {
				this.version = version;
			}
				
			@Override
			protected ITokenFactory getFactory(char ch) {
				if (ch == '|') {
					ITokenFactory d = new TFConstChar('|');
					return TFSeqRequired.getInstance(d, TFExpr.getInstance(this.version), d);
				} else if (ch == '[') {
					ITokenFactory l = new TFConstChar('[');
					ITokenFactory f = TFCommaDelimitedList.getInstance(MTFSupply.getInstance(this.version).getTFExprAtom(), ',');
					ITokenFactory r = new TFConstChar(']');			
					return TFSeqRequired.getInstance(l, f, r);
				} else {
					return null;
				}
			}
		}
		
		private ITokenFactory environment = new TFChoiceBasic();
		private TFChoiceBasic exprAtom = new TFChoiceBasic();
		private TFChoiceOnChar0th actual = new TFChoiceOnChar0th();
		private TFChoiceOnChar0th exprItem = new TFChoiceOnChar0th();
		private TFChoiceOnChar0th glvn = new TFChoiceOnChar0th();
		private TFChoiceOnChar1st gvnAll = new TFChoiceOnChar1st();
		
		public ITokenFactory getTFEnvironment() {
			return environment;
		}
		
		public ITokenFactory getTFExprItem() {
			return this.exprItem;
		}
		
		public ITokenFactory getTFGvnAll() {
			return this.gvnAll;
		}
		
		public ITokenFactory getTFGlvn() {
			return this.glvn;
		}
	
		public ITokenFactory getTFExprAtom() {
			return this.exprAtom;
		}
		
		public ITokenFactory getTFActual() {
			return this.actual;
		}
	
		@Override
		protected void initialize() {
			if (version == MVersion.CACHE) {
				this.exprAtom.setFactories(new ITokenFactory[]{getTFGlvn(), getTFExprItem(), TFCacheClassMethod.getInstance()});
			} else {
				this.exprAtom.setFactories(new ITokenFactory[]{getTFGlvn(), getTFExprItem()});
			}			
			
			{
				ICharPredicate[] predsDollar = {new CharPredicate('$'), new CharPredicate('&'), new LetterPredicate()};
				ITokenFactory fDollar = ChoiceSupply.get('$', null, predsDollar, 
					new TFExtrinsic(this.version), new TFExternal(this.version), TFIntrinsic.getInstance(this.version));
	
				ICharPredicate[] preds = {
					new CharPredicate('"'), new CharPredicate('$'),
					new CharsPredicate('\'', '+', '-'), new CharPredicate('.'), new CharPredicate('('),
					new DigitPredicate()};
				this.exprItem.setChoices(preds, new TFStringLiteral(), fDollar, 
							new TFUnaryOperatedExprItem(), this.numlit,
							TFInParantheses.getInstance(TFExpr.getInstance(this.version)), this.numlit);
			}
		
			{
				ICharPredicate[] preds = {new CharPredicate('$'), new CharPredicate('('), new CharsPredicate('%', '|', '['), new LetterPredicate()};
				this.gvnAll.setLeadingChar('^');
				this.gvnAll.setDefault(TFSyntaxError.getInstance());
				this.gvnAll.setChoices( preds, new TFGvnSsvn(this.version), new TFGvnNaked(this.version), 
							TFGvn.getInstance(this.version), TFGvn.getInstance(this.version));
			}
			
		    {
				ICharPredicate[] preds = {new IdentifierStartPredicate(), new CharPredicate('^'), new CharPredicate('@')};
				this.glvn.setChoices(preds, TFLvn.getInstance(this.version), getTFGvnAll(), TFIndirection.getInstance(this.version));
			}
	
			{
				ICharPredicate[] predsDot = {new DigitPredicate(), new IdentifierStartPredicate(), new CharPredicate('@')};
				ITokenFactory[] fsDot = {
						this.numlit,
						TFSeqRequired.getInstance(dot, name),
						TFSeqRequired.getInstance(dot, TFIndirection.getInstance(this.version))
				};				
				ITokenFactory fDot = ChoiceSupply.get('.', TFSyntaxError.getInstance(), predsDot, fsDot);
				ITokenFactory f0 = TFEmptyVerified.getInstance(',');
				ITokenFactory f1 = TFEmptyVerified.getInstance(')');
				ICharPredicate[] predsAll = {new CharPredicate('.'), new CharPredicate(','), new CharPredicate(')')};
				this.actual.setDefault(TFExpr.getInstance(this.version));
				this.actual.setChoices(predsAll, fDot, f0, f1);
			}
			
			{
				environment = new TFEnvironment(version);
			}			

		}
	}
	
	
	private static MTFSupply CACHE_SUPPLY;
	private static MTFSupply STD_95_SUPPLY;
	
	
	public static MTFSupply getInstance(MVersion version) {
		switch (version) {
			case CACHE: {
				if (CACHE_SUPPLY == null) {
					CACHE_SUPPLY = new CacheSupply();
					CACHE_SUPPLY.initialize();
				}
				return CACHE_SUPPLY;
			}
			case ANSI_STD_95: {
				if (STD_95_SUPPLY == null) {
					STD_95_SUPPLY = new Std95Supply();
					STD_95_SUPPLY.initialize();
				}
				return STD_95_SUPPLY;
			}
			default:
				return null;
		}
	}	
}
