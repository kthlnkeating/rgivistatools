package com.raygroupintl.vista.tools.entryinfo;

import java.util.List;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.tools.entryfanout.EntryFanouts;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class FanoutAccumulator extends Accumulator<EntryFanouts, Void>{
	public FanoutAccumulator(BlocksSupply<Block<Void>> blocksSupply) {
		super(blocksSupply, new ToolResultCollection<EntryFanouts>());
	}

	public FanoutAccumulator(BlocksSupply<Block<Void>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		super(blocksSupply, filterFactory, new ToolResultCollection<EntryFanouts>());
	}
	
	@Override
	protected EntryFanouts getResult(Block<Void> block, Filter<EntryId> filter) {
		FanoutBlocks<Block<Void>> fanoutBlocks = block.getFanoutBlocks(this.blocksSupply, filter);
		List<Block<Void>> blocks = fanoutBlocks.getBlocks();
		boolean first = true;
		EntryFanouts result = new EntryFanouts(block.getEntryId());
		for (Block<Void> b : blocks) {
			if (first) {
				first = false;
			} else if (! b.isInternal()) {					
				result.add(b.getEntryId());
			}
		}
		return result;
	}
	
	@Override
	protected EntryFanouts getEmptyBlockResult(EntryId entryId) {
		EntryFanouts result = new EntryFanouts(entryId);
		result.setErrorMsg("Not Found");
		return result;
	}
}
