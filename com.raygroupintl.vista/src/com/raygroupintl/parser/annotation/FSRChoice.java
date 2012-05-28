package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFForkableChoice;
import com.raygroupintl.parser.TokenFactory;

public class FSRChoice extends FSRBase {
	private List<FactorySupplyRule> list = new ArrayList<FactorySupplyRule>(); 
	private TFForkableChoice factory;
	
	
/*	private Map<String, Integer> choiceOrder = new HashMap<String, Integer>();
	private Map<Integer, List<String>> possibleShared = new HashMap<Integer, List<String>>();
	private Set<String> restrictedChoices = new HashSet<String>();
	private Map<Integer, String> leadingShared = new HashMap<Integer, String>();
	
	private void updateChoicePossibilities(FactorySupplyRule f, TokenFactoriesByName symbols, int index) {
		FactorySupplyRule previous = null;
		List<String> allForIndex = new ArrayList<String>();
		while (f != previous) {
			String name = f.getName();
			if (! restrictedChoices.contains(name)) {
				if (symbols.get(name) != null) {
					this.choiceOrder.put(name, index);
					allForIndex.add(name);
				}
			}
			previous = f;
			f = f.getLeadingFactory();
		}
		this.possibleShared.put(index, allForIndex);
	}
	
	private Integer findInChoices(FactorySupplyRule f, TokenFactoriesByName symbols) {
		FactorySupplyRule previous = null;
		while (f != previous) {
			String name = f.getName();
			Integer order = this.choiceOrder.get(name);
			if (order != null) {
				if (this.possibleShared.containsKey(order)) {
					for (String r : this.possibleShared.get(order)) {
						if (! r.equals(name)) {
							this.choiceOrder.remove(r);
							this.restrictedChoices.add(r);
						}
					}
					this.leadingShared.put(order, name);
					this.possibleShared.remove(order);
				}
				return order;
			}
			previous = f;
			f = f.getLeadingFactory();
		}
		return null;
	}
	
	public void reset() {
		this.factories = new ArrayList<TokenFactory>();
		this.choiceOrder = new HashMap<String, Integer>();
		this.possibleShared = new HashMap<Integer, List<String>>();
		this.restrictedChoices = new HashSet<String>();
		this.leadingShared = new HashMap<Integer,TokenFactory>();		
	}
	
	public void add(TokenFactory tf, TokenFactoriesByName symbols) {
		Integer existing = this.findInChoices(tf, symbols);
		if (existing == null) {
			int n = this.factories.size();
			this.factories.add(tf);
			this.updateChoicePossibilities(tf, symbols, n);
		} else {
			int n = existing.intValue();
			TokenFactory current = this.factories.get(n);
			if (current instanceof TFForkedSequence) {
				((TFForkedSequence) current).addFollower(tf);
			} else {
				TokenFactory leading = this.leadingShared.get(n);
				String name = leading.getName();			
				TFForkedSequence newForked = new TFForkedSequence(this.getName() + "." + name, leading);
				newForked.addFollower(current);
				newForked.addFollower(tf);
				this.factories.set(n, newForked);
			}
		}
	}*/
	
	
	
	
	
	
	
	
	
	public FSRChoice(String name, RuleSupplyFlag flag) {
		super(flag);
		this.factory = new TFForkableChoice(name);
	}
	
	public void add(FactorySupplyRule r) {
		this.list.add(r);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public FactorySupplyRule getLeadingRule() {
		return this;
	}
	
	@Override
	public TFForkableChoice getFactory(RulesByName symbols) {
		this.factory.reset();

		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
			
		for (FactorySupplyRule r : this.list) {
			TokenFactory f = r.getFactory(localSymbols);
			if (f == null) {
				return null;
			}
			this.factory.add(f, symbols);
		}
		
		return this.factory;
	}

	@Override
	public TFBasic getShellFactory() {
		return this.factory;	
	}
}
