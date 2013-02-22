package com.raygroupintl.vista.tools.entryinfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.vista.tools.AccumulatorTestCommon;
import com.raygroupintl.vista.tools.entry.CodeLocations;
import com.raygroupintl.vista.tools.entry.EntryCodeLocations;
import com.raygroupintl.vista.tools.entry.LocalAssignmentAccumulator;
import com.raygroupintl.vista.tools.entry.LocalAssignmentRecorder;

public class EntryLocalAssignmentTest {
	private void testLocations(EntryCodeLocations r, CodeLocation[] expectedCodeLocations) {
		CodeLocations cls = r.getCodeLocations();
		Assert.assertTrue(cls.isIdenticalTo(expectedCodeLocations));
	}
	
	@Test
	public void test() {
		String[] resourceNames = {
				"resource/APIROU00.m", "resource/APIROU01.m", "resource/APIROU02.m", "resource/APIROU03.m", 
				"resource/DMI.m", "resource/DDI.m", "resource/DIE.m", "resource/FIE.m"};
		Routine[] routines = AccumulatorTestCommon.getRoutines(EntryCodeInfoToolTest.class, resourceNames);
		
		Set<String> locals = new HashSet<String>();
		locals.add("R");
		LocalAssignmentRecorder recorder = new LocalAssignmentRecorder(locals);
		Map<String, String> replacement = new HashMap<String, String>();
		replacement.put("%DTC", "APIROU02");
		BlocksInMap<CodeLocations> blocksMap = BlocksInMap.getInstance(recorder, routines, replacement);
		
		LocalAssignmentAccumulator a = new LocalAssignmentAccumulator(blocksMap);
				
		CodeLocation[] expectedCodeLocations = new CodeLocation[]{
				new CodeLocation("APIROU01", "SUMFACT", 4),
				new CodeLocation("APIROU00", "FACT", 2),
				new CodeLocation("APIROU00", "FACT", 3),
				new CodeLocation("APIROU00", "SUM", 1),
				new CodeLocation("APIROU00", "SUM", 2),
					};
		this.testLocations(a.getResult(new EntryId("APIROU01", "SUMFACT")), expectedCodeLocations);
	}
}
