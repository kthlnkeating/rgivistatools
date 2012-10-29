package com.raygroupintl.parsergen.rulebased;

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
	private static class ContainerAndIndex<T extends Token> {
		public FSRContainer<T> container;
		public int index;
		
		public ContainerAndIndex(FSRContainer<T> container, int index) {
			this.container = container;
			this.index = index;
		}
		
		public void addToContainer(FactorySupplyRule<T> fsr) {
			this.container.set(this.index, fsr);
		}
	}
	
	public Map<String, FactorySupplyRule<T>> topRules  = new HashMap<String, FactorySupplyRule<T>>();
	private ContainerAndIndex<T> lastContainer;
	
	@Override
	public void visitCharSymbol(CharSymbol charSymbol, String name, RuleSupplyFlag flag) {
		FactorySupplyRule<T> result = this.topRules.get(name);
		if (result == null) {
			Predicate p = charSymbol.getPredicate();
			String key = charSymbol.getKey();
			result = new FSRChar<T>(key, flag, p);
			this.topRules.put(name, result);
		}
		if (this.lastContainer != null) {
			this.lastContainer.addToContainer(result);
		}
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
		if (this.lastContainer != null) {
			this.lastContainer.addToContainer(result);
		}
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
		if (this.lastContainer != null) {
			this.lastContainer.addToContainer(result);
		}
	}
	
	@Override
	public void visitCharSymbolList(CharSymbol charSymbol, String name, RuleSupplyFlag flag) {		
		FactorySupplyRule<T> result = new FSRString<T>("{" + charSymbol.getKey() + "}", flag, charSymbol.getPredicate());
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		if (this.lastContainer != null) {
			this.lastContainer.addToContainer(result);
		}
	}
	
	@Override
	public void visitSymbolList(RuleSupply ruleSupply, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
		FSRList<T> result = new FSRList<T>(name, innerFlag);
		ContainerAndIndex<T> previousContPair = this.lastContainer;
		this.lastContainer = new ContainerAndIndex<T>(result, 0);
		ruleSupply.accept(this, name + ".element", innerFlag);
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		if (previousContPair != null) {
			previousContPair.addToContainer(result);
		}
		this.lastContainer = previousContPair;
	}
	
	@Override
	public void visitDelimitedSymbolList(RuleSupply element, RuleSupply delimiter, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
		FSRDelimitedList<T> result = new FSRDelimitedList<T>(name, innerFlag);
		ContainerAndIndex<T> previousContPair = this.lastContainer;
		this.lastContainer = new ContainerAndIndex<T>(result, 0);
		element.accept(this, name + ".element", innerFlag);
		this.lastContainer = new ContainerAndIndex<T>(result, 1);
		delimiter.accept(this, name + ".delimiter", RuleSupplyFlag.INNER_REQUIRED);
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		if (previousContPair != null) {
			previousContPair.addToContainer(result);
		}
		this.lastContainer = previousContPair;
	}
	
	@Override
	public void visitEnclosedDelimitedSymbolList(SymbolList symbolList, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
 	    FSREnclosedDelimitedList<T> result = new FSREnclosedDelimitedList<T>(name, innerFlag);
 	    ContainerAndIndex<T> previousContPair = this.lastContainer;
		this.lastContainer = new ContainerAndIndex<T>(result, 0);
		symbolList.getElement().accept(this, name + ".element", innerFlag);
		this.lastContainer = new ContainerAndIndex<T>(result, 1);
		symbolList.getDelimiter().accept(this, name + ".delimiter", RuleSupplyFlag.INNER_REQUIRED);
		this.lastContainer = new ContainerAndIndex<T>(result, 2);
		symbolList.getLeftParanthesis().accept(this, name + ".left", RuleSupplyFlag.INNER_REQUIRED);
		this.lastContainer = new ContainerAndIndex<T>(result, 3);
		symbolList.getRightParanthesis().accept(this, name + ".right", RuleSupplyFlag.INNER_REQUIRED);
		result.setEmptyAllowed(symbolList.isEmptyAllowed());
		result.setNoneAllowed(symbolList.isNoneAllowed());
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, result);
		}
		if (previousContPair != null) {
			previousContPair.addToContainer(result);
		}
		this.lastContainer = previousContPair;
	}
	
	private void visitCollection(FSRCollection<T> fsrCollection, RuleSupplies rss, String name, RuleSupplyFlag flag) {
		ContainerAndIndex<T> previousContPair = this.lastContainer;
		int size = rss.getSize();
		for (int index = 0; index<size; ++index) {
			this.lastContainer = new ContainerAndIndex<T>(fsrCollection, index);
			rss.acceptElement(this, index, name + "." + String.valueOf(index), RuleSupplyFlag.INNER_REQUIRED);
		}
		if (flag == RuleSupplyFlag.TOP) {
			this.topRules.put(name, fsrCollection);
		}
		if (previousContPair != null) {
			previousContPair.addToContainer(fsrCollection);
		}
		this.lastContainer = previousContPair;
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
}
