package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TSymbolList extends TSequence  implements TopFactorySupplyRule, FactorySupplyRule {
	public TSymbolList(java.util.List<Token> tokens) {
		super(tokens);
	}
	
	private TFBasic getListFactory(String name, TokenFactory element, TokenFactory delimiter, boolean emptyAllowed) {
		if (delimiter == null) { 
			return new TFList(name, element);
		} else {
			TFDelimitedList r = new TFDelimitedList(name);
			r.set(element, delimiter, emptyAllowed);
			return r;
		}		
	}
	
	@Override
	public TFBasic getFactory(String name, Map<String, TokenFactory> map) {
		FactorySupplyRule elementGenerator = (FactorySupplyRule) this.get(1);
		TokenFactory element = elementGenerator.getFactory(name + ".element", map);
		TSequence delimleftrightspec = (TSequence) this.get(2);
		if (delimleftrightspec == null) {
			return new TFList(name, element);
		} else {
			FactorySupplyRule delimGenerator = (FactorySupplyRule) delimleftrightspec.get(1);
			TokenFactory delimiter = delimGenerator == null ? null : delimGenerator.getFactory(name + ".delimiter", map);
			TSequence leftrightSpec = (TSequence) delimleftrightspec.get(2);
			TFBasic dl = this.getListFactory(name, element, delimiter, false);
			if (leftrightSpec == null) {
				return dl;				
			} else {
				TFSequence result = new TFSequence(name);
				FactorySupplyRule leftGenerator = (FactorySupplyRule) leftrightSpec.get(1);
				FactorySupplyRule rightGenerator = (FactorySupplyRule) leftrightSpec.get(3);
				TokenFactory left = leftGenerator.getFactory(name + ".left", map);
				TokenFactory right = rightGenerator.getFactory(name + ".right", map);
				TokenFactory[] factories = {left, dl, right};
				boolean[] required = {true, true, true};
				result.setFactories(factories, required);
				return result;
			}
		}	
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}
	
	@Override
	public TFBasic getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		if (! asShell) {
			return this.getFactory(name, symbols);
		} else {			
			TSequence delimleftrightspec = (TSequence) this.get(2);
			if (delimleftrightspec == null) {
				return new TFList(name);
			} else {
				FactorySupplyRule delimGenerator = (FactorySupplyRule) delimleftrightspec.get(1);
				TSequence leftrightSpec = (TSequence) delimleftrightspec.get(2);
				if (leftrightSpec == null) {
					if (delimGenerator == null) {
						return new TFList(name);
					} else {
						return new TFDelimitedList(name);					
					}
				} else {
					return new TFSequence(name);
				}
			}
		}
	}	
}
