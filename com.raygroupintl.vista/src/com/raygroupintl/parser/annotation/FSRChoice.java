package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.parser.TFForkableChoice;
import com.raygroupintl.parser.TFForkedSequence;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TokenFactory;

public class FSRChoice extends FSRBase {
	private static class ForkAlgorithm {	
		
		public ForkAlgorithm(String name) {
			this.aname = name;
		}
	 	
		public String aname;
		
		public List<FactorySupplyRule> list = new ArrayList<FactorySupplyRule>();
		
		public Map<String, Integer> choiceOrder = new HashMap<String, Integer>();
		public Map<Integer, List<String>> possibleShared = new HashMap<Integer, List<String>>();
		public Set<String> restrictedChoices = new HashSet<String>();
		public Map<Integer, String> leadingShared = new HashMap<Integer, String>();
		
		public void updateChoicePossibilities(FactorySupplyRule f, RulesByName symbols, int index) {
			FactorySupplyRule previous = null;
			List<String> allForIndex = new ArrayList<String>();
			while (f != previous) {
				String name = f.getName();
				if (! restrictedChoices.contains(name)) {
					if (symbols.hasRule(name)) {
						this.choiceOrder.put(name, index);
						allForIndex.add(name);
					}
				}
				previous = f;
				f = f.getLeading(symbols);
			}
			this.possibleShared.put(index, allForIndex);
		}
		
		public Integer findInChoices(FactorySupplyRule f, RulesByName names) {
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
				f = f.getLeading(names);
			}
			return null;
		}
		
		public void add(FactorySupplyRule tf, RulesByName symbols) {
			Integer existing = this.findInChoices(tf, symbols);
			if (existing == null) {
				int n = this.list.size();
				this.list.add(tf);
				this.updateChoicePossibilities(tf, symbols, n);
			} else {
				int n = existing.intValue();
				FactorySupplyRule current = this.list.get(n);
				if (current instanceof FSRForkedSequence) {
					((FSRForkedSequence) current).addFollower(tf);
				} else {
					String name = this.leadingShared.get(n);
					FactorySupplyRule leading = symbols.get(name);
					FSRForkedSequence newForked = new FSRForkedSequence(this.aname + "." + name, leading);
					newForked.addFollower(current);
					newForked.addFollower( tf);
					this.list.set(n, newForked);
				}
			}
		}	
	}

	private List<FactorySupplyRule> list = new ArrayList<FactorySupplyRule>(); 
	private TFForkableChoice factory;
	
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
	
	private List<TokenFactory> getChoiceFactories(RulesByName symbols) {
		List<TokenFactory> result = new ArrayList<TokenFactory>();
		
		ForkAlgorithm algorithm = new ForkAlgorithm(this.getName());
		for (FactorySupplyRule r : this.list) {
			FactorySupplyRule ar = r.getActualRule(symbols);
			algorithm.add(ar, symbols);
		}
		int n = algorithm.list.size();
		for (int i=0; i<n; ++i) {
			FactorySupplyRule on =algorithm.list.get(i);
			if (on instanceof FSRForkedSequence) {
				FSRForkedSequence onf = (FSRForkedSequence) on;
				int m = onf.followers.size();
				List<TFSequence> followerFactories = new ArrayList<TFSequence>(m);
				for (int j=0; j<m; ++j) {
					FactorySupplyRule ons = (FactorySupplyRule) onf.followers.get(j);
					ons.update(symbols);
					followerFactories.add((TFSequence) ons.getTheFactory(symbols));
				}
				FactorySupplyRule fsrLeader = ((FactorySupplyRule) onf.leader);
				fsrLeader.update(symbols);
				TFForkedSequence nfs = new TFForkedSequence(onf.getName(), fsrLeader.getTheFactory(symbols), onf.singleValid);
				nfs.setFollowers(followerFactories);
				result.add(nfs);
			} else {
				result.add(((FactorySupplyRule) on).getTheFactory(symbols));
			}
		}
		return result;
	}
	
	@Override
	public boolean update(RulesByName symbols) {
		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
		for (FactorySupplyRule r : this.list) {
			r.update(localSymbols);
		}
		List<TokenFactory> fs = this.getChoiceFactories(localSymbols);
		this.factory.setFactories(fs);
		return true;
	}

	@Override
	public TokenFactory getShellFactory() {
		return this.factory;	
	}
}
