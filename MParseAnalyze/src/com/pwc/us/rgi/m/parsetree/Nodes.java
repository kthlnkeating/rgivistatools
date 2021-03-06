//---------------------------------------------------------------------------
// Copyright 2013 PwC
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

package com.pwc.us.rgi.m.parsetree;

public abstract class Nodes<T extends Node> extends BasicNode {
	private static final long serialVersionUID = 1L;

	public abstract Iterable<T> getNodes();

	public abstract T getLastNode();
	
	public abstract T getFirstNode();
	
	protected void acceptElements(Visitor visitor) {
		for (Node node : this.getNodes()) {
			if (node != null) node.accept(visitor);
		}		
	}
 	
	public void acceptSubNodes(Visitor visitor) {
		this.acceptElements(visitor);
	}
	
	@Override
	public void accept(Visitor visitor) {
		this.acceptElements(visitor);
	}
	
	@Override
	public void update(AtomicGoto atomicGoto) {
		for (Node node : this.getNodes()) {
			if (node != null) node.update(atomicGoto);
		}		
	}

	@Override
	public void update(AtomicDo atomicDo) {
		for (Node node : this.getNodes()) {
			if (node != null) node.update(atomicDo);
		}		
	}
	@Override
	
	public void update(Extrinsic extrinsic) {
		for (Node node : this.getNodes()) {
			if (node != null) node.update(extrinsic);
		}		
	}
}
