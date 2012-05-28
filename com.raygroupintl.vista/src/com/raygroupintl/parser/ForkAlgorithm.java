package com.raygroupintl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.parser.annotation.FSRCopy;
import com.raygroupintl.parser.annotation.FSRSequence;
import com.raygroupintl.parser.annotation.ParseErrorException;

public class ForkAlgorithm {	
	public static class Forked implements OrderedName {
		public String name;
		public OrderedName leader;
		public List<OrderedName> followers = new ArrayList<OrderedName>();
		public boolean singleValid;

		public Forked(String name, OrderedName leader) {
			this.name = name;
			this.leader = leader;
		}
		
		public String getName() {
			return this.name;
		}
		
		public OrderedName getLeading(OrderedNameContainer names) {
			return null;
		}
		
		public int getSequenceCount() {
			return 1;
		}
		
		private void addFollower(OrderedName follower) {
			if (follower.getSequenceCount() == 1) {
				this.singleValid = true;
				this.leader = follower;
				return;
			}
			if ((follower instanceof FSRSequence) || (follower instanceof FSRCopy)) {			
				this.followers.add(follower);		
			} else {
				throw new ParseErrorException("Forked sequence only supports objects of kind " + FSRSequence.class.getName());
			}
		}
	}
	
	public ForkAlgorithm(String name) {
		this.aname = name;
	}
 	
	public String aname;
	
	public List<OrderedName> list = new ArrayList<OrderedName>();
	
	public Map<String, Integer> choiceOrder = new HashMap<String, Integer>();
	public Map<Integer, List<String>> possibleShared = new HashMap<Integer, List<String>>();
	public Set<String> restrictedChoices = new HashSet<String>();
	public Map<Integer, String> leadingShared = new HashMap<Integer, String>();
	
	public void updateChoicePossibilities(OrderedName f, OrderedNameContainer symbols, int index) {
		OrderedName previous = null;
		List<String> allForIndex = new ArrayList<String>();
		while (f != previous) {
			String name = f.getName();
			if (! restrictedChoices.contains(name)) {
				if (symbols.hasName(name)) {
					this.choiceOrder.put(name, index);
					allForIndex.add(name);
				}
			}
			previous = f;
			f = f.getLeading(symbols);
		}
		this.possibleShared.put(index, allForIndex);
	}
	
	public Integer findInChoices(OrderedName f, OrderedNameContainer names) {
		OrderedName previous = null;
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
	
	public void add(OrderedName tf, OrderedNameContainer symbols) {
		Integer existing = this.findInChoices(tf, symbols);
		if (existing == null) {
			int n = this.list.size();
			this.list.add(tf);
			this.updateChoicePossibilities(tf, symbols, n);
		} else {
			int n = existing.intValue();
			OrderedName current = this.list.get(n);
			if (current instanceof Forked) {
				((Forked) current).addFollower(tf);
			} else {
				String name = this.leadingShared.get(n);
				OrderedName leading = symbols.getNamed(name);
				Forked newForked = new Forked(this.aname + "." + name, leading);
				newForked.addFollower(current);
				newForked.addFollower( tf);
				this.list.set(n, newForked);
			}
		}
	}	
}
