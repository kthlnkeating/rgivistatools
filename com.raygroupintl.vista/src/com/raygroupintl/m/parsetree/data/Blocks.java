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

package com.raygroupintl.m.parsetree.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.struct.Child;

public class Blocks<T extends Child<Blocks<T>>> {
	private Map<String, T> blocks = new HashMap<String, T>();
	private T parent;
	
	public Blocks() {		
	}
	
	public Blocks(T parent) {
		this.parent = parent;
	}
	
	public Set<String> getTags() {
		return this.blocks.keySet();
	}
	
	public Collection<T> getBlocks() {
		return this.blocks.values();
	}
	
	public T get2(String name) {
		return this.blocks.get(name);
	}
	
	public T get(String name) {
		T result = this.blocks.get(name);
		if ((result == null) && (this.parent != null)) {
			Blocks<T> grandParent = this.parent.getParent();
			if (grandParent != null) {
				return grandParent.get(name);
			} else {
				return null;
			}
		} else {
			return result;
		}
	}
	
	public void put(String name, T block) {
		this.blocks.put(name, block);
	}
	
	public T getParent() {
		return this.parent;
	}
}