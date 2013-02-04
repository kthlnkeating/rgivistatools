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

public class EntryCodeInfoAccumulator extends Accumulator<EntryCodeInfo> {
	private BlocksSupply<Block<CodeInfo>> blocksSupply;
	private DataStore<Set<String>> store = new DataStore<Set<String>>();					
	
	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		super(new ToolResultCollection<EntryCodeInfo>());
		this.blocksSupply = blocksSupply;
	}

	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		super(filterFactory, new ToolResultCollection<EntryCodeInfo>());
		this.blocksSupply = blocksSupply;
	}
	
	@Override
	public EntryCodeInfo getResult(EntryId entryId) {
		Block<CodeInfo> b = blocksSupply.getBlock(entryId);
		if (b != null) {
			Filter<EntryId> filter = this.filterFactory.getFilter(entryId);
			RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(b, blocksSupply);
			Set<String> assumedLocals = ala.get(store, filter);
			AdditiveDataAggregator<BasicCodeInfo, CodeInfo> bcia = new AdditiveDataAggregator<BasicCodeInfo, CodeInfo>(b, blocksSupply);
			BasicCodeInfo apiData = bcia.get(filter);
			return new EntryCodeInfo(entryId, b.getAttachedObject().getFormals(), assumedLocals, apiData);
		} else {
			return new EntryCodeInfo(entryId, null, null, null);
		}		
	}
}
