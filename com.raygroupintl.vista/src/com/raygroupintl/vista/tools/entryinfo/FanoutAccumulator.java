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

public class FanoutAccumulator extends Accumulator<EntryFanouts>{
	private BlocksSupply<Block<Void>> blocksSupply;
	
	public FanoutAccumulator(BlocksSupply<Block<Void>> blocksSupply) {
		super(new ToolResultCollection<EntryFanouts>());
		this.blocksSupply = blocksSupply;
	}

	public FanoutAccumulator(BlocksSupply<Block<Void>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		super(filterFactory, new ToolResultCollection<EntryFanouts>());
		this.blocksSupply = blocksSupply;
	}
	
	@Override
	public EntryFanouts getResult(EntryId entryId) {
		Block<Void> topBlock = this.blocksSupply.getBlock(entryId);
		EntryFanouts result = new EntryFanouts(entryId);
		if (topBlock != null) {
			Filter<EntryId> filter = this.filterFactory.getFilter(entryId);
			FanoutBlocks<Block<Void>> fanoutBlocks = topBlock.getFanoutBlocks(this.blocksSupply, filter);
			List<Block<Void>> blocks = fanoutBlocks.getBlocks();
			boolean first = true;
			for (Block<Void> b : blocks) {
				if (first) {
					first = false;
				} else if (! b.isInternal()) {					
					result.add(b.getEntryId());
				}
			}
		} else {
			result.setErrorMsg("Not Found");
		}		
		return result;
	}
}
