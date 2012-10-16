package com.raygroupintl.parsergen.rulebased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.charlib.PredicateFactory;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.ruledef.RuleDefinitionVisitor;
import com.raygroupintl.parsergen.ruledef.RuleSupply;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;
import com.raygroupintl.parsergen.ruledef.TCharSymbol;
import com.raygroupintl.parsergen.ruledef.TChoiceOfSymbols;
import com.raygroupintl.parsergen.ruledef.TConstSymbol;
import com.raygroupintl.parsergen.ruledef.TSymbol;
import com.raygroupintl.parsergen.ruledef.TSymbolList;
import com.raygroupintl.parsergen.ruledef.TSymbolSequence;

public class DefinitionVisitor implements RuleDefinitionVisitor {
	public Map<String, FactorySupplyRule> topRules  = new HashMap<String, FactorySupplyRule>();
	private java.util.ArrayList<FactorySupplyRule> allRules  = new ArrayList<FactorySupplyRule>();
		
	private FactorySupplyRule acceptAndReturn(RuleSupply rs, String name, RuleSupplyFlag flag) {
		rs.accept(this, name, flag);
		int index = this.allRules.size();
		return this.allRules.get(index-1);
	}
	
	@Override
	public void visitCharSymbol(TCharSymbol charSymbol, String name, RuleSupplyFlag flag) {
		PredicateFactory pf = new PredicateFactory();
		TCharSymbol.update(pf, charSymbol.get(0), (TokenStore) charSymbol.get(1));
		TokenStore list = (TokenStore) charSymbol.get(2);
		if (list != null) for (Token t : list) {
			TokenStore casted = (TokenStore) t;
			TCharSymbol.update(pf, casted.get(0), (TokenStore) casted.get(1));
		}		

		String key = charSymbol.toValue().toString();
		FSRChar result = new FSRChar(key, flag, pf.generate());
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	@Override
	public void visitConstSymbol(TConstSymbol constSymbol, String name, RuleSupplyFlag flag) {
		String value = constSymbol.get(1).toValue().toString();
		Token tIgnoreCase = constSymbol.get(4);
		boolean ignoreCase = (tIgnoreCase != null) && (tIgnoreCase.toValue().toString().equals("1"));
		FSRConst result = new FSRConst(value, ignoreCase, flag);
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}		
		this.allRules.add(result);
	}
	
	@Override
	public void visitSymbol(TSymbol symbol, String name, RuleSupplyFlag flag) {
		String value = symbol.toValue().toString();
		FactorySupplyRule result = null;
		if (flag == RuleSupplyFlag.TOP) {
			result = new FSRCopy(name, value);
		} else {
			result = new FSRSingle(name, value, flag);			
		}
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}		
		this.allRules.add(result);
	}
	
	public ListInfo getListInfo(TSymbolList symbolList, String name) {
		ListInfo result = new ListInfo();
		TokenStore listInfoSpec = (TokenStore) symbolList.get(2);
		if (listInfoSpec == null) {
			return result;
		}
		RuleSupply delimiter = (RuleSupply) listInfoSpec.get(1);
		result.delimiter = this.acceptAndReturn(delimiter, name + ".delimiter", RuleSupplyFlag.INNER_REQUIRED);
		if (result.delimiter == null) {
			return null;
		}
		TokenStore otherSpec = (TokenStore)listInfoSpec.get(2);
		if (otherSpec != null) {
			RuleSupply leftSpec = (RuleSupply) otherSpec.get(1);
			result.left = this.acceptAndReturn(leftSpec, name + ".left", RuleSupplyFlag.INNER_REQUIRED);
			RuleSupply rightSpec = (RuleSupply) otherSpec.get(3);
			result.right = this.acceptAndReturn(rightSpec, name + ".right", RuleSupplyFlag.INNER_REQUIRED);
			Token emptyAllowedSpec = otherSpec.get(5);
			result.emptyAllowed = (emptyAllowedSpec != null) && (emptyAllowedSpec.toValue().toString().equals("1"));
			Token noneAllowedSpec = otherSpec.get(7);
			result.noneAllowed = (noneAllowedSpec != null) && (noneAllowedSpec.toValue().toString().equals("1"));
			if ((result.left == null) || (result.right == null)) {
				return null;
			}
		}
		return result;
	}

	@Override
	public void visitSymbolList(TSymbolList symbolList, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
		RuleSupply ers = (RuleSupply) symbolList.get(1);
		FactorySupplyRule e = this.acceptAndReturn(ers, name + ".element", innerFlag);
		ListInfo listInfo = this.getListInfo(symbolList, name);
		if (listInfo == null) {
			return;
		}
		FactorySupplyRule result = e.formList(name, innerFlag, listInfo);

		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	@Override
	public void visitChoiceOfSymbols(TChoiceOfSymbols choiceOfSymbols, String name, RuleSupplyFlag flag) {		
		int index = 0;
		FSRChoice result = new FSRChoice(name, flag);
		for (Token t : choiceOfSymbols) {
			RuleSupply r  = (RuleSupply) t;
			FactorySupplyRule fsr = acceptAndReturn(r, name + "." + String.valueOf(index), flag.demoteInner()); 
			result.add(fsr);
			++index;
		}
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	@Override
	public void visitSymbolSequence(TSymbolSequence sequence, String name, RuleSupplyFlag flag) {
		FSRSequence result = new FSRSequence(name, flag);
		int index = 0;
		for (Token t : sequence) {
			RuleSupply rs = (RuleSupply) t;
			FactorySupplyRule fsr = acceptAndReturn(rs, name + "." + String.valueOf(index), RuleSupplyFlag.INNER_REQUIRED); 
			result.add(fsr);
			++index;
		}
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	public FactorySupplyRule getLastRule() {
		int length = this.allRules.size();
		if (this.allRules.size() == 0) {
			return null;
		} else {
			return this.allRules.get(length - 1);
		}
	}
}
