package com.raygroupintl.vista.mtoken;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFChar;
import com.raygroupintl.bnf.TFChoiceOnChar0th;
import com.raygroupintl.bnf.TFChoiceOnChar1st;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFConstString;
import com.raygroupintl.bnf.TFEmptyVerified;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.bnf.TFSyntaxError;
import com.raygroupintl.bnf.TokenAdapter;
import com.raygroupintl.bnf.annotation.Parser;
import com.raygroupintl.bnf.annotation.Sequence;
import com.raygroupintl.bnf.annotation.Choice;
import com.raygroupintl.bnf.annotation.List;
import com.raygroupintl.fnds.ICharPredicate;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenArray;
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
	
	public abstract ITokenFactory getTFExprTail();	
	
	public abstract ITokenFactory getTFExpr();	
	
	public abstract ITokenFactory getTFActual();

	public abstract ITokenFactory getTFIndirection();

	protected static abstract class CommonSupply extends MTFSupply {
		public ITokenFactory dot = TFChar.DOT;
		public ITokenFactory comma = TFChar.COMMA;
		public ITokenFactory pipe = new TFConstChar('|');
		public ITokenFactory lsqr = new TFConstChar('[');
		public ITokenFactory rsqr = new TFConstChar(']');
		public ITokenFactory lpar = new TFConstChar('(');
		public ITokenFactory rpar = new TFConstChar(')');
		public ITokenFactory qmark = new TFConstChar('?');
		public ITokenFactory at = new TFConstChar('@');

		public ITokenFactory nqmark = new TFConstString("'?");
		public ITokenFactory atlpar = new TFConstString("@(");
		
		public ITokenFactory name = TFName.getInstance();
		public ITokenFactory numlit = TFNumLit.getInstance();
		public ITokenFactory operator = TFOperator.getInstance();
		
		@Sequence(value={"pipe", "expr", "pipe"}, required="all")
		public ITokenFactory env_0;
		@List(value="expratom", delim="comma", left="lsqr", right="rsqr")
		public ITokenFactory env_1;
		@Choice({"env_0", "env_1"})
		public ITokenFactory environment;
		
		@Sequence(value={"qmark", "pattern"}, required="all")
		public ITokenFactory exprtail_s0;
		@Sequence(value={"nqmark", "pattern"}, required="all")
		public ITokenFactory exprtail_s1;
		@Sequence(value={"operator", "expratom"}, required="all")
		public ITokenFactory exprtail_s2;
		@Choice({"exprtail_s0", "exprtail_s1", "exprtail_s2"})
		public ITokenFactory exprtail_s;
		@List("exprtail_s")
		public ITokenFactory exprtail;

		@List(value="expr", delim="comma")
		public ITokenFactory exprlist;
		
		@Sequence(value={"at", "expratom"}, required="all")
		public ITokenFactory indirection_0;
		@Sequence(value={"atlpar", "exprlist", "rpar"}, required="all")
		public ITokenFactory indirection_1;
		@Sequence(value={"indirection_0", "indirection_1"}, required="ro")
		public ITokenFactory indirection;
		
		public ITokenFactory getTFExprTail() {
			return this.exprtail;
		}

		public ITokenFactory getTFIndirection() {
			return this.indirection;
		}
	
	}
		
	public static class Std95Supply extends CommonSupply {	
		static final Map<String, TokenAdapter> ADAPTERS = new HashMap<String, TokenAdapter>();
		static {
			ADAPTERS.put("indirection", new TokenAdapter() {				
				@Override
				public IToken convert(IToken[] tokens) {
					if (tokens[1] == null) {
						TArray t = (TArray) tokens[0];
						return new TIndirection(t.get(1));			
					} else {		
						TArray tReqArray = (TArray) tokens[0];
						ITokenArray tOptArray = (ITokenArray) tokens[1];
						IToken subscripts = tOptArray.get(1);
						return new TIndirection(tReqArray.get(1), subscripts);
					}
				}
			});
		}

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
						return TFDelimitedList.getInstance(MTFSupply.getInstance(version).getTFExpr(), ',');
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
				
		@Choice({"glvn", "exprItem"})
		public ITokenFactory expratom;

		public TFChoiceOnChar0th actual = new TFChoiceOnChar0th();
		public TFChoiceOnChar0th exprItem = new TFChoiceOnChar0th();
		public TFChoiceOnChar0th glvn = new TFChoiceOnChar0th();
		public TFChoiceOnChar1st gvnAll = new TFChoiceOnChar1st();
		
		
		@Sequence(value={"expratom", "exprtail"}, required="ro")
		public ITokenFactory expr;
		
		
		public ITokenFactory pattern = TFPattern.getInstance(this.version);
		
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
			return this.expratom;
		}
		
		public ITokenFactory getTFExpr() {
			return this.expr;
		}
		
		public ITokenFactory getTFActual() {
			return this.actual;
		}
	
		@Override
		protected void initialize() {
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
							TFInParantheses.getInstance(this.expr), this.numlit);
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
				this.glvn.setChoices(preds, TFLvn.getInstance(this.version), getTFGvnAll(), indirection);
			}
	
			{
				ICharPredicate[] predsDot = {new DigitPredicate(), new IdentifierStartPredicate(), new CharPredicate('@')};
				ITokenFactory[] fsDot = {
						this.numlit,
						TFSeqRequired.getInstance(dot, name),
						TFSeqRequired.getInstance(dot, indirection)
				};				
				ITokenFactory fDot = ChoiceSupply.get('.', TFSyntaxError.getInstance(), predsDot, fsDot);
				ITokenFactory f0 = TFEmptyVerified.getInstance(',');
				ITokenFactory f1 = TFEmptyVerified.getInstance(')');
				ICharPredicate[] predsAll = {new CharPredicate('.'), new CharPredicate(','), new CharPredicate(')')};
				this.actual.setDefault(this.expr);
				this.actual.setChoices(predsAll, fDot, f0, f1);
			}
		}
	}

	public static class CacheSupply extends CommonSupply {
		static final Map<String, TokenAdapter> ADAPTERS = new HashMap<String, TokenAdapter>();
		static {
			ADAPTERS.put("indirection", new TokenAdapter() {				
				@Override
				public IToken convert(IToken[] tokens) {
					if (tokens[1] == null) {
						TArray t = (TArray) tokens[0];
						return new TIndirection(t.get(1));			
					} else {		
						TArray tReqArray = (TArray) tokens[0];
						ITokenArray tOptArray = (ITokenArray) tokens[1];
						IToken subscripts = tOptArray.get(1);
						return new TIndirection(tReqArray.get(1), subscripts);
					}
				}
			});
		}
		
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
						return TFDelimitedList.getInstance(MTFSupply.getInstance(version).getTFExpr(), ',');
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
		
		@Choice({"glvn", "exprItem", "classmethod"})
		public ITokenFactory expratom;
		
		public TFChoiceOnChar0th actual = new TFChoiceOnChar0th();
		public TFChoiceOnChar0th exprItem = new TFChoiceOnChar0th();
		public TFChoiceOnChar0th glvn = new TFChoiceOnChar0th();
		public TFChoiceOnChar1st gvnAll = new TFChoiceOnChar1st();
		
		
		@Choice({"expratom", "classmethod"})
		public ITokenFactory expr_0;
		@Sequence(value={"expr_0", "exprtail"}, required="ro")
		public ITokenFactory expr;
		
		public ITokenFactory pattern = TFPattern.getInstance(this.version);
		public ITokenFactory classmethod = TFCacheClassMethod.getInstance();
		
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
			return this.expratom;
		}
		
		public ITokenFactory getTFExpr() {
			return this.expr;
		}
		
		public ITokenFactory getTFActual() {
			return this.actual;
		}
	
		@Override
		protected void initialize() {
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
							TFInParantheses.getInstance(this.expr), this.numlit);
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
				this.glvn.setChoices(preds, TFLvn.getInstance(this.version), getTFGvnAll(), indirection);
			}
	
			{
				ICharPredicate[] predsDot = {new DigitPredicate(), new IdentifierStartPredicate(), new CharPredicate('@')};
				ITokenFactory[] fsDot = {
						this.numlit,
						TFSeqRequired.getInstance(dot, name),
						TFSeqRequired.getInstance(dot, indirection)
				};				
				ITokenFactory fDot = ChoiceSupply.get('.', TFSyntaxError.getInstance(), predsDot, fsDot);
				ITokenFactory f0 = TFEmptyVerified.getInstance(',');
				ITokenFactory f1 = TFEmptyVerified.getInstance(')');
				ICharPredicate[] predsAll = {new CharPredicate('.'), new CharPredicate(','), new CharPredicate(')')};
				this.actual.setDefault(this.expr);
				this.actual.setChoices(predsAll, fDot, f0, f1);
			}			
		}
	}
	
	
	private static MTFSupply CACHE_SUPPLY;
	private static MTFSupply STD_95_SUPPLY;
		
	public static MTFSupply getInstance(MVersion version) {
		try {
			switch (version) {
				case CACHE: {
					if (CACHE_SUPPLY == null) {
						CACHE_SUPPLY = Parser.parse(CacheSupply.class, CacheSupply.ADAPTERS);
						CACHE_SUPPLY.initialize();
					}
					return CACHE_SUPPLY;
				}
				case ANSI_STD_95: {
					if (STD_95_SUPPLY == null) {
						STD_95_SUPPLY = Parser.parse(Std95Supply.class, Std95Supply.ADAPTERS);
						STD_95_SUPPLY.initialize();
					}
					return STD_95_SUPPLY;
				}
				default:
					return null;
			}
		} catch (Throwable t) {
			return null;
		}
	}	
}
