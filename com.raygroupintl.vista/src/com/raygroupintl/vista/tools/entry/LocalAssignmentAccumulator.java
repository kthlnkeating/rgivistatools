package com.raygroupintl.vista.tools.entry;

import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.tools.entryinfo.Accumulator;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class LocalAssignmentAccumulator extends Accumulator<EntryCodeLocations> {
	private BlocksSupply<Block<CodeLocations>> blocksSupply;
	
	public LocalAssignmentAccumulator(BlocksSupply<Block<CodeLocations>> blocksSupply) {
		super(new ToolResultCollection<EntryCodeLocations>());
		this.blocksSupply = blocksSupply;
	}

	public LocalAssignmentAccumulator(BlocksSupply<Block<CodeLocations>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		super(filterFactory, new ToolResultCollection<EntryCodeLocations>());
		this.blocksSupply = blocksSupply;
	}
	
	@Override
	public EntryCodeLocations getResult(EntryId entryId) {
		Block<CodeLocations> b = blocksSupply.getBlock(entryId);
		if (b != null) {
			Filter<EntryId> filter = this.filterFactory.getFilter(entryId);
			AdditiveDataAggregator<CodeLocations, CodeLocations> bcia = new AdditiveDataAggregator<CodeLocations, CodeLocations>(b, blocksSupply);
			CodeLocations codeLocations = bcia.get(filter);
			return new EntryCodeLocations(entryId, codeLocations);
		} else {
			return new EntryCodeLocations(entryId);
		}		
	}
}
