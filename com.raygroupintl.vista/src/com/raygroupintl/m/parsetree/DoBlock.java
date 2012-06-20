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

package com.raygroupintl.m.parsetree;

public class DoBlock extends BasicNode {
	private Node postCondition;
	private EntryList entryList;
	
	public DoBlock(Node postCondition) {
		this.postCondition = postCondition;
	}
	
	public Node getPostCondition() {
		return this.postCondition;
	}
	
	public EntryList getEntryList() {
		return this.entryList;
	}
	
	public void acceptPostCondition(Visitor visitor) {
		if (this.postCondition != null) {
			this.postCondition.accept(visitor);
		}		
	}
	
	public void acceptEntryList(Visitor visitor) {
		if (this.entryList != null) {			
			this.entryList.accept(visitor);
		}				
	}
	
	public void acceptSubNodes(Visitor visitor) {
		this.acceptPostCondition(visitor);
		this.acceptEntryList(visitor);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitDoBlock(this);
	}
	
	@Override
	public boolean setEntryList(EntryList entryList) {
		this.entryList = entryList;
		return true;
	}
}
