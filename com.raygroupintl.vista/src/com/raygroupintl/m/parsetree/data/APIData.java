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
//import java.util.logging.Logger;

import com.raygroupintl.struct.Indexed;

public class APIData {
	//private final static Logger LOGGER = Logger.getLogger(APIData.class.getName());

	private Block sourceBlock;
	
	private Set<String> inputs;
	private Set<String> outputs;
	private Set<String> subscripted;
	private Set<String> globals;
		
	public APIData(Block source) {
		this.sourceBlock = source;
	}
		
	public void set(Set<String> inputs, Set<String> outputs, Set<String> subscripted, Set<String> globals) {
		this.inputs = new HashSet<String>(inputs);
		this.outputs = new HashSet<String>(outputs);
		this.globals = new HashSet<String>(globals);
		this.subscripted = subscripted;
	}
	
	public Block getSourceBlock() {
		return this.sourceBlock;
	}
	
	private void mergeInput(Block thisBlock, Block sourceBlock, String name, int sourceIndex) {
		if ((! thisBlock.isNewed(name, sourceIndex) && (! thisBlock.isAssigned(name, sourceIndex)))) {
			this.inputs.add(name);
			if (sourceBlock.isSubscripted(name)) {
				this.subscripted.add(name);
			}
		} 		
	}
	
	private void mergeInputs(APIData source, int sourceIndex, List<Indexed<String>> byRefs) {
		Block thisBlock = this.getSourceBlock();
		Block sourceBlock = source.getSourceBlock();
		for (String name : source.inputs) {
			Integer formalIndex = sourceBlock.getAsFormal(name);
			if (formalIndex == null) {	
				this.mergeInput(thisBlock, sourceBlock, name, sourceIndex);
			} else if (byRefs!= null) {
				int index = formalIndex.intValue();
				for (Indexed<String> byRef : byRefs) {
					if (index == byRef.getIndex()) {
						String byRefName = byRef.getObject();
						this.mergeInput(thisBlock, sourceBlock, byRefName, sourceIndex);
					}
				}				
			}
		}		
	}
	
	private boolean mergeOutput(Block thisBlock, Block sourceBlock, String name, int sourceIndex) {
		if ((! thisBlock.isNewed(name, sourceIndex) && (! this.outputs.contains(name)))) {
			//if ("A".equals(name)) {
			//	LOGGER.info(sourceBlock.getEntryId().toString() + " updated " + thisBlock.getEntryId().toString() + "\n");
			//}
			this.outputs.add(name);
			if (sourceBlock.isSubscripted(name)) {
				this.subscripted.add(name);
			}
			return true;
		}
		return false;
	}

	private int mergeOutputs(APIData source, int sourceIndex, List<Indexed<String>> byRefs) {
		Block thisBlock = this.getSourceBlock();
		Block sourceBlock = source.getSourceBlock();
		int result = 0;
		for (String name : source.outputs) {			
			Integer formalIndex = sourceBlock.getAsFormal(name);
			if (formalIndex == null) {
				boolean b = this.mergeOutput(thisBlock, sourceBlock, name, sourceIndex);
				if (b) ++result;
			} else if (byRefs!= null) {
				int index = formalIndex.intValue();
				for (Indexed<String> byRef : byRefs) {
					if (index == byRef.getIndex()) {
						String byRefName = byRef.getObject();
						boolean b = this.mergeOutput(thisBlock, sourceBlock, byRefName, sourceIndex);
						if (b) ++result;
					}
				}
			}
		}
		return result;
	}
	
	public int merge(APIData source, int sourceIndex, List<Indexed<String>> byRefs) {
		this.mergeInputs(source, sourceIndex, byRefs);
		int r = this.mergeOutputs(source, sourceIndex, byRefs);
		this.globals.addAll(source.globals);
		return r;
	}
	
	private List<String> getIO(Set<String> source) {
		if (source == null) {
			return Collections.emptyList();
		} else {
			List<String> result = new ArrayList<String>(source.size());
			for (String n : source) {
				//if (this.subscripted.contains(n)) {
				//	result.add(n + "*");
				//} else {
					result.add(n);
				//}
			}
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
