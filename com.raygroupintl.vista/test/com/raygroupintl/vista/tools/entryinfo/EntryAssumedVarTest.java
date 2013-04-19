package com.raygroupintl.vista.tools.entryinfo;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.filter.ExcludeAllFanoutFilter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.tools.AccumulatorTestCommon;
import com.raygroupintl.vista.tools.entryinfo.CodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoRecorder;

public class EntryAssumedVarTest {
	private void testAssumedLocal(AssumedVariablesTR r, String[] expectedAssumeds) {
		Set<String> assumeds = r.getData();
		Assert.assertEquals(expectedAssumeds.length, assumeds.size());
		for (String expectedOutput : expectedAssumeds) {
			Assert.assertTrue(assumeds.contains(expectedOutput));			
		}				
	}
	
	@Test
	public void testAssumedLocals() {
		String[] resourceNames = {
				"resource/APIROU00.m", "resource/APIROU01.m", "resource/APIROU02.m", "resource/APIROU03.m", 
				"resource/APIROU04.m", "resource/DMI.m", "resource/DDI.m", "resource/DIE.m", "resource/FIE.m"};
		Routine[] routines = AccumulatorTestCommon.getRoutines(EntryCodeInfoToolTest.class, resourceNames);
		
		EntryCodeInfoRecorder recorder = new EntryCodeInfoRecorder(null);
		BlocksInMap<CodeInfo> blocksMap = BlocksInMap.getInstance(recorder, routines);
		
		AssumedVariableAccumulator a = new AssumedVariableAccumulator(blocksMap);
				
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "FACT")), new String[]{"I"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "SUM")), new String[]{"M", "R", "I"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "SUMFACT")), new String[]{"S", "P"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "STORE")), new String[]{"K", "D", "R"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "STOREG")), new String[]{"K", "A", "D", "R"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "TOOTHER")), new String[]{"I", "M"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "TONONE")), new String[]{"A", "D", "ME", "NE", "HR"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU00", "ZZ")), new String[]{"A", "D"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU01", "SUMFACT")), new String[]{"S", "P"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU01", "STORE")), new String[]{"K", "D", "R"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU01", "LOOP")), new String[]{"S", "A", "C", "I", "J", "B", "D", "P"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU03", "GPIND")), new String[]{"B", "A"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU03", "CALL1")), new String[]{"A", "B"});
		
		FilterFactory<EntryId, EntryId> filterFactory = new FilterFactory<EntryId, EntryId>() {
			@Override
			public Filter<EntryId> getFilter(EntryId parameter) {
				return new ExcludeAllFanoutFilter();
			}
		};
		AssumedVariableAccumulator a2 = new AssumedVariableAccumulator(blocksMap, filterFactory);
		this.testAssumedLocal(a2.getResult(new EntryId("APIROU04", "INDOBLK")), new String[]{"I", "Y"});		
		
		AssumedVarsToolFlag flags = new AssumedVarsToolFlag();
		flags.addExpectedAssumeVariable("I");
		AssumedVariableAccumulator a3 = new AssumedVariableAccumulator(blocksMap, filterFactory, flags);		
		this.testAssumedLocal(a3.getResult(new EntryId("APIROU04", "INDOBLK")), new String[]{"Y"});		
	}
}