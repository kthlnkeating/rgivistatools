package com.raygroupintl.vista.tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.tools.entryfanout.EntryFanoutAccumulator;
import com.raygroupintl.vista.tools.entryfanout.EntryFanouts;
import com.raygroupintl.vista.tools.fnds.ToolResult;
import com.raygroupintl.vista.tools.returntype.ReturnType;
import com.raygroupintl.vista.tools.returntype.ReturnTypeAccumulator;
import com.raygroupintl.vista.tools.returntype.ReturnTypeBR;

public class ReturnTypeTool extends Tool {

	private Map<String,Routine> ptMemCache = new HashMap<String,Routine>();

	
	protected ReturnTypeTool(CLIParams params) {
		super(params);
	}

	//this class should only be responsible for handling invoking the returntype scan for the entries passed in via a param
	
	
	private void cacheRoutines(Collection<EntryId> entries) {
		for (EntryId eid: entries) {
			if (eid.getRoutineName() == null || ptMemCache.get(eid.getRoutineName()) != null)
				continue;
			
			VistaPackage vp = getRepositoryInfo().getPackageFromRoutineName(eid.getRoutineName());
			ptMemCache.put(
					eid.getRoutineName(), //key: routine name
					vp.getRoutineFactory().getRoutineNode(vp.getPathForRoutine(eid.getRoutineName())) //value: routine parse tree
					);
		}
	}
	
	/**
	 * Returns a subset of the cache. For every valid
	 * @param keySet
	 * @return
	 */
	private Set<Routine> getSubset(Collection<String> keySet) {
		Set<Routine> subset = new HashSet<Routine>();
		for (String key : keySet) {
			subset.add(ptMemCache.get(key));
		}
		return subset;
	}
	
	
	@Override
	public void run() {
		FileWrapper fr = getOutputFile();
		RepositoryInfo ri = getRepositoryInfo();
		List<EntryId> entries = getEntries();
		if (fr == null || ri == null || entries == null)
			return;
		
		//load the routine parse trees for each entry param into a map String<routineName>,parsetree
		cacheRoutines(entries);

		
		EntryFanoutAccumulator efa = new EntryFanoutAccumulator();
		for (EntryId eid : entries) {
			efa.addRoutine(ptMemCache.get(eid.getRoutineName())); //add all the routines that are user params, probably just 1
		}
		Map<EntryId, EntryFanouts> entryFanoutsMap = efa.getResult(); //pull out multiple sets of fanouts, possible to containing dups. <EntryId><EntryFanouts> - key: entryTag in param routine, value: contains [same entryTag, fanouts for that entry tag]
		Set<EntryId> allFanouts = new HashSet<EntryId>();
		for (EntryId eidKey: entryFanoutsMap.keySet()) { //add all fanouts to 'allFanouts', but also make sure to replace empty routine names with actual routine names since there won't be any context to derive the routine name in this data struture
			EntryFanouts entryFanouts = entryFanoutsMap.get(eidKey);
			for (EntryId fanout : entryFanouts.getFanouts()) {
				if (fanout.getRoutineName() == null) {
					allFanouts.add(new EntryId(entryFanouts.getEntry().getRoutineName(), fanout.getTag()));
				} else
					allFanouts.add(fanout);
			}
		}
		
		cacheRoutines(allFanouts); //add the fanout routines to the cache so they can be parsed in memory
		
		//use BlocksInMap.getInstance and pass in an array of routines and pass the custom retern type as the parm
		Set<String> fanoutRoutineNames = new HashSet<String>();
		for (EntryId eid: allFanouts) {
			fanoutRoutineNames.add(eid.getRoutineName());
		}
		ReturnTypeBR rtBR = new ReturnTypeBR();
		Routine[] fanoutRoutines = getSubset(fanoutRoutineNames).toArray(new Routine[0]);
		
		//resource intensive
		BlocksInMap<ReturnType> blocksMap = BlocksInMap.getInstance(rtBR, fanoutRoutines, null); //for every routine that is a fanout, return all the block types
		//resource intensive
		
		//transform results to a list of ToolResult?
		ToolResult resultList = getResult(entries, entryFanoutsMap, blocksMap);	
		if (fr.start()) {
			TerminalFormatter tf = new TerminalFormatter();
			tf.setTab(12);
			resultList.write(fr, tf);
			fr.stop();
		}
		
		//blocksMap.map <String,Blocks> key: Routinename, the routine being a fanout. value: blocks of the routine key
		//--Blocks is a hashmap wrapper of <String,Block<T>> and links to 1 parent Blocks.. the get can query the parent if not found in the present. key: tag, in the routine, Block, of the tag
		//----Block contains an attached object, children - ArrayList<Block> (do blocks), siblings - Blocks<T> (siblings in the routine?), fanouts - ArrayList of IndexedFanout (int routineindex, EntryId)
		
		//TODO: how to remap the original entry tags to their fanouts??? and what if I want to return all the return types for a given routine.
		//all the fanouts are inside the Set 'allFanouts'. This data structure should be remade to map an entry tag to a fanout...
		//or better, use EntryFanouts type (map of sets
		
//		for (String foRoutine : fanoutRoutineNames) {
//			System.out.println(foRoutine);
//			System.out.println(blocksMap.getBlocks(foRoutine).get("lol"));
//		}
		
		//TODO: filter out the tags which were not called: iterate to a routine, was this given tag called? put it into List<Block>
		
		
		
		//--need to know that for the routine and tag we are validating against, which fanouts have what return type
		
		
		//BlocksInMap.map.get(routineName)/*type: Blocks*/.map.get(tag)
		
		//TODO: handle writing results
		
		//TODO: go back make re-usable from eclipse validation
		
		
		
		//major note: can create a blocksupply from a list of routines
		
//		1) get all fanouts of a tag(block) (use EntryFanoutBR ?) (done, using EntryFanoutBR. but need to to limit it to just a tag, not the entire routine)
//		2) visit each fanout's routine (but only the block) (write code to load routine, and send it the new visitor for 
//		checking the tags in a routine, not the entire routine
//		a) use a map <String>(routinename),<Routine>(parsetree) to store all routines. this is just a simple in-memory 	cache.
//		b) visit each routine with a custom BlockRecorder<ReturnStatus>
//		c) extract only the tags which were called, using the filtered set from EntryFanoutBr.getResult() (1) --can a 
//		filter object as seen before speed this up, or filter this after visiting all of the routines blocks.

		//parm: just 1 tag that is called.
		//get results: line location, command start and end pos, return type (invokes EntryFanoutBR, then a custom BlockRecorder<ReturnStatus>
	}

	private ToolResult getResult(List<EntryId> entries,
			Map<EntryId, EntryFanouts> entryFanoutsMap,
			BlocksInMap<ReturnType> blocksMap) {
		
		ReturnTypeAccumulator rta = new ReturnTypeAccumulator(blocksMap, entryFanoutsMap);
		
		for (EntryId paramEntry : entries) {
			rta.addEntry(paramEntry);
		}
	
		return rta.getToolResult();
	}

}
