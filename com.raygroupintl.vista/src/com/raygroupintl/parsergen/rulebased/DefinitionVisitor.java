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
	
	//private Pair<FSRContainer<T>, Integer> lastContainer;
		
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
		FactorySupplyRule<T> result = this.topRules.get(name);
		if (result == null) {
			Predicate p = charSymbol.getPredicate();
			String key = charSymbol.getKey();
			result = new FSRChar<T>(key, flag, p);
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	@Override
	public void visitConstSymbol(ConstSymbol constSymbol, String name, RuleSupplyFlag flag) {
		FactorySupplyRule<T> result = this.topRules.get(name);
		if (result == null) {
			String value = constSymbol.getValue();
			boolean ignoreCase = constSymbol.getIgnoreCaseFlag();
			result = new FSRConst<T>(value, ignoreCase, flag);
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
	
	@Override
	public void visitCharSymbolList(CharSymbol charSymbol, String name, RuleSupplyFlag flag) {		
		FactorySupplyRule<T> result = new FSRString<T>("{" + charSymbol.getKey() + "}", flag, charSymbol.getPredicate());
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	@Override
	public void visitSymbolList(RuleSupply ruleSupply, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
		FactorySupplyRule<T> e = this.acceptAndReturn(ruleSupply, name + ".element", innerFlag);
		FSRList<T> result = new FSRList<T>(name, innerFlag);
		result.set(0, e);
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	@Override
	public void visitDelimitedSymbolList(RuleSupply element, RuleSupply delimiter, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
		FactorySupplyRule<T> e = this.acceptAndReturn(element, name + ".element", innerFlag);
		FactorySupplyRule<T> d = this.acceptAndReturn(delimiter, name + ".delimiter", RuleSupplyFlag.INNER_REQUIRED);
		FSRDelimitedList<T> result = new FSRDelimitedList<T>(name, innerFlag);
		result.set(0, e);
		result.set(1, d);
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	@Override
	public void visitEnclosedDelimitedSymbolList(SymbolList symbolList, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
		FactorySupplyRule<T> e = this.acceptAndReturn(symbolList.getElement(), name + ".element", innerFlag);
		FactorySupplyRule<T> d = this.acceptAndReturn(symbolList.getDelimiter(), name + ".delimiter", RuleSupplyFlag.INNER_REQUIRED);
		FactorySupplyRule<T> l = this.acceptAndReturn(symbolList.getLeftParanthesis(), name + ".left", RuleSupplyFlag.INNER_REQUIRED);
		FactorySupplyRule<T> r = this.acceptAndReturn(symbolList.getRightParanthesis(), name + ".right", RuleSupplyFlag.INNER_REQUIRED);
		FSREnclosedDelimitedList<T> result = new FSREnclosedDelimitedList<T>(name, innerFlag);
		result.set(0, e);
		result.set(1, d);
		result.set(2, l);
		result.set(3, r);
		result.setEmptyAllowed(symbolList.isEmptyAllowed());
		result.setNoneAllowed(symbolList.isNoneAllowed());
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		this.allRules.add(result);
	}
	
	private void visitCollection(FSRCollection<T> fsrCollection, RuleSupplies rss, String name, RuleSupplyFlag flag) {		
		int size = rss.getSize();
		for (int index = 0; index<size; ++index) {
			FactorySupplyRule<T> fsr = acceptAndReturn(rss, index, name + "." + String.valueOf(index), RuleSupplyFlag.INNER_REQUIRED); 
			fsrCollection.set(index, fsr);
		}
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, fsrCollection);
		}
		this.allRules.add(fsrCollection);
	}
	
	@Override
	public void visitChoiceOfSymbols(RuleSupplies choiceOfSymbols, String name, RuleSupplyFlag flag) {		
		FSRChoice<T> result = new FSRChoice<T>(name, choiceOfSymbols.getSize(), flag);
		this.visitCollection(result, choiceOfSymbols, name, flag);
	}
	
	@Override
	public void visitSymbolSequence(RuleSupplies sequence, String name, RuleSupplyFlag flag) {
		FSRSequence<T> result = new FSRSequence<T>(name, sequence.getSize(), flag);
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
