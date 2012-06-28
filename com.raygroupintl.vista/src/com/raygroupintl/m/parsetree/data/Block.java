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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.visitor.APIRecorder;

public class Block {
	private final static Logger LOGGER = Logger.getLogger(APIRecorder.class.getName());

	private int index;
	private EntryId entryId;
	private Blocks siblings;
	
	private String[] formals;
	private Map<String, Integer> newedLocals = new HashMap<String, Integer>();
	private Map<String, Integer> inputLocals = new HashMap<String, Integer>();
	private Map<String, Integer> outputLocals = new HashMap<String, Integer>();

	private List<IndexedFanout> fanouts = new ArrayList<IndexedFanout>();
	private boolean closed;
	
	public Block(int index, EntryId entryId, Blocks siblings) {
		this.index = index;
		this.entryId = entryId;
		this.siblings = siblings;
	}
	
	public void close() {
		this.closed = true;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public void setFormals(String[] formals) {
		this.formals = formals;		
		if (formals != null) for (String formal : formals) {
			this.newedLocals.put(formal, this.index);
		}
	}
	
	public String[] getFormals() {
		return this.formals;
	}
	
	public void addNewed(int index, Local local) {
		if (! this.closed) {
			String label = local.getName().toString();
			if (! this.newedLocals.containsKey(label)) {
				this.newedLocals.put(label, index);
			}
		}
	}		
	
	public void addInput(int index, Local local) {
		if (! this.closed) {
			String label = local.getName().toString();
			if ((! this.inputLocals.containsKey(label)) && (! this.newedLocals.containsKey(label))) {
				this.inputLocals.put(label, index);
			}
		}
	}

	public void addOutput(int index, Local local) {
		if (! this.closed) {
			String label = local.getName().toString();
			if ((! this.outputLocals.containsKey(label)) && (! this.newedLocals.containsKey(label))) {
				this.outputLocals.put(label, index);
			}
		}
	}

	public void addFanout(int index, EntryId fanout, boolean shouldClose) {
		if (! this.closed) {
			IndexedFanout ifo = new IndexedFanout(index, fanout);
			this.fanouts.add(ifo);
			if (shouldClose) {
				this.close();
			}
		}
	}	
	
	public boolean isNewed(Local local) {
		return this.newedLocals.containsKey(local.getName().toString());
	}
	
	public boolean isInput(Local local) {
		return this.inputLocals.containsKey(local.getName().toString());
	}
	
	public boolean isOutput(Local local) {
		return this.outputLocals.containsKey(local.getName().toString());
	}
	
	public boolean isUsed(Local local) {
		String name = local.getName().toString();
		return this.inputLocals.containsKey(name) || this.outputLocals.containsKey(name);
	}
	
	public APIData getAPIData(Map<String, Blocks> overallMap, Set<EntryId> alreadyVisited) {
		if (alreadyVisited.contains(this.entryId)) return null;
		APIData result = new APIData(this.inputLocals.keySet(), this.outputLocals.keySet());
		alreadyVisited.add(this.entryId);
		for (IndexedFanout ifout : this.fanouts) {
			EntryId fout = ifout.getFanout();
			String routineName = fout.getRoutineName();
			String tagName = fout.getTag();					
			if (tagName == null) {
				tagName = routineName;
			}
			if (routineName == null) {
				Block tagBlock = this.siblings.get(tagName);
				if (tagBlock == null) {
					LOGGER.log(Level.SEVERE, "Unable to find information about tag " + tagName + " in " + this.entryId.getRoutineName());
					continue;
				}
				APIData blockUsed = tagBlock.getAPIData(overallMap, alreadyVisited);
				if (blockUsed != null) result.merge(blockUsed, ifout.getIndex(), this.newedLocals);
			} else {
				Blocks routineBlocks = overallMap.get(routineName);
				if (routineBlocks == null) {
					LOGGER.log(Level.SEVERE, "Unable to find information about routine " + routineName);
					continue;
				}
				Block tagBlock = routineBlocks.get(tagName);
				if (tagBlock == null) {
					LOGGER.log(Level.SEVERE, "Unable to find information about tag " + fout.toString() + " in " + routineName);
					continue;
				}
				APIData blockUsed = tagBlock.getAPIData(overallMap, alreadyVisited);
				if (blockUsed != null) result.merge(blockUsed, ifout.getIndex(), this.newedLocals);
			}				 
		}
		return result;
	}
}
