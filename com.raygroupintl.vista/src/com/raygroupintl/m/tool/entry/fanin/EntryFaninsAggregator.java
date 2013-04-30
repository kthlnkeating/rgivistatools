package com.raygroupintl.m.tool.entry.fanin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FaninList;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
import com.raygroupintl.m.parsetree.data.IndexedFanout;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.ObjectWithProperty;

public class EntryFaninsAggregator {
	private Block<IndexedFanout, FaninMark> block;
	private BlocksSupply<Block<IndexedFanout, FaninMark>> supply;
	private boolean filterInternalBlocks;
	
	public EntryFaninsAggregator(Block<IndexedFanout, FaninMark> block, BlocksSupply<Block<IndexedFanout, FaninMark>> supply, boolean filterInternalBlocks) {
		this.block = block;
		this.supply = supply;
		this.filterInternalBlocks = filterInternalBlocks;
	}
	
	private int updateFaninData(PathPieceToEntry data, Block<IndexedFanout, FaninMark> b, FanoutBlocks<Block<IndexedFanout, FaninMark>, IndexedFanout> fanoutBlocks, Map<Integer, PathPieceToEntry> datas) {
		int numChange = 0;
		FaninList<Block<IndexedFanout, FaninMark>, IndexedFanout> faninList = fanoutBlocks.getFaninList(b);
		List<ObjectWithProperty<Block<IndexedFanout, FaninMark>, IndexedFanout>> faninBlocks = faninList.getFanins();
		for (ObjectWithProperty<Block<IndexedFanout, FaninMark>, IndexedFanout> ib : faninBlocks) {
			Block<IndexedFanout, FaninMark> faninBlock = ib.getObject();
			int faninId = System.identityHashCode(faninBlock);
			PathPieceToEntry faninData = datas.get(faninId);
			boolean alreadyVisited = false; //faninData.exist();
			boolean useInternal = this.filterInternalBlocks && b.isInternal();
			int localChange = faninBlock.getData().update(faninData, data, useInternal);
			if ((localChange > 0) && (useInternal || ! alreadyVisited)) {
				this.updateFaninData(faninData, faninBlock, fanoutBlocks, datas);
			}
		}		
		return numChange;
	}
	
	private PathPieceToEntry get(FanoutBlocks<Block<IndexedFanout, FaninMark>, IndexedFanout> fanoutBlocks, DataStore<PathPieceToEntry> store) {			
		Map<Integer, PathPieceToEntry> datas = new HashMap<Integer, PathPieceToEntry>();

		List<Block<IndexedFanout, FaninMark>> blocks = fanoutBlocks.getBlocks();
		for (Block<IndexedFanout, FaninMark> b : blocks) {
			int id = System.identityHashCode(b);
			FaninMark bd = b.getData();
			PathPieceToEntry data = bd.getLocalCopy();
			datas.put(id, data);
		}
		
		List<Block<IndexedFanout, FaninMark>> evaluatedBlocks = fanoutBlocks.getEvaludatedBlocks();
		for (Block<IndexedFanout, FaninMark> b : evaluatedBlocks) {
			PathPieceToEntry data = store.get(b);
			if (data.exist() || b.getData().isFanin()) {
				this.updateFaninData(data, b, fanoutBlocks, datas);
			}
		}
		
		for (int i=blocks.size()-1; i>=0; --i) {
			Block<IndexedFanout, FaninMark> b = blocks.get(i);
			int id = System.identityHashCode(b);
			PathPieceToEntry data = datas.get(id);
			if ((! data.exist()) && b.getData().isFanin()) {
				b.getData().initialize(data);
				this.updateFaninData(data, b, fanoutBlocks, datas);
			}
		}
					
		for (Block<IndexedFanout, FaninMark> bi : blocks) {
			if (! (bi.isInternal() && this.filterInternalBlocks)) {
				store.put(bi, datas);
			}
		}
		Block<IndexedFanout, FaninMark> b = blocks.get(0);
		return store.put(b, datas);
	}
		
	public PathPieceToEntry get(DataStore<PathPieceToEntry> store, Filter<EntryId> filter) {
		PathPieceToEntry result = store.get(this.block);
		if (result == null) {
			Set<EntryId> missing = new HashSet<EntryId>();
			FanoutBlocks<Block<IndexedFanout, FaninMark>, IndexedFanout> fanoutBlocks = this.block.getFanoutBlocks(this.supply, store, filter, missing);
			result = this.get(fanoutBlocks, store);
		}
		return result;
	}
}
