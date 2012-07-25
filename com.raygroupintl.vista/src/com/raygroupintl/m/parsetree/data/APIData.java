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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class APIData {
	private Block sourceBlock;
	
	private Set<String> assumeds;
	private Set<String> globals;
		
	public APIData(Block source) {
		this.sourceBlock = source;
	}
		
	public void setAssumeds(Set<String> assumeds) {
		this.assumeds = new HashSet<String>(assumeds);
	}
	
	public void setAssumeds(APIData rhs) {
		if (rhs != null) {
			this.setAssumeds(rhs.assumeds);
		}
	}
	
	public Block getSourceBlock() {
		return this.sourceBlock;
	}
	
	private boolean mergeAssumed(Block thisBlock, String name, int sourceIndex) {
		if ((! thisBlock.isDefined(name, sourceIndex) && (! this.assumeds.contains(name)))) {
			this.assumeds.add(name);
			return true;
		}
		return false;
	}
	
	public int mergeAssumeds(APIData source, int sourceIndex) {
		Block thisBlock = this.getSourceBlock();
		int result = 0;
		for (String name : source.assumeds) {
			boolean b = this.mergeAssumed(thisBlock, name, sourceIndex);
			if (b) ++result;
		}
		return result;
	}
	
	public void mergeGlobals(APIData source) {
		this.mergeGlobals(source.globals);
	}
	
	public void mergeGlobals(Set<String> globals) {
		if (this.globals == null) {
			this.globals = new HashSet<String>(globals);	
		} else {
			this.globals.addAll(globals);		
		}
	}
		
	private List<String> getIO(Set<String> source) {
		if (source == null) {
			return Collections.emptyList();
		} else {
			List<String> result = new ArrayList<String>(source);
			Collections.sort(result);
			return result;
		}
		
	}
	
	public List<String> getAssumed() {
		return getIO(this.assumeds);
	}
 	
	public List<String> getGlobals() {
		return getIO(this.globals);
	}
}
