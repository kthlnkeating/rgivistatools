package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFForkableChoice;
import com.raygroupintl.parser.TokenFactory;

public class FSRChoice extends FSRBase implements TopTFRule {
	private List<FactorySupplyRule> list = new ArrayList<FactorySupplyRule>(); 
	 
	public FSRChoice(boolean required) {
		super(required);
	}
	
	public void add(FactorySupplyRule r) {
		this.list.add(r);
	}
	 
	@Override
	public TFForkableChoice getFactory(String name, Map<String, TokenFactory> symbols) {
		TFForkableChoice result = new TFForkableChoice(name);
		for (FactorySupplyRule r : this.list) {
			TokenFactory f = r.getFactory(name, symbols);
			if (f == null) {
				return null;
			}
			result.add(f);
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
