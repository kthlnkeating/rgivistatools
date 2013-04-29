package com.raygroupintl.vista.tools;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.FanoutRecorder;
import com.raygroupintl.m.struct.EntryFanoutInfo;
import com.raygroupintl.m.struct.FanoutTypeENUM;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.OutputTerm;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.vista.tools.returntype.ReturnType;
import com.raygroupintl.vista.tools.returntype.ReturnTypeBR;
import com.raygroupintl.vista.tools.returntype.ReturnTypeENUM;
import com.raygroupintl.vista.tools.returntype.ReturnTypeToolResults;
import com.raygroupintl.vista.tools.returntype.RoutineLoader;

public class ReturnTypeTool extends Tool {
	
	private RoutineLoader routineLoader;
	private Terminal outputTerm;
	private FileWrapper fileWrapper;
	private Collection<String> inputRoutineNames;
	
	protected ReturnTypeTool(CLIParams params) {
		super(params);
		
		fileWrapper = getOutputFile();
		inputRoutineNames = new TreeSet<String>( getEntriesInString());
	}
	
	public ReturnTypeTool(Terminal outputTerm, Collection<String> inputRoutineNames) {
		super(null);
		
		this.outputTerm = outputTerm;
		this.inputRoutineNames = inputRoutineNames;
	}	

	//this class should only be responsible for handling invoking the returntype scan for the entries passed in via a param
	
	@Override
	public void run() {
		
		routineLoader = new RoutineLoader(MVersion.CACHE); //TODO: how is this set, via CLI? and how to set it for eclipse too?
		//load the routine parse trees for each entry param into a map String<routineName>,parsetree
		routineLoader.loadRoutines(inputRoutineNames);

		for (String routineName : inputRoutineNames) {
			processRoutine(routineName);
		}
	}

	private void processRoutine(String routineName) {
		
		//1) get all the fanouts from a routine
		FanoutRecorder fanoutRecorder = new FanoutRecorder();
		LinkedList<EntryFanoutInfo> entryFanoutsList = fanoutRecorder.getFanouts2(routineLoader.getRoutine(routineName)); //get all fanouts of a given routine
		
		//need to transform the local tag calls to qualified fanouts
		for (EntryFanoutInfo efi : entryFanoutsList) {
			EntryId fanout = efi.getFanoutTo();
			if (fanout.getRoutineName() == null)
				efi.setFanoutTo(new EntryId(routineName, fanout.getTag()));
		}
		
		//2) lookup/parse each return type for that fanout
		Set<String> fanoutRoutineNames = new HashSet<String>();
		for (EntryFanoutInfo fanout : entryFanoutsList) {
			routineLoader.loadRoutine(fanout.getFanoutTo().getRoutineName()); //lookup
			
			if (fanout.getFanoutTo().getRoutineName() != null)
				fanoutRoutineNames.add(fanout.getFanoutTo().getRoutineName());
		}
		
		ReturnTypeBR rtBR = new ReturnTypeBR();
		Routine[] fanoutRoutines = routineLoader.getSubset(fanoutRoutineNames).toArray(new Routine[0]);
		
		//gather only the return types
		//parse as indicated in (2)
		BlocksInMap<ReturnType> blocksMap = BlocksInMap.getInstance(rtBR, fanoutRoutines); //for every routine that is a fanout, return all the block types
		////blocksMap.map <String,Blocks> key: Routinename, the routine being a fanout. value: blocks of the routine key
		//--Blocks is a hashmap wrapper of <String,Block<T>> and links to 1 parent Blocks.. the get can query the parent if not found in the present. key: tag, in the routine, Block, of the tag
		//----Block contains an attached object, children - ArrayList<Block> (do blocks), siblings - Blocks<T> (siblings in the routine?), fanouts - ArrayList of IndexedFanout (int routineindex, EntryId)		
		
		//note: need to map the 'entryFanoutList' to the 'blocksMap'

		//3) iterate each of the original 'entryFanouts' and see if the fanout is a valid call
		for (EntryFanoutInfo fanout : entryFanoutsList) {
			fanout.setValid(true);
			fanout.setFanoutExists(true);
			Block<ReturnType> blockOfFanout = blocksMap.getBlock(fanout.getFanoutTo());
			if (blockOfFanout == null) {
				fanout.setValid(false);
				fanout.setFanoutExists(false);
				continue;
			}
			fanout.setReturnType(blockOfFanout.getData().getReturnType());
			if (fanout.getFanoutType() == FanoutTypeENUM.DO || fanout.getFanoutType() == FanoutTypeENUM.GOTO) {
				if (blockOfFanout.getData().getReturnType() != ReturnTypeENUM.RETURN_NOTHING) {
					fanout.setValid(false);
				}
			} else if (fanout.getFanoutType() == FanoutTypeENUM.EXTRINSIC) {
				if (blockOfFanout.getData().getReturnType() != ReturnTypeENUM.RETURN_VALUE) {
					fanout.setValid(false);
				}
			}
			//System.out.println(fanout.getLineLocation()+ " : " +fanout.getFanoutTo() + " : " +fanout.getFanoutType() + " : " + fanout.getReturnType() + " : " +fanout.isValid());
		}
		
		ReturnTypeToolResults resultList = new ReturnTypeToolResults(routineName, entryFanoutsList);
		
		if (fileWrapper != null) {
			if (fileWrapper.start()) {
				sendToOut(resultList, fileWrapper);
				fileWrapper.stop();
			}
		}
		
		if (outputTerm != null) {
			sendToOut(resultList, outputTerm);
		}

	}

	private void sendToOut(ReturnTypeToolResults resultList, Terminal term) {
		TerminalFormatter tf = new TerminalFormatter();
		tf.setTab(12);
		resultList.write(term, tf);
	}
	
	public static void main(String[] args) {
		Collection<String> routineNames = new LinkedList<String>();
		routineNames.add("GMPLRPTR");
		Terminal term = new OutputTerm(System.out);
		ReturnTypeTool rtt = new ReturnTypeTool(term, routineNames);
		rtt.run();
	}

}
