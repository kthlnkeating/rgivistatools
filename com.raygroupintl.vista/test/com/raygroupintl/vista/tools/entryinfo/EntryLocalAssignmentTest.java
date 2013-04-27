package com.raygroupintl.vista.tools.entryinfo;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.RecursionDepth;
import com.raygroupintl.m.tool.RecursionSpecification;
import com.raygroupintl.m.tool.basiccodeinfo.CodeLocations;
import com.raygroupintl.m.tool.localassignment.LocalAssignmentTool;
import com.raygroupintl.m.tool.localassignment.LocalAssignmentToolParams;
import com.raygroupintl.vista.tools.AccumulatorTestCommon;

public class EntryLocalAssignmentTest {
	private void testLocations(CodeLocations r, CodeLocation[] expectedCodeLocations) {
		Assert.assertTrue(r.isIdenticalTo(expectedCodeLocations));
	}
	
	@Test
	public void test() {
		String[] resourceNames = {
				"resource/APIROU00.m", "resource/APIROU01.m", "resource/APIROU02.m", "resource/APIROU03.m", 
				"resource/DMI.m", "resource/DDI.m", "resource/DIE.m", "resource/FIE.m"};
		ParseTreeSupply pts = AccumulatorTestCommon.getParseTreeSupply(EntryLocalAssignmentTest.class, resourceNames);
		LocalAssignmentToolParams p = new LocalAssignmentToolParams(pts);		
		RecursionSpecification rs = new RecursionSpecification();
		rs.setDepth(RecursionDepth.ALL);
		p.setRecursionSpecification(rs);
		p.addLocal("R");

		LocalAssignmentTool a = new LocalAssignmentTool(p);
				
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
