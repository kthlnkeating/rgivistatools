package com.raygroupintl.vista.tools.entrycodeinfo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.struct.ConstFilterFactory;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.struct.PassFilter;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class EntryCodeInfoAccumulator {
	private BlocksSupply<Block<CodeInfo>> blocksSupply;
	private DataStore<Set<String>> store = new DataStore<Set<String>>();					
	private FilterFactory<EntryId, EntryId> filterFactory = new ConstFilterFactory<EntryId, EntryId>(new PassFilter<EntryId>());
	private ToolResultCollection<EntryCodeInfo> results = new ToolResultCollection<EntryCodeInfo>();
	
	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		this.blocksSupply = blocksSupply;
	}
	
	public void setFilterFactory(FilterFactory<EntryId, EntryId> filterFactory) {
		this.filterFactory = filterFactory;
	}
	
	public EntryCodeInfo findForEntry(EntryId entryId) {
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
	
	public void addEntry(EntryId entryId) {
		EntryCodeInfo e = this.findForEntry(entryId);
		this.results.add(e);
	}
	
	public void addRoutine(Routine routine) {
		List<EntryId> routineEntryTags = routine.getEntryIdList();
		for (EntryId routineEntryTag : routineEntryTags) {
			this.addEntry(routineEntryTag);
		}		
	}
	
	public void addRoutines(Collection<Routine> routines) {
		for (Routine routine : routines) {
			this.addRoutine(routine);
		}
	}
		
	public ToolResultCollection<EntryCodeInfo> getResult() {
		return this.results;
	}
	
	public EntryCodeInfo getLastResult() {
		return this.results.getLast();
	}
}
