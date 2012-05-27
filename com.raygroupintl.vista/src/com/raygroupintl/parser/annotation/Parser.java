package com.raygroupintl.parser.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.charlib.PredicateFactory;
import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFCharacter;
import com.raygroupintl.parser.TFChoiceBasic;
import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFEnd;
import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.TokenFactory;

public class Parser {
	private static final class Triple<T extends TokenFactory, A extends Annotation> {
		public T factory;
		public A annotation;
		
		public Triple(T factory, A annotation) {
			this.factory = factory;
			this.annotation = annotation;
		}
	}
	
	private static final class RuleStore {
		public FactorySupplyRule rule;
		
		public RuleStore(TFBasic factory, FactorySupplyRule rule) {
			this.rule = rule;
		}
	}
	
	private static class Store {
		private static RuleParser ruleParser;
		
		public Map<String, TokenFactory> symbols = new HashMap<String, TokenFactory>();
		
		private java.util.List<Triple<TFChoiceBasic, Choice>> choices  = new ArrayList<Triple<TFChoiceBasic, Choice>>();
		private java.util.List<Triple<TFSequence, Sequence>> sequences  = new ArrayList<Triple<TFSequence, Sequence>>();
		private java.util.List<Triple<TFList, List>> lists  = new ArrayList<Triple<TFList, List>>();
		private java.util.List<Triple<TFSequence, List>> enclosedLists  = new ArrayList<Triple<TFSequence, List>>();
		private java.util.List<Triple<TFDelimitedList, List>> delimitedLists  = new ArrayList<Triple<TFDelimitedList, List>>();
		private java.util.List<Triple<TFSequence, List>> enclosedDelimitedLists  = new ArrayList<Triple<TFSequence, List>>();
		
		private java.util.List<RuleStore> rules  = new ArrayList<RuleStore>();
		private Map<String, RuleSupply> ruleSupplies  = new HashMap<String, RuleSupply>();
		private Map<String, FactorySupplyRule> topRules  = new HashMap<String, FactorySupplyRule>();
		
		private TokenFactory addChoice(String name, Choice choice) {
			TFChoiceBasic value = new TFChoiceBasic(name);
			this.choices.add(new Triple<TFChoiceBasic, Choice>(value, choice));
			return value;			
		}
		
		private void updateAdapter(Field f, TFBasic target)  {
			TokenType tokenType = f.getAnnotation(TokenType.class);
			if (tokenType != null) {
				target.setTargetType(tokenType.value());
			} 		
		}

		private TokenFactory addSequence(String name, Sequence sequence, Field f) {
			TFSequence value = new TFSequence(name);
			this.sequences.add(new Triple<TFSequence, Sequence>(value, sequence));
			return value;			
		}
		
