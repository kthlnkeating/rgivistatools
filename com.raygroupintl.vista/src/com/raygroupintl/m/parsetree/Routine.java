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

public class Routine extends BasicNode {
	private String name;
	private EntryList entryList;
	private ErrorNode errorNode;
	
	public Routine(String name) {
		this.name = name;
	}
	
	public Routine(String name, ErrorNode errorNode) {
		this.name = name;
		this.errorNode = errorNode;
	}
	
	public String getName() {
		return this.name;
	}
	
	public EntryList getEntryList() {
		return this.entryList;
	}
	
	public ErrorNode getErrorNode() {
		return this.errorNode;
	}
	
	public void acceptSubNodes(Visitor visitor) {
		if (this.errorNode != null) {
			this.errorNode.accept(visitor);
			if (this.errorNode.getError().isFatal()) {
				return;
			}
		}
		if (this.entryList != null) {
			this.entryList.accept(visitor);
		}
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitRoutine(this);
	}
	
	@Override
	public boolean setEntryList(EntryList entryList) {
		this.entryList = entryList;
		return true;
	}
	
	public void setErrorNode(ErrorNode errorNode) {
		this.errorNode = errorNode;
	}
}
