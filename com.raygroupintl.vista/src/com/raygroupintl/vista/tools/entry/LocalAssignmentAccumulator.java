package com.raygroupintl.vista.tools.entry;

import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.tools.entryinfo.Accumulator;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class LocalAssignmentAccumulator extends Accumulator<EntryCodeLocations, CodeLocations> {
	public LocalAssignmentAccumulator(BlocksSupply<Block<CodeLocations>> blocksSupply) {
		super(blocksSupply, new ToolResultCollection<EntryCodeLocations>());
	}

	public LocalAssignmentAccumulator(BlocksSupply<Block<CodeLocations>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		super(blocksSupply, filterFactory, new ToolResultCollection<EntryCodeLocations>());
	}
	
	@Override
	protected EntryCodeLocations getResult(Block<CodeLocations> block, Filter<EntryId> filter) {
		EntryId entryId = block.getEntryId();
		AdditiveDataAggregator<CodeLocations, CodeLocations> bcia = new AdditiveDataAggregator<CodeLocations, CodeLocations>(block, blocksSupply);
		CodeLocations codeLocations = bcia.get(filter);
		return new EntryCodeLocations(entryId, codeLocations);
	}
	
	@Override
	protected EntryCodeLocations getEmptyBlockResult(EntryId entryId) {
		return new EntryCodeLocations(entryId);
	}
}
