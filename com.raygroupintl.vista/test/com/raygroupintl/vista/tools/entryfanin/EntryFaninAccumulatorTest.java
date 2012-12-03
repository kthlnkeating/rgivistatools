package com.raygroupintl.vista.tools.entryfanin;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.vista.tools.MRARoutineFactory;

public class EntryFaninAccumulatorTest { 
	private void checkResult(EntryFanins fanins, String faninEntry, String[] faninNextEntries) {
		EntryId faninEntryId = EntryId.getInstance(faninEntry); 
		Assert.assertTrue(fanins.hasFaninEntry(faninEntryId));
		Set<EntryId> s = fanins.getFaninNextEntries(faninEntryId);
		Assert.assertNotNull(s);
		Assert.assertEquals(faninNextEntries.length, s.size());
		for (String faninNextEntry : faninNextEntries) {
			EntryId faninNextEntryId = EntryId.getInstance(faninNextEntry);
			Assert.assertTrue(s.contains(faninNextEntryId));
		}
	}
		
	@Test
	public void test() {
		String[] resourceNames = {"resource/FINROU00.m", 
				                  "resource/FINROU01.m", 
				                  "resource/FINROU02.m", 
				                  "resource/FINROU03.m", 
				                  "resource/FINROU04.m"};
		Routine[] routines = new Routine[resourceNames.length];
		MRARoutineFactory rf = MRARoutineFactory.getInstance(MVersion.CACHE);
		for (int i=0; i<resourceNames.length; ++i) {
			String resourceName = resourceNames[i];
			routines[i] = rf.getRoutineFromResource(EntryFaninAccumulatorTest.class, resourceName);
			ErrorRecorder er = new ErrorRecorder();
			routines[i].accept(er);
			Assert.assertEquals(0, er.getLastErrors().size());
		}
		EntryId entryId = new EntryId("FINROU00", "ADD");
		MarkedAsFaninBR recorder = new MarkedAsFaninBR(entryId);
		Map<String, String> emptyMap = Collections.emptyMap();
		BlocksInMap<FaninMark> blocksMap = BlocksInMap.getInstance(recorder, routines, emptyMap);
		EntryFaninAccumulator accumulator = new EntryFaninAccumulator(entryId, blocksMap);
		for (Routine r : routines) {
			accumulator.addRoutine(r);
		}
		EntryFanins fanins = accumulator.getResult();
		Set<EntryId> s = fanins.getFaninEntries();
		Assert.assertEquals(15, s.size());
		this.checkResult(fanins, "FINROU01^FINROU01", new String[]{":4^FINROU01"});
		this.checkResult(fanins, ":4^FINROU01", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "ADDALL^FINROU01", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "CONDADD^FINROU01", new String[]{"CONDADD2^FINROU01"});
		this.checkResult(fanins, "CONDADD2^FINROU01", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "MULTAALL^FINROU01", new String[]{"MULTADD^FINROU00"});
		this.checkResult(fanins, "ADD^FINROU02", new String[]{"ADDALL^FINROU01"});
		this.checkResult(fanins, "ADD2^FINROU02", new String[]{"ADD^FINROU02"});
		this.checkResult(fanins, "MULTADD^FINROU00", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "FINROU03^FINROU03", new String[]{"TESTINDO^FINROU03"});
		this.checkResult(fanins, "TESTINDO^FINROU03", new String[]{"ADD^FINROU00"});
		this.checkResult(fanins, "FINROU04^FINROU04", new String[]{"TESTINDO^FINROU04"});
		this.checkResult(fanins, "TESTINDO^FINROU04", new String[]{":5^FINROU04"});
		this.checkResult(fanins, ":5^FINROU04", new String[]{"OTHER^FINROU02"});
		this.checkResult(fanins, "OTHER^FINROU02", new String[]{"ADD^FINROU00"});
	}
}
