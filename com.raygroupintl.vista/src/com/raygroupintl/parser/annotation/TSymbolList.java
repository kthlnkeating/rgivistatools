package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.TFSequenceStatic;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TSymbolList extends TSequence  implements RulePieceGenerator {
	public TSymbolList(java.util.List<Token> tokens) {
		super(tokens);
	}
	
	private TokenFactory getListFactory(String name, TokenFactory element, TokenFactory delimiter, boolean emptyAllowed) {
		if (delimiter == null) { 
			return new TFList(name, element);
		} else {
			TFDelimitedList r = new TFDelimitedList(name);
			r.set(element, delimiter, emptyAllowed);
			return r;
		}		
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> map) {
		RulePieceGenerator elementGenerator = (RulePieceGenerator) this.get(1);
		TokenFactory element = elementGenerator.getFactory(name + ".element", map);
		TSequence delimleftrightspec = (TSequence) this.get(2);
		if (delimleftrightspec == null) {
			return new TFList(name, element);
		} else {
			RulePieceGenerator delimGenerator = (RulePieceGenerator) delimleftrightspec.get(1);
			TokenFactory delimiter = delimGenerator == null ? null : delimGenerator.getFactory(name + ".delimiter", map);
			TSequence leftrightSpec = (TSequence) delimleftrightspec.get(2);
			TokenFactory dl = this.getListFactory(name, element, delimiter, false);
			if (leftrightSpec == null) {
				return dl;				
			} else {
				TFSequenceStatic result = new TFSequenceStatic(name);
				RulePieceGenerator leftGenerator = (RulePieceGenerator) leftrightSpec.get(1);
				RulePieceGenerator rightGenerator = (RulePieceGenerator) leftrightSpec.get(3);
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
	public TokenFactory getPreliminaryTop(String name) {
		TSequence delimleftrightspec = (TSequence) this.get(2);
		if (delimleftrightspec == null) {
			return new TFList(name);
		} else {
			RulePieceGenerator delimGenerator = (RulePieceGenerator) delimleftrightspec.get(1);
			TSequence leftrightSpec = (TSequence) delimleftrightspec.get(2);
			if (leftrightSpec == null) {
				if (delimGenerator == null) {
					return new TFList(name);
				} else {
					return new TFDelimitedList(name);					
				}
			} else {
				if (delimGenerator == null) {
					return new TFList(name);
				} else {
					return new TFDelimitedList(name);					
				}
			}
		}	
	}	
}
