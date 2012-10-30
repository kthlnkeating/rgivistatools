package com.raygroupintl.parsergen.rulebased;

import java.util.ArrayList;
import java.util.Collection;
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
		
		public void addToContainer(RuleSupplyFlag flag, FactorySupplyRule<T> fsr) {
			this.container.set(this.index, flag, fsr);
		}
	}
	
	//private static class ContainerIndexAndFlag<T extends Token> {
	//	private ContainerAndIndex<T> containerNIndex;
	//	private RuleSupplyFlag flag;
	//	
	//	public ContainerIndexAndFlag(ContainerAndIndex<T> containerNIndex, RuleSupplyFlag flag) {
	//		this.containerNIndex = containerNIndex;
	//		this.flag = flag;
	//	}
	//	
	//	public void addToContainer(FactorySupplyRule<T> fsr) {
	//		this.containerNIndex.addToContainer(this.flag, fsr);
	//	}		
	//}
	
	
	public Map<String, FactorySupplyRule<T>> topRules = new HashMap<String, FactorySupplyRule<T>>();
	private ContainerAndIndex<T> lastContainer;
	
	//private Map<String, List<ContainerIndexAndFlag<T>>> missing = new HashMap<String, List<ContainerIndexAndFlag<T>>>();
	
	public void addTopRule(String name, FactorySupplyRule<T> fsr) {
		this.topRules.put(name, fsr);
		//List<ContainerIndexAndFlag<T>> missingForName = missing.get(name);
		//if (missingForName != null) {
		//	for (ContainerIndexAndFlag<T> c : missingForName) {
		//		c.addToContainer(fsr);
		//	}
		//	missingForName.remove(name);
		//}
	}
	
	public FactorySupplyRule<T> getTopRule(String name) {
		return this.topRules.get(name);
	}
	
	public Collection<FactorySupplyRule<T>> getTopRules() {
		return new ArrayList<FactorySupplyRule<T>>(this.topRules.values());
	}
	
	@Override
	public void visitCharSymbol(CharSymbol charSymbol, String name, RuleSupplyFlag flag) {
		String key = charSymbol.getKey();
		FactorySupplyRule<T> result = this.topRules.get(key);
		if (result == null) {
			Predicate p = charSymbol.getPredicate();
			result = new FSRChar<T>(key, p);
			this.addTopRule(key, result);
		}
		if (flag == RuleSupplyFlag.TOP) {
			if (! this.topRules.containsKey(name)) {
				this.addTopRule(name, result);
			}
		}		
		if (this.lastContainer != null) {
			this.lastContainer.addToContainer(flag, result);
		}
	}
	
	@Override
	public void visitConstSymbol(ConstSymbol constSymbol, String name, RuleSupplyFlag flag) {
		String value = constSymbol.getValue();
		FactorySupplyRule<T> result = this.topRules.get(value);
		if (result == null) {
			boolean ignoreCase = constSymbol.getIgnoreCaseFlag();
			result = new FSRConst<T>(value, ignoreCase);
			this.addTopRule(value, result);
		}
		if (flag == RuleSupplyFlag.TOP) {
			if (! this.topRules.containsKey(name)) {
				this.addTopRule(name, result);
			}
		}		
		if (this.lastContainer != null) {
			this.lastContainer.addToContainer(flag, result);
		}
	}
	
	@Override
	public void visitSymbol(Symbol symbol, String name, RuleSupplyFlag flag) {
		String value = symbol.getValue();
		if (flag == RuleSupplyFlag.TOP) {
			FSRCopy<T> result = new FSRCopy<T>(name, value);
			this.addTopRule(name, result);
		} else {
			//FactorySupplyRule<T> topRule = this.topRules.get(value);
			//if (topRule != null) {
			//	this.lastContainer.addToContainer(flag, topRule);
			//} else {
			//	List<ContainerIndexAndFlag<T>> missingForName = missing.get(value);
			//	if (missingForName == null) {
			//		missingForName = new ArrayList<ContainerIndexAndFlag<T>>();
			//		this.missing.put(value, missingForName);
			//	}
			//	missingForName.add(new ContainerIndexAndFlag<T>(this.lastContainer, flag));
			//}			
			FSRSingle<T> result = new FSRSingle<T>(name, value);			
			this.lastContainer.addToContainer(flag, result);
		}
	}
	
	@Override
	public void visitCharSymbolList(CharSymbol charSymbol, String name, RuleSupplyFlag flag) {	
		String key = "{" + charSymbol.getKey() + "}";
		FactorySupplyRule<T> result = this.topRules.get(key);
		if (result == null) {
			Predicate p = charSymbol.getPredicate();
			result = new FSRString<T>(key, flag, p);
			this.addTopRule(name, result);
		}
		if (flag == RuleSupplyFlag.TOP) {
			if (! this.topRules.containsKey(name)) {
				this.addTopRule(name, result);
			}
		}
		if (this.lastContainer != null) {
			this.lastContainer.addToContainer(flag, result);
		}
	}
	
	@Override
	public void visitSymbolList(RuleSupply ruleSupply, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
		FSRList<T> result = new FSRList<T>(name);
		ContainerAndIndex<T> previousContPair = this.lastContainer;
		this.lastContainer = new ContainerAndIndex<T>(result, 0);
		ruleSupply.accept(this, name + ".element", innerFlag);
		if (flag == RuleSupplyFlag.TOP) {
			this.addTopRule(name, result);
		}
		if (previousContPair != null) {
			previousContPair.addToContainer(flag, result);
		}
		this.lastContainer = previousContPair;
	}
	
	@Override
	public void visitDelimitedSymbolList(RuleSupply element, RuleSupply delimiter, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
		FSRDelimitedList<T> result = new FSRDelimitedList<T>(name);
		ContainerAndIndex<T> previousContPair = this.lastContainer;
		this.lastContainer = new ContainerAndIndex<T>(result, 0);
		element.accept(this, name + ".element", innerFlag);
		this.lastContainer = new ContainerAndIndex<T>(result, 1);
		delimiter.accept(this, name + ".delimiter", RuleSupplyFlag.INNER_REQUIRED);
		if (flag == RuleSupplyFlag.TOP) {
			this.addTopRule(name, result);
		}
		if (previousContPair != null) {
			previousContPair.addToContainer(flag, result);
		}
		this.lastContainer = previousContPair;
	}
	
	@Override
	public void visitEnclosedDelimitedSymbolList(SymbolList symbolList, String name, RuleSupplyFlag flag) {		
		RuleSupplyFlag innerFlag = flag.demoteInner();
 	    FSREnclosedDelimitedList<T> result = new FSREnclosedDelimitedList<T>(name);
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
			this.addTopRule(name, result);
		}
		if (previousContPair != null) {
			previousContPair.addToContainer(flag, result);
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
			this.addTopRule(name, fsrCollection);
		}
		if (previousContPair != null) {
			previousContPair.addToContainer(flag, fsrCollection);
		}
		this.lastContainer = previousContPair;
	}
	
	@Override
	public void visitChoiceOfSymbols(RuleSupplies choiceOfSymbols, String name, RuleSupplyFlag flag) {		
		FSRChoice<T> result = new FSRChoice<T>(name, choiceOfSymbols.getSize());
		this.visitCollection(result, choiceOfSymbols, name, flag);
	}
	
	@Override
	public void visitSymbolSequence(RuleSupplies sequence, String name, RuleSupplyFlag flag) {
		FSRSequence<T> result = new FSRSequence<T>(name, sequence.getSize());
		this.visitCollection(result, sequence, name, flag);
	}
}
