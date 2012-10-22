package com.raygroupintl.parsergen.rulebased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parsergen.ruledef.CharSymbol;
import com.raygroupintl.parsergen.ruledef.ConstSymbol;
import com.raygroupintl.parsergen.ruledef.RuleDefinitionVisitor;
import com.raygroupintl.parsergen.ruledef.RuleSupplies;
import com.raygroupintl.parsergen.ruledef.RuleSupply;
import com.raygroupintl.parsergen.ruledef.RuleSupplyFlag;
import com.raygroupintl.parsergen.ruledef.Symbol;
import com.raygroupintl.parsergen.ruledef.SymbolList;

public class DefinitionVisitor<T extends Token> implements RuleDefinitionVisitor {
	public Map<String, FactorySupplyRule<T>> topRules  = new HashMap<String, FactorySupplyRule<T>>();
	private java.util.ArrayList<FactorySupplyRule<T>> allRules  = new ArrayList<FactorySupplyRule<T>>();
		
	private FactorySupplyRule<T> acceptAndReturn(RuleSupply rs, String name, RuleSupplyFlag flag) {
		rs.accept(this, name, flag);
		int index = this.allRules.size();
		return this.allRules.get(index-1);
	}
	
	private FactorySupplyRule<T> acceptAndReturn(RuleSupplies rss, int index, String name, RuleSupplyFlag flag) {
		rss.acceptElement(this, index, name, flag);
		int rIndex = this.allRules.size();
		return this.allRules.get(rIndex-1);
	}
	
	@Override
	public void visitCharSymbol(CharSymbol charSymbol, String name, RuleSupplyFlag flag) {
		Predicate p = charSymbol.getPredicate();
		String key = charSymbol.getKey();
		FSRChar<T> result = new FSRChar<T>(key, flag, p);
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	@Override
	public void visitConstSymbol(ConstSymbol constSymbol, String name, RuleSupplyFlag flag) {
		String value = constSymbol.getValue();
		boolean ignoreCase = constSymbol.getIgnoreCaseFlag();
		FSRConst<T> result = new FSRConst<T>(value, ignoreCase, flag);
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}		
		this.allRules.add(result);
	}
	
	@Override
	public void visitSymbol(Symbol symbol, String name, RuleSupplyFlag flag) {
		String value = symbol.getValue();
		FactorySupplyRule<T> result = null;
		if (flag == RuleSupplyFlag.TOP) {
			result = new FSRCopy<T>(name, value);
		} else {
			result = new FSRSingle<T>(name, value, flag);			
		}
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}		
		this.allRules.add(result);
	}
	
	public ListInfo<T> getListInfo(SymbolList symbolList, String name) {
		ListInfo<T> result = new ListInfo<T>();
		RuleSupply delimiter = symbolList.getDelimiter();
		if (delimiter != null) {		
			result.delimiter = this.acceptAndReturn(delimiter, name + ".delimiter", RuleSupplyFlag.INNER_REQUIRED);
			RuleSupply leftSpec = symbolList.getLeftParanthesis();
			RuleSupply rightSpec = symbolList.getRightParanthesis();
			if ((leftSpec != null) || (rightSpec != null)) { 
				result.left = this.acceptAndReturn(leftSpec, name + ".left", RuleSupplyFlag.INNER_REQUIRED);
				result.right = this.acceptAndReturn(rightSpec, name + ".right", RuleSupplyFlag.INNER_REQUIRED);
				result.emptyAllowed = symbolList.isEmptyAllowed();
				result.noneAllowed = symbolList.isNoneAllowed();
				// throw if only one is set
			}
		}
		return result;
	}

	@Override
	public void visitSymbolList(SymbolList symbolList, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
		RuleSupply ers = symbolList.getElement();
		FactorySupplyRule<T> e = this.acceptAndReturn(ers, name + ".element", innerFlag);
		ListInfo<T> listInfo = this.getListInfo(symbolList, name);
		if (listInfo == null) {
			return;
		}
		FactorySupplyRule<T> result = e.formList(name, innerFlag, listInfo);

		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	private void visitCollection(FSRCollection<T> fsrCollection, RuleSupplies rss, String name, RuleSupplyFlag flag) {
		int size = rss.getSize();
		for (int index = 0; index<size; ++index) {
			FactorySupplyRule<T> fsr = acceptAndReturn(rss, index, name + "." + String.valueOf(index), RuleSupplyFlag.INNER_REQUIRED); 
			fsrCollection.add(fsr);
		}
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, fsrCollection);
		}
		this.allRules.add(fsrCollection);
	}
	
	@Override
	public void visitChoiceOfSymbols(RuleSupplies choiceOfSymbols, String name, RuleSupplyFlag flag) {		
		FSRChoice<T> result = new FSRChoice<T>(name, flag);
		this.visitCollection(result, choiceOfSymbols, name, flag);
	}
	
	@Override
	public void visitSymbolSequence(RuleSupplies sequence, String name, RuleSupplyFlag flag) {
		FSRSequence<T> result = new FSRSequence<T>(name, flag);
		this.visitCollection(result, sequence, name, flag);
	}
	
	public FactorySupplyRule<T> getLastRule() {
		int length = this.allRules.size();
		if (this.allRules.size() == 0) {
			return null;
		} else {
			return this.allRules.get(length - 1);
		}
	}
}
