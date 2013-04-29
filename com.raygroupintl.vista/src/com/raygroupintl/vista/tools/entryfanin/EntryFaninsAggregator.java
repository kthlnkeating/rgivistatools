package com.raygroupintl.vista.tools.entryfanin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FaninList;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.Indexed;

public class EntryFaninsAggregator {
	private Block<FaninMark> block;
	private BlocksSupply<Block<FaninMark>> supply;
	private boolean filterInternalBlocks;
	
	public EntryFaninsAggregator(Block<FaninMark> block, BlocksSupply<Block<FaninMark>> supply, boolean filterInternalBlocks) {
		this.block = block;
		this.supply = supply;
		this.filterInternalBlocks = filterInternalBlocks;
	}
	
	private int updateFaninData(PathPieceToEntry data, Block<FaninMark> b, FanoutBlocks<Block<FaninMark>> fanoutBlocks, Map<Integer, PathPieceToEntry> datas) {
		int numChange = 0;
		FaninList<Block<FaninMark>> faninList = fanoutBlocks.getFaninList(b);
		List<Indexed<Block<FaninMark>>> faninBlocks = faninList.getFanins();
		for (Indexed<Block<FaninMark>> ib : faninBlocks) {
			Block<FaninMark> faninBlock = ib.getObject();
			int faninId = System.identityHashCode(faninBlock);
			PathPieceToEntry faninData = datas.get(faninId);
			boolean alreadyVisited = false; //faninData.exist();
			boolean useInternal = this.filterInternalBlocks && b.isInternal();
			int localChange = faninBlock.getData().getAttachedObject().update(faninData, data, useInternal);
			if ((localChange > 0) && (useInternal || ! alreadyVisited)) {
				this.updateFaninData(faninData, faninBlock, fanoutBlocks, datas);
			}
		}		
		return numChange;
	}
	
	private PathPieceToEntry get(FanoutBlocks<Block<FaninMark>> fanoutBlocks, DataStore<PathPieceToEntry> store) {			
		Map<Integer, PathPieceToEntry> datas = new HashMap<Integer, PathPieceToEntry>();

		List<Block<FaninMark>> blocks = fanoutBlocks.getBlocks();
		for (Block<FaninMark> b : blocks) {
			int id = System.identityHashCode(b);
			FaninMark bd = b.getData().getAttachedObject();
			PathPieceToEntry data = bd.getLocalCopy();
			datas.put(id, data);
		}
		
		List<Block<FaninMark>> evaluatedBlocks = fanoutBlocks.getEvaludatedBlocks();
		for (Block<FaninMark> b : evaluatedBlocks) {
			PathPieceToEntry data = store.get(b);
			if (data.exist() || b.getData().getAttachedObject().isFanin()) {
				this.updateFaninData(data, b, fanoutBlocks, datas);
			}
		}
		
		for (int i=blocks.size()-1; i>=0; --i) {
			Block<FaninMark> b = blocks.get(i);
			int id = System.identityHashCode(b);
			PathPieceToEntry data = datas.get(id);
			if ((! data.exist()) && b.getData().getAttachedObject().isFanin()) {
				b.getData().getAttachedObject().initialize(data);
				this.updateFaninData(data, b, fanoutBlocks, datas);
			}
		}
					
		for (Block<FaninMark> bi : blocks) {
			if (! (bi.isInternal() && this.filterInternalBlocks)) {
				store.put(bi, datas);
			}
		}
		Block<FaninMark> b = blocks.get(0);
		return store.put(b, datas);
	}
		
	public PathPieceToEntry get(DataStore<PathPieceToEntry> store, Filter<EntryId> filter) {
		PathPieceToEntry result = store.get(this.block);
		if (result == null) {
			Set<EntryId> missing = new HashSet<EntryId>();
			FanoutBlocks<Block<FaninMark>> fanoutBlocks = this.block.getFanoutBlocks(this.supply, store, filter, missing);
			result = this.get(fanoutBlocks, store);
		}
		return result;
	}
}
