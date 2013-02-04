package com.raygroupintl.vista.tools.entryinfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.vista.tools.AccumulatorTestCommon;
import com.raygroupintl.vista.tools.entryinfo.CodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoAccumulator;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoRecorder;

public class EntryCodeInfoToolTest {
	private void testAssumedLocal(EntryCodeInfo r, String[] expectedAssumeds, String[] expectedGlobals) {
		Set<String> assumeds = r.assumedLocals;
		Assert.assertEquals(expectedAssumeds.length, assumeds.size());
		for (String expectedOutput : expectedAssumeds) {
			Assert.assertTrue(assumeds.contains(expectedOutput));			
		}				

		Set<String> globals = new HashSet<String>(r.otherCodeInfo.getGlobals());
		Assert.assertEquals(expectedGlobals.length, globals.size());
		for (String expectedGlobal : expectedGlobals) {
			Assert.assertTrue(globals.contains(expectedGlobal));			
		}				
	}
	
	private void filemanTest(EntryCodeInfo r, String[] expectedGlobals, String[] expectedCalls) {
		Set<String> globals = new HashSet<String>(r.otherCodeInfo.getFilemanGlobals());
		Assert.assertEquals(expectedGlobals.length, globals.size());
		for (String expectedGlobal : expectedGlobals) {
			Assert.assertTrue(globals.contains(expectedGlobal));			
		}				

		Set<String> calls = new HashSet<String>(r.otherCodeInfo.getFilemanCalls());
		Assert.assertEquals(expectedCalls.length, calls.size());
		for (String expectedCall : expectedCalls) {
			Assert.assertTrue(calls.contains(expectedCall));			
		}				
	}
	
	@Test
	public void testAssumedLocals() {
		String[] resourceNames = {
				"resource/APIROU00.m", "resource/APIROU01.m", "resource/APIROU02.m", "resource/APIROU03.m", 
				"resource/DMI.m", "resource/DDI.m", "resource/DIE.m", "resource/FIE.m"};
		Routine[] routines = AccumulatorTestCommon.getRoutines(EntryCodeInfoToolTest.class, resourceNames);
		
		EntryCodeInfoRecorder recorder = new EntryCodeInfoRecorder(null);
		Map<String, String> replacement = new HashMap<String, String>();
		replacement.put("%DTC", "APIROU02");
		BlocksInMap<CodeInfo> blocksMap = BlocksInMap.getInstance(recorder, routines, replacement);
		
		EntryCodeInfoAccumulator a = new EntryCodeInfoAccumulator(blocksMap);
				
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "FACT")), new String[]{"I"}, new String[0]);
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "SUM")), new String[]{"M", "R", "I"}, new String[]{"^RGI0(\"EF\""});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "SUMFACT")), new String[]{"S", "P"}, new String[]{"^RGI0(\"EF\""});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "STORE")), new String[]{"K", "D", "R"}, new String[0]);
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "STOREG")), new String[]{"K", "A", "D", "R"}, new String[0]);
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "TOOTHER")), new String[]{"I", "M"}, new String[0]);
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "TONONE")), new String[]{"A", "D", "ME", "NE", "HR"}, new String[0]);
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "ZZ")), new String[]{"A", "D"}, new String[0]);
		this.testAssumedLocal(a.getResult(new EntryId("APIROU01", "SUMFACT")), new String[]{"S", "P"}, new String[]{"^RGI0(\"EF\"", "^UD(", "^UD(5", "^UM"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU01", "STORE")), new String[]{"K", "D", "R"}, new String[0]);
		this.testAssumedLocal(a.getResult(new EntryId("APIROU01", "LOOP")), new String[]{"S", "A", "C", "I", "J", "B", "D", "P"}, new String[]{"^RGI0(\"EF\"", "^UD(", "^UD(5", "^UM"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU03", "GPIND")), new String[]{"B", "A"}, new String[0]);
		this.testAssumedLocal(a.getResult(new EntryId("APIROU03", "CALL1")), new String[]{"A", "B"}, new String[0]);
		this.filemanTest(a.getResult(new EntryId("APIROU03", "FILEMAN")), new String[]{"^DIC(9.4","^DIE(9.5", "^DIK(9.6"}, new String[]{"CHK^DIE(10.1", "CHK^DMI(10.2", "CHK^DDI(10.3"});				
	}
}
