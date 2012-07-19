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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.raygroupintl.struct.Indexed;

public class APIData {
	private final static Logger LOGGER = Logger.getLogger(APIData.class.getName());

	private Block sourceBlock;
	
	private Map<String, Integer> inputs;
	private Map<String, Integer> outputs;
	private Set<String> globals;
		
	public APIData(Block source) {
		this.sourceBlock = source;
	}
		
	public void set(Map<String, Integer> inputs, Map<String, Integer> outputs, Set<String> globals) {
		this.inputs = new HashMap<String, Integer>(inputs);
		this.outputs = new HashMap<String, Integer>(outputs);
		this.globals = new HashSet<String>(globals);
	}
	
	public Block getSourceBlock() {
		return this.sourceBlock;
	}
	
	private boolean mergeInput(Block thisBlock, Block sourceBlock, String name, int sourceIndex) {
		if (! thisBlock.isNewed(name, sourceIndex)) {
			Integer outputIndex = this.outputs.get(name);
			if ((outputIndex == null) || (outputIndex.intValue() >= sourceIndex)) {			
				Integer inputIndex = this.inputs.get(name);
				if ((inputIndex == null) || (inputIndex.intValue() > sourceIndex)) {						
					this.inputs.put(name, sourceIndex);
					return true;
				}
			}
		}
		return false;
	}
	
	public int mergeInputs(APIData source, int sourceIndex, List<Indexed<String>> byRefs) {
		Block thisBlock = this.getSourceBlock();
		Block sourceBlock = source.getSourceBlock();
		int result = 0;
		for (String name : source.inputs.keySet()) {
			Integer formalIndex = sourceBlock.getAsFormal(name);
			if (formalIndex == null) {	
				boolean b = this.mergeInput(thisBlock, sourceBlock, name, sourceIndex);
				if (b) ++ result;
			} else if (byRefs!= null) {
				int index = formalIndex.intValue();
				for (Indexed<String> byRef : byRefs) {
					if (index == byRef.getIndex()) {
						String byRefName = byRef.getObject();
						boolean b = this.mergeInput(thisBlock, sourceBlock, byRefName, sourceIndex);
						if (b) ++result;
					}
				}				
			}
		}
		return result;
	}
	
	static int count = 0;
	
	private boolean mergeOutput(Block thisBlock, Block sourceBlock, String name, int sourceIndex) {
		if (! thisBlock.isNewed(name, sourceIndex)) {
			Integer outputIndex = this.outputs.get(name);
			if ((outputIndex == null) || (outputIndex.intValue() > sourceIndex)) {			
				
				//if (count < 30) 
					if ("DIFILEI".equals(name)) {
					LOGGER.info(sourceBlock.getEntryId().toString() + " updated " + thisBlock.getEntryId().toString() + "\n");
					++count;
				}
				this.outputs.put(name, sourceIndex);
				Integer inputIndex = this.inputs.get(name);
				if ((inputIndex != null) && (inputIndex.intValue() > sourceIndex)) {
					this.inputs.remove(name);
				}
				return true;
			}
		}
		return false;
	}

	public int mergeOutputs(APIData source, int sourceIndex, List<Indexed<String>> byRefs) {
		Block thisBlock = this.getSourceBlock();
		Block sourceBlock = source.getSourceBlock();
		int result = 0;
		for (String name : source.outputs.keySet()) {			
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
	
	public void mergeGlobals(APIData source) {
		this.globals.addAll(source.globals);		
	}
	
	
/*	public int merge(APIData source, int sourceIndex, List<Indexed<String>> byRefs) {
		int r = this.mergeOutputs(source, sourceIndex, byRefs);
		r += this.mergeInputs(source, sourceIndex, byRefs);
		this.globals.addAll(source.globals);
		return r;
	}
*/	
	private List<String> getIO(Set<String> source) {
		if (source == null) {
			return Collections.emptyList();
		} else {
			List<String> result = new ArrayList<String>(source);
			Collections.sort(result);
			return result;
		}
		
	}
	
	public List<String> getInputs() {
		return getIO(this.inputs.keySet());
	}

	public List<String> getOutputs() {
		return getIO(this.outputs.keySet());
	}

	public List<String> getGlobals() {
		return getIO(this.globals);
	}
}
