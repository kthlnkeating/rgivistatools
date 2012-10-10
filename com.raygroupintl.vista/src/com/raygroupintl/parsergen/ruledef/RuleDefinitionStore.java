//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.parsergen.ruledef;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.charlib.PredicateFactory;
import com.raygroupintl.parser.TFCharacter;
import com.raygroupintl.parser.TFChoice;
import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFEnd;
import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.DelimitedListTokenType;
import com.raygroupintl.parsergen.ParseException;
import com.raygroupintl.parsergen.SequenceTokenType;
import com.raygroupintl.parsergen.TokenFactoryStore;
import com.raygroupintl.parsergen.TokenType;
import com.raygroupintl.parsergen.rulebased.FSRCustom;
import com.raygroupintl.parsergen.rulebased.FactorySupplyRule;

public class RuleDefinitionStore extends TokenFactoryStore {
	private static final class Triple<T extends TokenFactory, A extends Annotation> {
		public T factory;
		public A annotation;
		
		public Triple(T factory, A annotation) {
			this.factory = factory;
			this.annotation = annotation;
		}
	}
	
	public Map<String, TokenFactory> symbols = new HashMap<String, TokenFactory>();
	
	private java.util.List<Triple<TFChoice, Choice>> choices  = new ArrayList<Triple<TFChoice, Choice>>();
	private java.util.List<Triple<TFSequence, Sequence>> sequences  = new ArrayList<Triple<TFSequence, Sequence>>();
	private java.util.List<Triple<TFList, List>> lists  = new ArrayList<Triple<TFList, List>>();
	private java.util.List<Triple<TFSequence, List>> enclosedLists  = new ArrayList<Triple<TFSequence, List>>();
	private java.util.List<Triple<TFDelimitedList, List>> delimitedLists  = new ArrayList<Triple<TFDelimitedList, List>>();
	private java.util.List<Triple<TFSequence, List>> enclosedDelimitedLists  = new ArrayList<Triple<TFSequence, List>>();
	
	private java.util.List<FactorySupplyRule> rules  = new ArrayList<FactorySupplyRule>();
	private Map<String, FactorySupplyRule> topRules  = new HashMap<String, FactorySupplyRule>();
	
	public RuleDefinitionStore() {
	}
	
	private TokenFactory addChoice(String name, Choice choice) {
		TFChoice value = new TFChoice(name);
		this.choices.add(new Triple<TFChoice, Choice>(value, choice));
		return value;			
	}
	
	private void updateAdapter(Field f, TokenFactory target)  {
		TokenType tokenType = f.getAnnotation(TokenType.class);
		if (tokenType != null) {
			target.setTargetType(tokenType.value());
		}
		SequenceTokenType seqTokenType = f.getAnnotation(SequenceTokenType.class);
		if (seqTokenType != null) {
			target.setSequenceTargetType(seqTokenType.value());
		}
		DelimitedListTokenType dlTokenType = f.getAnnotation(DelimitedListTokenType.class);
		if (dlTokenType != null) {
			target.setDelimitedListTargetType(dlTokenType.value());
		}
	}

	private TokenFactory addSequence(String name, Sequence sequence, Field f) {
		TFSequence value = new TFSequence(name);
		this.sequences.add(new Triple<TFSequence, Sequence>(value, sequence));
		return value;			
	}
	
	private TokenFactory addList(String name, List list, Field f) {
		String delimiter = list.delim();
		String left = list.left();
		String right = list.right();
		if (delimiter.length() == 0) {
			if ((left.length() == 0) || (right.length() == 0)) {
				TFList value = new TFList(name);
				this.lists.add(new Triple<TFList, List>(value, list));
				return value;
			} else {
				TFSequence value = new TFSequence(name);
				this.enclosedLists.add(new Triple<TFSequence, List>(value, list));
				return value;
			}
		} else {			
			if ((left.length() == 0) || (right.length() == 0)) {
				TFDelimitedList value = new TFDelimitedList(name);
				this.delimitedLists.add(new Triple<TFDelimitedList, List>(value, list));
				return value;
			} else {
				TFSequence value = new TFSequence(name);
				this.enclosedDelimitedLists.add(new Triple<TFSequence, List>(value, list));
				return value;					
			}
		}
	}
	
	private TokenFactory addCharacters(String name, CharSpecified characters, Field f) {
		PredicateFactory pf = new PredicateFactory();
		pf.addChars(characters.chars());
		pf.addRanges(characters.ranges());
		pf.removeChars(characters.excludechars());
		pf.removeRanges(characters.excluderanges());
		Predicate result = pf.generate();
				
		if (characters.single()) {
			TFCharacter tf = new TFCharacter(name, result);
			return tf;
		} else {		
			TFString tf = new TFString(name, result);
			return tf;
		}
	}
	
	private TokenFactory addWords(String name, WordSpecified wordSpecied, Field f) {
		String word = wordSpecied.value();
		TFConstant tf = new TFConstant(name, word, wordSpecied.ignorecase());
		return tf;
	}
	
