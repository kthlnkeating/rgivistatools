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

package com.raygroupintl.parsergen.rulebased;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.parser.Adapter;
import com.raygroupintl.parser.TFForkedSequence;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.ParseErrorException;

public class FSRForkedSequence<T extends Token> extends FSRBase<T> {
	private String name;
	private FactorySupplyRule<T> leader;
	private List<FSRSequence<T>> followers = new ArrayList<FSRSequence<T>>();
	private FactorySupplyRule<T> single;
	private TFForkedSequence<T> factory;

	public FSRForkedSequence(String name, FactorySupplyRule<T> leader) {
		this.name = name;
		this.leader = leader;
		this.factory = new TFForkedSequence<T>(name);
	}
	
	@Override
	public TokenFactory<T> getShellFactory() {
		return this.factory;
	}
	
	public String getName() {
		return this.name;
	}
	
	public FactorySupplyRule<T> getLeading(RulesByName<T> names) {
		return null;
	}
	
	public int getSequenceCount() {
		return 1;
	}
	
	private void addNonSequence(FactorySupplyRule<T> single) {
		if (this.single != null) {
			throw new ParseErrorException("Cannot have two rules that are not sequences in the same rule");
		}
		this.single = single;
	}
	
	private void addSequence(FSRSequence<T> sequence) {
		this.followers.add(sequence);
	}
	
	public void add(FactorySupplyRule<T> follower) {
		if (follower.getSequenceCount() == 1) {
			this.addNonSequence(follower);
		} else {
			FSRSequence<T> sequence = (FSRSequence<T>) follower;
			this.addSequence(sequence);
		}
	}
	
	@Override
	public boolean update(RulesByName<T> symbols) {
		this.leader.update(symbols);
		this.factory.setLeader(this.leader.getTheFactory(symbols));
		if (this.single != null) {
			if ((this.leader != this.single)) this.single.update(symbols);
			this.factory.setSingleAdapter(this.single.getAdapter(symbols));
		}
		for (FSRSequence<T> follower : this.followers) {
			follower.update(symbols);
			TFSequence<T> tf = follower.getTheFactory(symbols);
			Adapter<T> a = follower.get(0).getAdapter(symbols);
			this.factory.addSequence(tf, a);
		}		
		return true;
	}
}
