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
import java.util.Map;
import java.util.Set;

public class APIData {
	private Set<String> inputs;
	private Set<String> outputs;
	private Set<String> globals;
	
	public APIData(Set<String> inputs, Set<String> outputs, Set<String> globals) {
		this.inputs = new HashSet<String>(inputs);
		this.outputs = new HashSet<String>(outputs);
		this.globals = globals;
	}
	
	private static void add(Set<String> target, String name, boolean hasSubscripts) {
		if (target.contains(name)) {
			if (hasSubscripts) {
				target.remove(name);
				target.add(name + "*");
			}
		} else if (! target.contains(name + "*")) {
			if (hasSubscripts) name = name + "*";
			target.add(name);
		}		
	}
	
	public void addInput(String name, boolean hasSubscripts) {
		if (this.inputs == null) this.inputs = new HashSet<String>();
		add(this.inputs, name, hasSubscripts);
	}

	public void addOutputs(String name, boolean hasSubscripts) {
		if (this.outputs == null) this.outputs = new HashSet<String>();
		add(this.outputs, name, hasSubscripts);
	}
	
	private static void merge(Set<String> target, Set<String> source, int sourceIndex, Map<String, Integer> newedLocals) {
		if (target == null) return;
		for (String name : source) {
			Integer index = newedLocals.get(name);
			if (index == null) {
				target.add(name);
			} else if (index.intValue() > sourceIndex) {
				target.add(name);
			}
		}		
	}
	
	public void merge(APIData source, int sourceIndex, Map<String, Integer> newedLocals) {
		merge(this.inputs, source.inputs, sourceIndex, newedLocals);
		merge(this.outputs, source.outputs, sourceIndex, newedLocals);
		this.globals.addAll(source.globals);
	}
	
	private static List<String> getIO(Set<String> source) {
		if (source == null) {
			return Collections.emptyList();
		} else {
			List<String> result = new ArrayList<String>(source);
			Collections.sort(result);
			return result;
		}
		
	}
	
	public List<String> getInputs() {
		return getIO(this.inputs);
	}

	public List<String> getOutputs() {
		return getIO(this.outputs);
	}

	public List<String> getGlobals() {
		return getIO(this.globals);
	}
}
