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
import com.raygroupintl.bnf.annotation.ChoiceCh0;
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

	public abstract ITokenFactory getTFLvn();

	public abstract ITokenFactory getTFExprAtom();	
	
	public abstract ITokenFactory getTFExpr();	
	
	public abstract ITokenFactory getTFActual();

	public abstract ITokenFactory getTFIndirection();

	public abstract ITokenFactory getTFGvn();
	
	protected static abstract class CommonSupply extends MTFSupply {
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
			//ADAPTERS.put("gvn", new TokenAdapter() {
			//	@Override
			//	public IToken convert(IToken[] tokens) {
			//		return new TGlobalNamed((TArray) tokens[1]);
			//	}
			//});
			ADAPTERS.put("gvnnaked", new TokenAdapter() {
				@Override
				public IToken convert(IToken[] tokens) {
					return new TGlobalNaked(tokens[1]);
				}
			});			 
		}

		public ITokenFactory dot = TFChar.DOT;
		public ITokenFactory comma = TFChar.COMMA;
		public ITokenFactory pipe = new TFConstChar('|');
		public ITokenFactory lsqr = new TFConstChar('[');
		public ITokenFactory rsqr = new TFConstChar(']');
		public ITokenFactory lpar = new TFConstChar('(');
		public ITokenFactory rpar = new TFConstChar(')');
		public ITokenFactory qmark = new TFConstChar('?');
		public ITokenFactory at = new TFConstChar('@');
		public ITokenFactory caret = new TFConstChar('^');

		public ITokenFactory nqmark = new TFConstString("'?");
		public ITokenFactory atlpar = new TFConstString("@(");
		public ITokenFactory caretquest = new TFConstString("^$");
		public ITokenFactory ddollar = new TFConstString("$$");

		public ITokenFactory name = TFName.getInstance();
		public ITokenFactory ident = TFIdent.getInstance();
		public ITokenFactory numlit = TFNumLit.getInstance();
		public ITokenFactory operator = TFOperator.getInstance();
		public ITokenFactory error = TFSyntaxError.getInstance();
		public ITokenFactory unaryop = new TFConstChars("+-\'");
		public ITokenFactory strlit = new TFStringLiteral();
		
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
		
		@List(value="expr", delim="comma", left="lpar", right="rpar")
		public ITokenFactory exprlistinparan;

		@Sequence(value={"at", "expratom"}, required="all")
		public ITokenFactory indirection_0;
		@Sequence(value={"atlpar", "exprlist", "rpar"}, required="all")
		public ITokenFactory indirection_1;
		@Sequence(value={"indirection_0", "indirection_1"}, required="ro")
		public ITokenFactory indirection;
		
		@ChoiceCh0(value={"lvn", "gvnAll", "indirection"}, preds={"idstart", "^", "@"})
		public ITokenFactory glvn;
		
		@Sequence(value={"environment", "name", "exprlistinparan"}, required="oro")
		public ITokenFactory gvn_0;
		@Sequence(value={"caret", "gvn_0"}, required="all")
		public ITokenFactory gvn;
		
		@Sequence(value={"caretquest", "ident", "exprlistinparan"}, required="all")
		public ITokenFactory gvnssvn;

		@Sequence(value={"ddollar", "doargument"}, required="all")
		public ITokenFactory extrinsic;
		
		@Sequence(value={"unaryop", "expratom"}, required="all")
		public ITokenFactory unaryexpritem;
				
		@Sequence(value={"caret", "exprlistinparan"}, required="all")
		public ITokenFactory gvnnaked;
				
		public ITokenFactory getTFIndirection() {
			return this.indirection;
		}
	
		public ITokenFactory getTFGvn() {
			return this.gvn;
		}
	}
		
	public static class Std95Supply extends CommonSupply {	
		private MVersion version = MVersion.ANSI_STD_95;

		@Choice({"glvn", "exprItem"})
		public ITokenFactory expratom;

		public TFChoiceOnChar0th actual = new TFChoiceOnChar0th();
		public TFChoiceOnChar0th exprItem = new TFChoiceOnChar0th();
		public TFChoiceOnChar1st gvnAll = new TFChoiceOnChar1st();
		
		@Sequence(value={"name", "actuallist"}, required="ro")
		public ITokenFactory lvn;
		
		public ITokenFactory doargument = TFDoArgument.getInstance(this.version, true);
		public ITokenFactory external = new TFExternal(this.version);
		//public ITokenFactory intrinsic = new TFIntrinsic(this.version);
		
		@Sequence(value={"expratom", "exprtail"}, required="ro")
		public ITokenFactory expr;
		
		public ITokenFactory actuallist = TFActualList.getInstance(this.version);
		
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
	
		public ITokenFactory getTFLvn() {
			return this.lvn;
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
					extrinsic, external, TFIntrinsic.getInstance(this.version));
	
				ICharPredicate[] preds = {
					new CharPredicate('"'), new CharPredicate('$'),
					new CharsPredicate('\'', '+', '-'), new CharPredicate('.'), new CharPredicate('('),
					new DigitPredicate()};
				this.exprItem.setChoices(preds, strlit, fDollar, 
						unaryexpritem, this.numlit,
							TFInParantheses.getInstance(this.expr), this.numlit);
			}
		
			{
				ICharPredicate[] preds = {new CharPredicate('$'), new CharPredicate('('), new CharsPredicate('%', '|', '['), new LetterPredicate()};
				this.gvnAll.setLeadingChar('^');
				this.gvnAll.setDefault(error);
				this.gvnAll.setChoices( preds, gvnssvn, gvnnaked, gvn, gvn);
			}
			
			{
				ICharPredicate[] predsDot = {new DigitPredicate(), new IdentifierStartPredicate(), new CharPredicate('@')};
				ITokenFactory[] fsDot = {
						this.numlit,
						TFSeqRequired.getInstance(dot, name),
						TFSeqRequired.getInstance(dot, indirection)
				};				
				ITokenFactory fDot = ChoiceSupply.get('.', error, predsDot, fsDot);
				ITokenFactory f0 = TFEmptyVerified.getInstance(',');
				ITokenFactory f1 = TFEmptyVerified.getInstance(')');
				ICharPredicate[] predsAll = {new CharPredicate('.'), new CharPredicate(','), new CharPredicate(')')};
				this.actual.setDefault(this.expr);
				this.actual.setChoices(predsAll, fDot, f0, f1);
			}
		}
	}

	public static class CacheSupply extends CommonSupply {
		private MVersion version = MVersion.CACHE;

		@Choice({"glvn", "exprItem", "classmethod"})
		public ITokenFactory expratom;
		
		public TFChoiceOnChar0th actual = new TFChoiceOnChar0th();
		public TFChoiceOnChar0th exprItem = new TFChoiceOnChar0th();
		public TFChoiceOnChar1st gvnAll = new TFChoiceOnChar1st();
		
		@Sequence(value={"dot", "name"}, required="all")
		public ITokenFactory lvn_a;
		@List("lvn_a")
		public ITokenFactory lvn_l;
		@Sequence(value={"name", "lvn_l"}, required="ro")
		public ITokenFactory lvn_x;
		@Sequence(value={"lvn_x", "actuallist"}, required="ro")
		public ITokenFactory lvn;
		
		public ITokenFactory doargument = TFDoArgument.getInstance(this.version, true);
		public ITokenFactory external = new TFExternal(this.version);
		//public ITokenFactory intrinsic = TFIntrinsic.getInstance(this.version);
			
		@Choice({"expratom", "classmethod"})
		public ITokenFactory expr_0;
		@Sequence(value={"expr_0", "exprtail"}, required="ro")
		public ITokenFactory expr;
		
		public ITokenFactory pattern = TFPattern.getInstance(this.version);
		public ITokenFactory classmethod = TFCacheClassMethod.getInstance();
		public ITokenFactory actuallist = TFActualList.getInstance(this.version);
		
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
	
		public ITokenFactory getTFLvn() {
			return this.lvn;
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
					extrinsic, external, TFIntrinsic.getInstance(this.version));
	
				ICharPredicate[] preds = {
					new CharPredicate('"'), new CharPredicate('$'),
					new CharsPredicate('\'', '+', '-'), new CharPredicate('.'), new CharPredicate('('),
					new DigitPredicate()};
				this.exprItem.setChoices(preds, strlit, fDollar, 
						unaryexpritem, this.numlit,
							TFInParantheses.getInstance(this.expr), this.numlit);
			}
		
			{
				ICharPredicate[] preds = {new CharPredicate('$'), new CharPredicate('('), new CharsPredicate('%', '|', '['), new LetterPredicate()};
				this.gvnAll.setLeadingChar('^');
				this.gvnAll.setDefault(error);
				this.gvnAll.setChoices( preds, gvnssvn, gvnnaked, gvn, gvn);
			}
			
			{
				ICharPredicate[] predsDot = {new DigitPredicate(), new IdentifierStartPredicate(), new CharPredicate('@')};
				ITokenFactory[] fsDot = {
						this.numlit,
						TFSeqRequired.getInstance(dot, name),
						TFSeqRequired.getInstance(dot, indirection)
				};				
				ITokenFactory fDot = ChoiceSupply.get('.', error, predsDot, fsDot);
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
