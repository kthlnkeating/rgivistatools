package com.raygroupintl.vista.tools.entryinfo;

import java.util.Set;

import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class EntryCodeInfoAccumulator extends Accumulator<EntryCodeInfo, CodeInfo> {
	private DataStore<Set<String>> store = new DataStore<Set<String>>();					
	
	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		super(blocksSupply, new ToolResultCollection<EntryCodeInfo>());
		this.blocksSupply = blocksSupply;
	}

	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		super(blocksSupply, filterFactory, new ToolResultCollection<EntryCodeInfo>());
	}
	
	@Override
	protected EntryCodeInfo getResult(Block<CodeInfo> block, Filter<EntryId> filter) {
		RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(block, blocksSupply);
		Set<String> assumedLocals = ala.get(store, filter);
		AdditiveDataAggregator<BasicCodeInfo, CodeInfo> bcia = new AdditiveDataAggregator<BasicCodeInfo, CodeInfo>(block, blocksSupply);
		BasicCodeInfo apiData = bcia.get(filter);
		return new EntryCodeInfo(block.getEntryId(), block.getAttachedObject().getFormals(), assumedLocals, apiData);		
	}
	
	@Override
	protected EntryCodeInfo getEmptyBlockResult(EntryId entryId) {
		return new EntryCodeInfo(entryId, null, null, null);		
	}
}
