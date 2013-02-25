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

package com.raygroupintl.struct;

import java.util.HashMap;

public class HierarchicalMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 1L;
	
	private HierarchicalMap<K, V> parent;
	
	public HierarchicalMap() {		
	}
	
	public HierarchicalMap(HierarchicalMap<K, V> parent) {
		this.parent = parent;
	}
	
	public V getThruHierarchy(K key) {
		V result = super.get(key);
		if ((result == null) && (this.parent != null)) {
			return this.parent.getThruHierarchy(key);
		} else {
			return result;
		}
	}
	
	public HierarchicalMap<K, V> getParent() {
		return this.parent;
	}
}