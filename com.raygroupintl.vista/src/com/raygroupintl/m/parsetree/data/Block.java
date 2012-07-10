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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.visitor.APIRecorder;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.Indexed;

public class Block {
	private final static Logger LOGGER = Logger.getLogger(APIRecorder.class.getName());
	private static Set<EntryId> reported = new HashSet<EntryId>();

	private static class FaninList {
		private Block block;
		private List<Indexed<Block>> faninBlocks = new ArrayList<Indexed<Block>>();
		private Set<Integer> existing = new HashSet<Integer>();
		
		public FaninList(Block block) {
			this.block = block;
		}
				
		public void addFanin(Block faninBlock, int index) {
			int faninId = System.identityHashCode(faninBlock);
			if (faninId != System.identityHashCode(this.block)) {
				if (! this.existing.contains(faninId)) {
					Indexed<Block> e = new Indexed<Block>(faninBlock, index);
					this.faninBlocks.add(e);
					this.existing.add(faninId);
				}
			}
		}
		
		public List<Indexed<Block>> getFaninBlocks() {
			return this.faninBlocks;
		}
	}
	
	private static class FanoutBlocks {
		private Map<Integer, FaninList> map = new HashMap<Integer, FaninList>();
		private List<Block> list = new ArrayList<Block>();
		private int rootId;
		
		public FanoutBlocks(Block root) {
			this.list.add(root);
			this.rootId = System.identityHashCode(root);
		}
		
		public void add(Block fanin, Block fanout, int fanoutIndex) {
			Integer fanoutId = System.identityHashCode(fanout);
			if (fanoutId != this.rootId) {
				FaninList faninList = this.map.get(fanoutId);
				if (faninList == null) {
					this.list.add(fanout);
					faninList = new FaninList(fanout);
					this.map.put(fanoutId, faninList);
				}
				faninList.addFanin(fanin, fanoutIndex);
			}
		}
		
		public Block getBlock(int index) {
			if (index < this.list.size()) {
				return this.list.get(index);
			} else {
				return null;
			}
		}
		
		public int getSize() {
			return this.list.size();
		}
		
		public APIData getAPI() {
			Map<Integer, APIData> datas = new HashMap<Integer, APIData>();
			for (Block b : this.list) {
				int id = System.identityHashCode(b);
				APIData data = b.toAPIData();
				datas.put(id, data);
			}
			
			for (int i=this.list.size()-1; i>0; --i) {
				Block b = this.list.get(i);
				int id = System.identityHashCode(b);
				APIData data = datas.get(id);
				FaninList faninList = this.map.get(id);
				List<Indexed<Block>> faninBlocks = faninList.getFaninBlocks();
				for (Indexed<Block> ib : faninBlocks) {
					Block faninBlock = ib.getObject();
					int faninId = System.identityHashCode(faninBlock);
					APIData faninData = datas.get(faninId);
					faninData.merge(data, ib.getIndex());
				}
			}
			
			Block b = this.list.get(0);
			int id = System.identityHashCode(b);
			return datas.get(id);
		}		
	}
		
	private int index;
	private EntryId entryId;
	private Blocks siblings;
	
	private String[] formals;
	private Map<String, Integer> newedLocals = new HashMap<String, Integer>();
	private Map<String, Integer> inputLocals = new HashMap<String, Integer>();
	private Map<String, Integer> outputLocals = new HashMap<String, Integer>();
	private Set<String> globals = new HashSet<String>();

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
	
