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

import com.raygroupintl.m.parsetree.filter.SourcedFanoutFilter;
import com.raygroupintl.m.parsetree.visitor.APIRecorder;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.struct.Indexed;

public abstract class Block {
	private final static Logger LOGGER = Logger.getLogger(APIRecorder.class.getName());
	private static Set<EntryId> reported = new HashSet<EntryId>();

	private static class FaninList {
		private Block block;
		private List<IndexedBlock> faninBlocks = new ArrayList<IndexedBlock>();
		private Set<Integer> existing = new HashSet<Integer>();
		
		public FaninList(Block block) {
			this.block = block;
		}
				
		public void addFanin(Block faninBlock, int index, List<Indexed<String>> byRefs) {
			int faninId = System.identityHashCode(faninBlock);
			if (faninId != System.identityHashCode(this.block)) {
				if (! this.existing.contains(faninId)) {
					IndexedBlock e = new IndexedBlock(index, faninBlock, byRefs);
					this.faninBlocks.add(e);
					this.existing.add(faninId);
				}
			}
		}
		
		public List<IndexedBlock> getFaninBlocks() {
			return this.faninBlocks;
		}
	}
	
	private static class FanoutBlocks {
		private Map<Integer, FaninList> map = new HashMap<Integer, FaninList>();
		private Map<Integer, FaninList> storedMap = new HashMap<Integer, FaninList>();
		private List<Block> list = new ArrayList<Block>();
		private List<Block> storedList = new ArrayList<Block>();
		private int rootId;
		private APIDataStore store;
		
		public FanoutBlocks(Block root, APIDataStore store) {
			this.list.add(root);
			this.rootId = System.identityHashCode(root);
			FaninList faninList = new FaninList(root);
			this.map.put(this.rootId, faninList);
			this.store = store;
		}
		
		public void add(Block fanin, Block fanout, int fanoutIndex, List<Indexed<String>> byRefs) {
			Integer fanoutId = System.identityHashCode(fanout);
			APIData storedData = this.store == null ? null : this.store.get(fanout);
			if (storedData != null) {
				FaninList faninList = this.storedMap.get(fanoutId);
				if (faninList == null) {
					this.storedList.add(fanout);
					faninList = new FaninList(fanout);
					this.storedMap.put(fanoutId, faninList);						
				}					
				faninList.addFanin(fanin, fanoutIndex, byRefs);
			} else {
				FaninList faninList = this.map.get(fanoutId);
				if (faninList == null) {
					this.list.add(fanout);
					faninList = new FaninList(fanout);
					this.map.put(fanoutId, faninList);
				}
				faninList.addFanin(fanin, fanoutIndex, byRefs);
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
				APIData data = b.getInitialAccumulativeData();
				datas.put(id, data);
			}
			
			for (Block b : this.storedList) {
				int id = System.identityHashCode(b);
				APIData data = this.store.get(b);
				FaninList faninList = this.storedMap.get(id);
				List<IndexedBlock> faninBlocks = faninList.getFaninBlocks();
				for (IndexedBlock ib : faninBlocks) {
					Block faninBlock = ib.getBlock();
					int faninId = System.identityHashCode(faninBlock);
					APIData faninData = datas.get(faninId);
					faninBlock.mergeAccumulative(faninData, data, ib.getIndex());
				}
			}
			
			int totalChange = Integer.MAX_VALUE;

			while (totalChange > 0) {
				totalChange = 0;
				for (int i=this.list.size()-1; i>=0; --i) {
					Block b = this.list.get(i);
					int id = System.identityHashCode(b);
					APIData data = datas.get(id);
					FaninList faninList = this.map.get(id);
					List<IndexedBlock> faninBlocks = faninList.getFaninBlocks();
					for (IndexedBlock ib : faninBlocks) {
						Block faninBlock = ib.getBlock();
						int faninId = System.identityHashCode(faninBlock);
						APIData faninData = datas.get(faninId);
						totalChange += faninBlock.mergeAccumulative(faninData, data, ib.getIndex());
					}
				}
			}
						
			Block b = this.list.get(0);
			int id = System.identityHashCode(b);
			APIData result = datas.get(id);
		
			for (Block bi : this.list) {
				int idi = System.identityHashCode(bi);
				APIData data = datas.get(idi);
				store.put(data);
			}
			store.put(result);
						
			return result;
		}
		
		public List<Block> getAllBlocks() {
			List<Block> result = new ArrayList<Block>(this.list);
			result.addAll(this.storedList);
			return result;
		}
		
	}
		
