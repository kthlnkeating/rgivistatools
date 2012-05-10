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

package com.raygroupintl.m.cmdtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Block<T extends Node> implements Node {
	private List<T> nodes;

	public void add(T node) {
		if (this.nodes == null) {
			this.nodes = new ArrayList<T>();
		}
		this.nodes.add(node);
	}
	
	public List<T> getNodes() {
		if (this.nodes == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.nodes);
		}
	}
}
