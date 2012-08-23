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
