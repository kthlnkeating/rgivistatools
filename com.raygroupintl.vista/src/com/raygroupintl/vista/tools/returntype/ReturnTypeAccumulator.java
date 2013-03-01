package com.raygroupintl.vista.tools.returntype;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.vista.tools.entryfanout.EntryFanouts;
import com.raygroupintl.vista.tools.fnds.ToolResult;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class ReturnTypeAccumulator {

	private BlocksInMap<ReturnType> blocksMap; //prefer an already created (parsed) data, faster for transforming.
	private Map<EntryId, EntryFanouts> entryFanoutsMap;
	private ToolResultCollection<ReturnTypeResult> results = new ToolResultCollection<ReturnTypeResult>();

	public ReturnTypeAccumulator(BlocksInMap<ReturnType> blocksMap, Map<EntryId, EntryFanouts> entryFanoutsMap) {
		this.blocksMap = blocksMap;
		this.entryFanoutsMap = entryFanoutsMap;
	}

	public void addEntry(EntryId entryParam) { //adds an entry for a single tag (ie: a single cli parameter) //TODO: make this reusable, add a parm that can accumulate for routine names
		List<ReturnTypeForFanout> fanoutReturnTypes = new ArrayList<ReturnTypeForFanout>();
		ReturnTypeResult toolResult = new ReturnTypeResult(entryParam, fanoutReturnTypes);
		
		//for (EntryId eidKey: entryFanoutsMap.keySet()) { //iterate each param entry tag
			//System.out.println(eidKey);
			EntryFanouts entryFanouts = entryFanoutsMap.get(entryParam); //all the fanouts for that param
			//System.out.println(eidKey);
			for (EntryId fanout : entryFanouts.getFanouts()) {
				if (fanout.getRoutineName() == null) {
					fanout = new EntryId(entryFanouts.getEntry().getRoutineName(), fanout.getTag());
				}
				//System.out.println("-- " +fanout+ ". fanout block: " +blocksMap.getBlock(fanout).getAttachedObject().getReturnType());
				fanoutReturnTypes.add(new ReturnTypeForFanout(fanout, blocksMap.getBlock(fanout).getAttachedObject().getReturnType()));
			}
		//}

		results.add(toolResult);
	}

	public ToolResult getToolResult() {
		return results;
	}

}