		private TokenFactory addRule(String name, Rule ruleAnnotation, Field f) {
			if (ruleParser == null) {
				ruleParser = new RuleParser();
			}
			String ruleText = ruleAnnotation.value();
			RuleSupply ruleSupply = this.ruleSupplies.get(name);
			if (ruleSupply == null) {
				ruleSupply = ruleParser.getTopTFRule(name, ruleText);
				if (ruleSupply == null) return null;
				this.ruleSupplies.put(name, ruleSupply);
			}
			FactorySupplyRule topRule = this.topRules.get(name);	
			if (topRule == null) {
				topRule = ruleSupply.getRule(RuleSupplyFlag.TOP, name, this.ruleSupplies);		
				this.topRules.put(name, topRule);
			}
			if (topRule != null) {
				TFBasic value = (TFBasic) topRule.getShellFactory(this.symbols);
				this.rules.add(new RuleStore(value, topRule));
				return value;		
			}
			return null;
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
		
		private TokenFactory add(Field f)  {
			String name = f.getName();
			
			Choice choice = f.getAnnotation(Choice.class);
			if (choice != null) {
				return this.addChoice(name, choice);
			}			
			Sequence sequence = f.getAnnotation(Sequence.class);
			if (sequence != null) {
				return this.addSequence(name, sequence, f);
			}			
			Rule description = f.getAnnotation(Rule.class);
			if (description != null) {
				return this.addRule(name, description, f);
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
		
		private <T> boolean handleField(T target, Field f) throws IllegalAccessException {
			String name = f.getName();
			TokenFactory already = this.symbols.get(name);
			if (already == null) {					
				TokenFactory value = (TokenFactory) f.get(target);
				if (value == null) {
					value = this.add(f);
					if (value != null) {
						if (value instanceof TFBasic) {
							this.updateAdapter(f, (TFBasic) value);
						}								
						f.set(target, value);
					} else {
						return false;
					}
				} 
				if (value != null) {
					this.symbols.put(name, value);
				}
			} else {
				f.set(target, already);						
			}
			return true;
		}
		
		private <T> void handleWithRemaining(T target, Field f, Set<String> remainingNames, java.util.List<Field> remaining) throws IllegalAccessException{
			String name = f.getName();
			if (remainingNames.contains(name)) {
				remaining.add(f);							
				return;
			}
			if (! this.handleField(target, f)) {
				remainingNames.add(name);
				remaining.add(f);
			}			
		}
		
		public <T> void add(T target) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
			Set<String> remainingNames = new HashSet<String>();
			java.util.List<Field> remaining = new ArrayList<Field>();
			Class<?> cls = target.getClass();
			while (! cls.equals(Object.class)) {
				for (Field f : cls.getDeclaredFields()) {
					if (TokenFactory.class.isAssignableFrom(f.getType())) {
						this.handleWithRemaining(target, f, remainingNames, remaining);
					}
				}
				cls = cls.getSuperclass();
			}
			while (remaining.size() > 0) {
				remainingNames = new HashSet<String>();
				java.util.List<Field> loopRemaining = new ArrayList<Field>();
				for (Field f : remaining) {
					this.handleWithRemaining(target, f, remainingNames, loopRemaining);
				}
				if (remaining.size() == loopRemaining.size()) {
					String symbols = "";
					for (Field f : remaining) {
						symbols += ", " + f.getName();
					}
					throw new ParseErrorException("Following symbols are not resolved: " + symbols.substring(1));
				}
				remaining = loopRemaining;
			}			
		}
		
		private void updateChoices() {		
			for (Triple<TFChoiceBasic, Choice> p : this.choices) {
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
	
		private static void updateRules(java.util.List<RuleStore> list, java.util.List<RuleStore> remaining, Map<String, TokenFactory> symbols) {
			for (RuleStore p : list) {
				FactorySupplyRule trule = p.rule;
				TFBasic f = (TFBasic) trule.getFactory(symbols);
				if (f == null) {
					remaining.add(p);
				} 
			}
		}

		private void updateRules() {
			java.util.List<RuleStore> remaining = new ArrayList<RuleStore>();
			updateRules(this.rules, remaining, this.symbols);
			while (remaining.size() > 0) {
				java.util.List<RuleStore> nextRemaining = new ArrayList<RuleStore>();
				updateRules(remaining, nextRemaining, this.symbols);
				if (nextRemaining.size() == remaining.size()) {
					throw new ParseErrorException("There looks to be a circular symbol condition");					
				}
				remaining = nextRemaining;
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
		
		public void update(Class<?> cls, boolean ignore)  throws IllegalAccessException, InstantiationException, ParseException {
			this.updateChoices();
			this.updateSequences();
			this.updateLists();
			this.updateEnclosedLists();
			this.updateEnclosedDelimitedLists();
			this.updateDelimitedLists();
			if (! ignore) {
				this.updateRules();
			}
		}		
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
	
	<T> T parse(Class<T> cls, boolean ignore) throws ParseException {
		try {
			T target = cls.newInstance();
			Store store = new Store();
			store.add(target);
			store.symbols.put("end", new TFEnd("end"));
			store.update(cls, ignore);
			return target;
		} catch (IllegalAccessException iae) {
			throw new ParseException(iae);
		} catch (InstantiationException ine) {
			throw new ParseException(ine);
		} catch (ClassNotFoundException cnf) {
			throw new ParseException(cnf);
		} catch (NoSuchMethodException nsm) {
			throw new ParseException(nsm);			
		}
	}

	public <T> T parse(Class<T> cls) throws ParseException {
		return this.parse(cls, false);
	}
}
