package com.raygroupintl.vista.tools.entryfanin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FaninList;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.Indexed;

public class EntryFaninsAggregator {
	Block<FaninMark> block;
	BlocksSupply<Block<FaninMark>> supply;
	
	public EntryFaninsAggregator(Block<FaninMark> block, BlocksSupply<Block<FaninMark>> supply) {
		this.block = block;
		this.supply = supply;
	}
	
	private int updateFaninData(PathPieceToEntry data, Block<FaninMark> b, FanoutBlocks<Block<FaninMark>> fanoutBlocks, Map<Integer, PathPieceToEntry> datas) {
		int numChange = 0;
		FaninList<Block<FaninMark>> faninList = fanoutBlocks.getFaninList(b);
		List<Indexed<Block<FaninMark>>> faninBlocks = faninList.getFanins();
		for (Indexed<Block<FaninMark>> ib : faninBlocks) {
			Block<FaninMark> faninBlock = ib.getObject();
			int faninId = System.identityHashCode(faninBlock);
			PathPieceToEntry faninData = datas.get(faninId);
			boolean alreadyVisited = faninData.exist();
			int localChange = faninBlock.getAttachedObject().update(faninData, data);
			if ((localChange > 0) && ! alreadyVisited) {
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
			FaninMark bd = b.getAttachedObject();
			PathPieceToEntry data = bd.getLocalCopy();
			datas.put(id, data);
		}
		
		List<Block<FaninMark>> evaluatedBlocks = fanoutBlocks.getEvaludatedBlocks();
		for (Block<FaninMark> b : evaluatedBlocks) {
			PathPieceToEntry data = store.get(b);
			if (data.exist() || b.getAttachedObject().isFanin()) {
				this.updateFaninData(data, b, fanoutBlocks, datas);
			}
		}
		
		for (int i=blocks.size()-1; i>=0; --i) {
			Block<FaninMark> b = blocks.get(i);
			int id = System.identityHashCode(b);
			PathPieceToEntry data = datas.get(id);
			if ((! data.exist()) && b.getAttachedObject().isFanin()) {
				b.getAttachedObject().update(data, data);
				this.updateFaninData(data, b, fanoutBlocks, datas);
			}
		}
					
		for (Block<FaninMark> bi : blocks) {
			store.put(bi, datas);
		}
		Block<FaninMark> b = blocks.get(0);
		return store.put(b, datas);
	}
		
	public PathPieceToEntry get(DataStore<PathPieceToEntry> store, Filter<EntryId> filter) {
		PathPieceToEntry result = store.get(this.block);
		if (result == null) {
			FanoutBlocks<Block<FaninMark>> fanoutBlocks = this.block.getFanoutBlocks(this.supply, store, filter);
			result = this.get(fanoutBlocks, store);
		}
		//Set<Integer> localEntries = new HashSet<Integer>();
		//this.block.updateLocals(localEntries, filter);
		//Set<EntryId> tobeRemoved = new HashSet<EntryId>();
		//for (Integer id : localEntries) {
		//	PathPieceToEntry lp = store.get(id);
		//	if (lp != null) {
		//		result.copy(lp);
		//		tobeRemoved.add(lp.getStartEntry());
		//	}
		//	store.remove(id);
		//}
		//result.remove(tobeRemoved);		
		return result;
	}
}
