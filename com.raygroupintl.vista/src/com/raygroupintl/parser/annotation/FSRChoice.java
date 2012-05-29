package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.parser.ForkAlgorithm;
import com.raygroupintl.parser.OrderedName;
import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFForkableChoice;
import com.raygroupintl.parser.TFForkedSequence;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TokenFactory;

public class FSRChoice extends FSRBase {
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
	
	private List<TokenFactory> verify(RulesByName symbols) {
		List<TokenFactory> result = new ArrayList<TokenFactory>();
		
		ForkAlgorithm algorithm = new ForkAlgorithm(this.getName());
		for (FactorySupplyRule r : this.list) {
			FactorySupplyRule ar = r.getActualRule(symbols);
			algorithm.add(ar, symbols);
		}
		int n = algorithm.list.size();
		for (int i=0; i<n; ++i) {
			OrderedName on =algorithm.list.get(i);
			if (on instanceof ForkAlgorithm.Forked) {
				ForkAlgorithm.Forked onf = (ForkAlgorithm.Forked) on;
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
		List<TokenFactory> fs = this.verify(localSymbols);
		this.factory.setFactories(fs);
		return true;
	}

	@Override
	public TFBasic getShellFactory() {
		return this.factory;	
	}
}
