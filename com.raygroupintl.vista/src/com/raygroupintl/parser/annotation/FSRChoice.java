package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.parser.ForkAlgorithm;
import com.raygroupintl.parser.OrderedName;
import com.raygroupintl.parser.OrderedNameContainer;
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
			String name = on.getName();

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
		
		
/*		List<TokenFactory> fs = this.factory.getFactories();
		int n = fs.size();
				
		assert(n == algorithm.list.size());
		for (int i=0; i<n; ++i) {
			TokenFactory f = fs.get(i);
			String name = f.getName();
			OrderedName on =algorithm.list.get(i);
			assert(name.equals(on.getName()));
			if (f instanceof TFForkedSequence) {
				TFForkedSequence ffs = (TFForkedSequence) f;
				assert(on instanceof ForkAlgorithm.Forked);
				ForkAlgorithm.Forked onf = (ForkAlgorithm.Forked) on;
				List<TFSequence> folowers = ffs.getFollowers();
				int m = folowers.size();
				assert(m == onf.followers.size());
				for (int j=0; j<m; ++j) {
					TFSequence s = folowers.get(j);
					OrderedName ons = (OrderedName) onf.followers.get(j);
					assert(s.getName().equals(ons.getName()));
				}
				assert(onf.leader.getName().equals(ffs.getLeader().getName()));
				assert(onf.singleValid == ffs.isSingleValid());
				
			}
		}
*/	
	}
	
	@Override
	public TFForkableChoice getFactory(RulesByName symbols) {
		this.factory.reset(this.getName());

		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
			
		for (FactorySupplyRule r : this.list) {
			TokenFactory f = r.getFactory(localSymbols);
			if (f == null) {
				return null;
			}
			this.factory.add(f, symbols);
		}
		
		
		this.verify(localSymbols);
		
		return this.factory;
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
