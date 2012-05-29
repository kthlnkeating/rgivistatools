package com.raygroupintl.parser.annotation;

class RulesByNameLocal implements RulesByName {
	private RulesByName factories;
	private FactorySupplyRule me;
	
	public RulesByNameLocal(RulesByName factories, FactorySupplyRule me) {
		this.factories = factories;
		this.me = me;
	}
	
	@Override
	public FactorySupplyRule get(String name) {
		if (this.me.getName().equals(name)) {
			return this.me;
		} else {
			return this.factories.get(name);
		}
	}
	
	@Override
	public void put(String name, FactorySupplyRule r) {
		this.factories.put(name, r);
	}
	
	@Override
	public boolean hasRule(String name) {
		if (this.me.getName().equals(name)) {
			return true;
		} else {
			return this.factories.get(name) != null;
		}
	}
}