	@Override
	protected TokenFactory add(Field f)  {
		String name = f.getName();			
		Choice choice = f.getAnnotation(Choice.class);
		if (choice != null) {
			return this.addChoice(name, choice);
		}			
		Sequence sequence = f.getAnnotation(Sequence.class);
		if (sequence != null) {
			return this.addSequence(name, sequence, f);
		}			
		List list = f.getAnnotation(List.class);
		if (list != null) {
			return this.addList(name, list, f);
		}			
		CharSpecified characters = f.getAnnotation(CharSpecified.class);
		if (characters != null) {
			return this.addCharacters(name, characters, f);
		}
		WordSpecified words = f.getAnnotation(WordSpecified.class);
		if (words != null) {
			return this.addWords(name, words, f);
		}
		return null;			
	}
	
	protected <T> boolean handleField(T target, Field f) throws IllegalAccessException {
		String name = f.getName();
		TokenFactory already = this.symbols.get(name);
		if (already == null) {					
			TokenFactory value = (TokenFactory) f.get(target);
			if (value == null) {
				value = this.add(f);
				if (value != null) {
					this.updateAdapter(f, value);
					f.set(target, value);
				} else {
					return false;
				}
			} else {
				FSRCustom fsr = new FSRCustom(value);
				this.topRules.put(name, fsr);
				this.rules.add(fsr);
			}
			if (value != null) {
				this.symbols.put(name, value);
			}
		} else {
			f.set(target, already);						
		}
		return true;
	}
	
	private void updateChoices() {		
		for (Triple<TFChoice, Choice> p : this.choices) {
			TokenFactory[] fs = getFactories(this.symbols, p.annotation.value());
			p.factory.setFactories(fs);
		}
	}
	
	private void updateSequences() {
		for (Triple<TFSequence, Sequence> p : this.sequences) {
			TokenFactory[] fs = getFactories(this.symbols, p.annotation.value());
			boolean[] required = getRequiredFlags(p.annotation.required(), fs.length);
			p.factory.setFactories(fs, required);
		}
	}

	private void updateLists() {
		for (Triple<TFList, List> p : this.lists) {
			TokenFactory f = this.symbols.get(p.annotation.value());
			p.factory.setElement(f);
		}	
	}
	
	private void updateEnclosedLists() {
		for (Triple<TFSequence, List> p : this.enclosedLists) {
			TokenFactory e = this.symbols.get(p.annotation.value());
			TokenFactory l = this.symbols.get(p.annotation.left());
			TokenFactory r = this.symbols.get(p.annotation.right());
			TFList f = new TFList(p.factory.getName() + ".list", e);
			p.factory.setFactories(new TokenFactory[]{l, f, r}, new boolean[]{true, ! p.annotation.none(), true});
		}	
	}
	
	private void updateEnclosedDelimitedLists() {
		for (Triple<TFSequence, List> p : this.enclosedDelimitedLists) {
			TokenFactory e = this.symbols.get(p.annotation.value());
			TokenFactory d = this.symbols.get(p.annotation.delim());
			TokenFactory l = this.symbols.get(p.annotation.left());
			TokenFactory r = this.symbols.get(p.annotation.right());
			
			TFDelimitedList dl = new TFDelimitedList(p.factory.getName() + ".list");
			dl.set(e, d, p.annotation.empty());
			
			p.factory.setFactories(new TokenFactory[]{l, dl, r}, new boolean[]{true, ! p.annotation.none(), true});
		}	
	}
	
	private void updateDelimitedLists() {
		for (Triple<TFDelimitedList, List> p : this.delimitedLists) {
			TokenFactory e = this.symbols.get(p.annotation.value());
			TokenFactory d = this.symbols.get(p.annotation.delim());
			boolean empty = p.annotation.empty();
			p.factory.set(e, d, empty);
		}	
	}
	
	@Override
	public void addAssumed() {		
		TokenFactory end = new TFEnd("end");
		this.symbols.put("end", end);
	}

	@Override
	public void update(Class<?> cls)  throws IllegalAccessException, InstantiationException, ParseException {
		this.updateChoices();
		this.updateSequences();
		this.updateLists();
		this.updateEnclosedLists();
		this.updateEnclosedDelimitedLists();
		this.updateDelimitedLists();
	}

	private static TokenFactory[] getFactories(Map<String, TokenFactory> map, String[] names) {
		int n = names.length;
		TokenFactory[] fs = new TokenFactory[n];
		for (int i=0; i<n; ++i) {
			String name = names[i];
			fs[i] = map.get(name);
		}
		return fs;
	}
	
	private static boolean[] getRequiredFlags(String specification, int n) {
		boolean[] result = new boolean[n];
		if (specification.equals("all")) {
			Arrays.fill(result, true);
			return result;
		}
		if (specification.equals("none")) {
			return result;
		}
		for (int i=0; i<specification.length(); ++i) {
			char ch = specification.charAt(i);
			if (ch == 'r') {
				result[i] = true;
			}
		}
		return result;
	}
}