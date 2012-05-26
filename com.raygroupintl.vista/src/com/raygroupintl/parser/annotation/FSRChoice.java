package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFForkableChoice;
import com.raygroupintl.parser.TFForkedSequence;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TokenFactory;

public class FSRChoice extends FSRBase {
	private List<FactorySupplyRule> list = new ArrayList<FactorySupplyRule>(); 
	private Map<String, Integer> keyToIndex = new HashMap<String, Integer>();

	private Map<Integer, List<FSRSequence>> forkeds = new HashMap<Integer, List<FSRSequence>>();
	private Map<Integer, String> keys = new HashMap<Integer, String>();
	
	public FSRChoice(boolean required) {
		super(required);
	}
	
	public void add(FactorySupplyRule r) {
		String key = r.getEntryKey();
		if (key != null) {
			Integer index = this.keyToIndex.get(key);
			if (index != null) {
				List<FSRSequence> forked = this.forkeds.get(index);
				if (forked == null) {
					forked = new ArrayList<FSRSequence>();
					FactorySupplyRule current = this.list.get(index);
					if (current.isSequence()) {
						forked.add((FSRSequence) current);
						this.list.set(index, null);
					}
					this.forkeds.put(index, forked);
					this.keys.put(index, key);
				}
				if (! r.isSequence()) {
					FactorySupplyRule lead = this.list.get(index);
					if (lead != null) {
						throw new ParseErrorException("Ambigous choices");
					}
					this.list.set(index, r);
				} else {
					forked.add((FSRSequence) r);
				}
				return;
			} else {
				this.keyToIndex.put(key, this.list.size());
			}
		}		
		this.list.add(r);
	}
	
	private TFForkedSequence getForkedSequence(int index, String name, Map<String, TokenFactory> symbols) {
		FactorySupplyRule fsr = this.list.get(index);
		if (fsr == null) {
			String key = this.keys.get(index);
			TokenFactory lead = symbols.get(key);
			if (lead == null) return null;
			return new TFForkedSequence(name + String.valueOf(index), lead, false);			
		} else {
			TokenFactory lead = fsr.getFactory(name, symbols);
			if (lead == null) return null;
			return new TFForkedSequence(name + String.valueOf(index), lead, true);						
		}		
	}
	
	private TokenFactory getInnerFactory(FactorySupplyRule r, int index, String name, Map<String, TokenFactory> symbols) {
		List<FSRSequence> fs = this.forkeds.get(index);
		if (fs == null) {
			return r.getFactory(name, symbols);
		} else {
			TFForkedSequence forkedSeq = this.getForkedSequence(index, name, symbols);
			List<TFSequence> inners = new ArrayList<TFSequence>();
			for (FSRSequence f : fs) {
				TFSequence tf = f.getFactory(name, symbols);
				if (tf == null) return null;
				inners.add(tf);
			}
			forkedSeq.set(inners);
			return forkedSeq;
		}
	}
	
	@Override
	public TFForkableChoice getFactory(String name, Map<String, TokenFactory> symbols) {
		TFForkableChoice result = new TFForkableChoice(name);
		int index = 0;
		for (FactorySupplyRule r : this.list) {
			TokenFactory f = this.getInnerFactory(r, index, name, symbols);
			if (f == null) {
				return null;
			}
			result.add(f, symbols);
			++index;
		}
		return result;
	}

	@Override
	public TFBasic getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		if (asShell) {
			return new TFForkableChoice(name);	
		} else {
			return this.getFactory(name, symbols);
		}
	}
}