	private int index;
	private EntryId entryId;
	private Blocks siblings;
	
	LineLocation location;
	
	private List<IndexedFanout> fanouts = new ArrayList<IndexedFanout>();
	private boolean closed;
	
	public Block(int index, EntryId entryId, Blocks siblings) {
		this.index = index;
		this.entryId = entryId;
		this.siblings = siblings;
	}
	
	public void setLineLocation(LineLocation location) {
		this.location = location;
	}
	
	public LineLocation getLineLocation() {
		return this.location;
	}
	
	public String getLocationTitle() {
		String result = this.entryId.getRoutineName();
		result += "^" + this.location.getTag();
		result += "^" + this.location.getOffset();
		return result;
	}
	
	public void close() {
		this.closed = true;
	}
	
	public boolean isClosed() {
		return this.closed;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public EntryId getEntryId() {
		return this.entryId;
	}
	
	public void addFanout(int index, EntryId fanout, CallArgument[] arguments) {
		if (! this.closed) {
			IndexedFanout ifo = new IndexedFanout(index, fanout);			
			ifo.setByRefs(arguments);
			this.fanouts.add(ifo);
		}
	}	
	
	public void update(FanoutBlocks fanoutBlocks, BlocksSupply blocksSupply, SourcedFanoutFilter filter, Map<String, String> replacedRoutines) {
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
							LOGGER.log(Level.WARNING, "Unable to find information about tag " + tagName + " in " + this.entryId.getRoutineName());
							reported.add(fout);
						}
						continue;
					}
				}
				fanoutBlocks.add(this, tagBlock, ifout.getIndex(), ifout.getByRefs());
			} else {
				Blocks routineBlocks = blocksSupply.getBlocks(routineName);
				String originalTagName = tagName;
				if (routineBlocks == null) {
					if (replacedRoutines != null) {
						String replacement = replacedRoutines.get(routineName);
						if (replacement != null) {
							routineBlocks = blocksSupply.getBlocks(replacement);
							if (tagName.equals(routineName)) {
								tagName = replacement;
							}
						}
					}
					if (routineBlocks == null) {					
						if (! reported.contains(fout)) {
							LOGGER.log(Level.WARNING, "Unable to find information about routine " + routineName + ".");
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
							LOGGER.log(Level.WARNING, "Unable to find information about tag " + fout.toString() + " in " + routineName);
							reported.add(fout);
						}
						continue;
					}
				}
				fanoutBlocks.add(this, tagBlock, ifout.getIndex(), ifout.getByRefs());
			}				 
		}
	}
	
	private APIData auxGetAPIData(BlocksSupply blocksSupply, APIDataStore store, SourcedFanoutFilter filter, Map<String, String> replacedRoutines) {
		APIData result = store.get(this);
		if (result != null) {
			return result;
		}		
		FanoutBlocks fanoutBlocks = new FanoutBlocks(this, store);
		int index = 0;	
		while (index < fanoutBlocks.getSize()) {
			Block b = fanoutBlocks.getBlock(index);
			b.update(fanoutBlocks, blocksSupply, filter, replacedRoutines);
			++index;
		}
		return fanoutBlocks.getAPI();
	}
	
	public APIData getAPIData(BlocksSupply blocksSupply, APIDataStore store, SourcedFanoutFilter filter, Map<String, String> replacedRoutines) {
		if (filter != null) filter.setSource(this.entryId);
		APIData forAssumeds = this.auxGetAPIData(blocksSupply, store, filter, replacedRoutines);
		APIData result = this.getInitialAdditiveData();
		this.mergeAccumulativeToAdditive(result,  forAssumeds);
		FanoutBlocks fanoutBlocks = new FanoutBlocks(this, null);
		int index = 0;	
		while (index < fanoutBlocks.getSize()) {
			Block b = fanoutBlocks.getBlock(index);
			b.update(fanoutBlocks, blocksSupply, filter, replacedRoutines);
			++index;
		}
		List<Block> blocks = fanoutBlocks.getAllBlocks();
		for (Block b : blocks) {
			b.mergeAdditiveTo(result);
		}
		return result;		
	}
	
	public abstract APIData getInitialAccumulativeData();
		
	public abstract APIData getInitialAdditiveData();
		
	public abstract void mergeAccumulativeToAdditive(APIData target, APIData source);

	public abstract int mergeAccumulative(APIData target, APIData source, int sourceIndex);
	
	public abstract void mergeAdditiveTo(APIData target);
}
