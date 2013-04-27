package com.raygroupintl.vista.tools.entryinfo;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.RecursionDepth;
import com.raygroupintl.m.tool.RecursionSpecification;
import com.raygroupintl.m.tool.fanout.EntryFanouts;
import com.raygroupintl.m.tool.fanout.FanoutTool;
import com.raygroupintl.vista.tools.AccumulatorTestCommon;

public class EntryFanoutToolTest {
	private void testFanouts(EntryFanouts r, EntryId[] expectedFanouts) {
		Set<EntryId> fanouts = r.getFanouts();
		Assert.assertEquals(expectedFanouts.length, fanouts == null ? 0 : fanouts.size());
		for (EntryId expectedFanout : expectedFanouts) {
			Assert.assertTrue(fanouts.contains(expectedFanout));			
		}				
	}
	
	@Test
	public void test() {
		String[] resourceNames = {
				"resource/APIROU00.m", "resource/APIROU01.m", "resource/APIROU02.m", "resource/APIROU03.m", 
				"resource/DMI.m", "resource/DDI.m", "resource/DIE.m", "resource/FIE.m"};
		ParseTreeSupply pts = AccumulatorTestCommon.getParseTreeSupply(EntryCodeInfoToolTest.class, resourceNames);
		
		CommonToolParams p = new CommonToolParams(pts);		
		RecursionSpecification rs = new RecursionSpecification();
		rs.setDepth(RecursionDepth.ALL);
		p.setRecursionSpecification(rs);

		FanoutTool a = new FanoutTool(p);
				
		this.testFanouts(a.getResult(new EntryId("APIROU00", "FACT")), new EntryId[0]);
		this.testFanouts(a.getResult(new EntryId("APIROU00", "SUM")), new EntryId[0]);
		this.testFanouts(a.getResult(new EntryId("APIROU01", "STORE")), new EntryId[]{new EntryId("APIROU00", "FACT"), new EntryId("APIROU00", "STOREG")});
		this.testFanouts(a.getResult(new EntryId("APIROU01", "LOOP")), new EntryId[]{new EntryId("APIROU01", "SUMFACT"), new EntryId("APIROU01", "LATER"), new EntryId("APIROU00", "FACT"), new EntryId("APIROU00", "SUM")});
	}
}
