package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.parser.TokenFactory;

public class FSRForkedSequence extends FSRBase {
	public String name;
	public FactorySupplyRule leader;
	public List<FactorySupplyRule> followers = new ArrayList<FactorySupplyRule>();
	public boolean singleValid;

	public FSRForkedSequence(String name, FactorySupplyRule leader) {
		super(RuleSupplyFlag.INNER_REQUIRED);
		this.name = name;
		this.leader = leader;
	}
	
	@Override
	public TokenFactory getShellFactory() {
		return null;
	}
	
	public String getName() {
		return this.name;
	}
	
	public FactorySupplyRule getLeading(RulesByName names) {
		return null;
	}
	
	public int getSequenceCount() {
		return 1;
	}
	
	public void addFollower(FactorySupplyRule follower) {
		if (follower.getSequenceCount() == 1) {
			this.singleValid = true;
			this.leader = follower;
			return;
		}
		if ((follower instanceof FSRSequence) || (follower instanceof FSRCopy)) {			
			this.followers.add(follower);		
		} else {
			throw new ParseErrorException("Unsupported token in the choices; " + follower.getName() + " must be either a sequence or copy of one");
		}
	}
}