	public EntryId getEntryId() {
		return this.entryId;
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

	public void addGlobal(String value) {
		if (! this.closed) {
			this.globals.add(value);
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
	
	public Map<String, Integer> getNewedLocals() {
		return this.newedLocals;
	}
	
	public APIData toAPIData() {
		APIData result = new APIData(this);
		result.set(this.inputLocals.keySet(), this.outputLocals.keySet(), this.globals);
		return result;
	}
	
	public void update(FanoutBlocks fanoutBlocks, BlocksSupply overallMap, Filter<EntryId> filter, Map<String, String> replacedRoutines) {
		for (IndexedFanout ifout : this.fanouts) {
			EntryId fout = ifout.getFanout();
			if ((filter != null) && (! filter.isValid(fout))) continue;
			String routineName = fout.getRoutineName();
			String tagName = fout.getTag();					
			if (tagName == null) {
				tagName = routineName;
			}
			if (routineName == null) {
				Block tagBlock = this.siblings.get(tagName);
				if (tagBlock == null) {
					String inRoutineName = this.entryId.getRoutineName();
					if (inRoutineName != null) {
						String replacement = replacedRoutines.get(tagName);
						if ((replacement != null) && (replacement.equals(inRoutineName))) {
							tagBlock = this.siblings.get(replacement);
						}
					}
					if (tagBlock == null) {
						if (! reported.contains(fout)) {						
							LOGGER.log(Level.SEVERE, "Unable to find information about tag " + tagName + " in " + this.entryId.getRoutineName());
							reported.add(fout);
						}
						continue;
					}
				}
				fanoutBlocks.add(this, tagBlock, ifout.getIndex());
			} else {
				Blocks routineBlocks = overallMap.getBlocks(routineName);
				String originalTagName = tagName;
				if (routineBlocks == null) {
					if (replacedRoutines != null) {
						String replacement = replacedRoutines.get(routineName);
						if (replacement != null) {
							routineBlocks = overallMap.getBlocks(replacement);
							if (tagName.equals(routineName)) {
								tagName = replacement;
							}
						}
					}
					if (routineBlocks == null) {					
						if (! reported.contains(fout)) {
							LOGGER.log(Level.SEVERE, "Unable to find information about routine " + routineName);
							reported.add(fout);
						}
						continue;
					}
				}
				Block tagBlock = routineBlocks.get(tagName);
				if (tagBlock == null) {
					if (! originalTagName.equals(tagName)) {
						tagBlock = routineBlocks.get(originalTagName);
					}
					if (tagBlock == null) {
						if (! reported.contains(fout)) {
							LOGGER.log(Level.SEVERE, "Unable to find information about tag " + fout.toString() + " in " + routineName);
							reported.add(fout);
						}
						continue;
					}
				}
				fanoutBlocks.add(this, tagBlock, ifout.getIndex());
			}				 
		}
	}
	
	public APIData getAPIData(BlocksSupply blocksSupply, Filter<EntryId> filter, Map<String, String> replacedRoutines) {
		FanoutBlocks fanoutBlocks = new FanoutBlocks(this);
		int index = 0;	
		while (index < fanoutBlocks.getSize()) {
			Block b = fanoutBlocks.getBlock(index);
			b.update(fanoutBlocks, blocksSupply, filter, replacedRoutines);
			++index;
		}
		return fanoutBlocks.getAPI();
	}

		
	public APIData getAPIData(BlocksSupply overallMap, Set<EntryId> alreadyVisitedIn, Map<String, String> replacedRoutines) {
		if (alreadyVisitedIn.contains(this.entryId)) return null;
		APIData result = this.toAPIData();
		Set<EntryId> alreadyVisited = new HashSet<EntryId>(alreadyVisitedIn);
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
					String inRoutineName = this.entryId.getRoutineName();
					if (inRoutineName != null) {
						String replacement = replacedRoutines.get(tagName);
						if ((replacement != null) && (replacement.equals(inRoutineName))) {
							tagBlock = this.siblings.get(replacement);
						}
					}
					if (tagBlock == null) {
						if (! reported.contains(fout)) {						
							LOGGER.log(Level.SEVERE, "Unable to find information about tag " + tagName + " in " + this.entryId.getRoutineName());
							reported.add(fout);
						}
						continue;
					}
				}
				APIData blockUsed = tagBlock.getAPIData(overallMap, alreadyVisited, replacedRoutines);
				if (blockUsed != null) result.merge(blockUsed, ifout.getIndex());
			} else {
				Blocks routineBlocks = overallMap.getBlocks(routineName);
				String originalTagName = tagName;
				if (routineBlocks == null) {
					if (replacedRoutines != null) {
						String replacement = replacedRoutines.get(routineName);
						if (replacement != null) {
							routineBlocks = overallMap.getBlocks(replacement);
							if (tagName.equals(routineName)) {
								tagName = replacement;
							}
						}
					}
					if (routineBlocks == null) {					
						if (! reported.contains(fout)) {
							LOGGER.log(Level.SEVERE, "Unable to find information about routine " + routineName);
							reported.add(fout);
						}
						continue;
					}
				}
				Block tagBlock = routineBlocks.get(tagName);
				if (tagBlock == null) {
					if (! originalTagName.equals(tagName)) {
						tagBlock = routineBlocks.get(originalTagName);
					}
					if (tagBlock == null) {
						if (! reported.contains(fout)) {
							LOGGER.log(Level.SEVERE, "Unable to find information about tag " + fout.toString() + " in " + routineName);
							reported.add(fout);
						}
						continue;
					}
				}
				APIData blockUsed = tagBlock.getAPIData(overallMap, alreadyVisited, replacedRoutines);
				if (blockUsed != null) result.merge(blockUsed, ifout.getIndex());
			}				 
		}
		return result;
	}
}
