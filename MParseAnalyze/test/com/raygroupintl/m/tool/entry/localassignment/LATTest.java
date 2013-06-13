package com.raygroupintl.m.tool.entry.localassignment;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.MTestCommon;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.RecursionDepth;
import com.raygroupintl.m.tool.entry.RecursionSpecification;
import com.raygroupintl.m.tool.entry.basiccodeinfo.CodeLocations;
import com.raygroupintl.m.tool.entry.localassignment.LocalAssignmentTool;
import com.raygroupintl.m.tool.entry.localassignment.LocalAssignmentToolParams;

public class LATTest {
	private void testLocations(CodeLocations r, CodeLocation[] expectedCodeLocations) {
		Assert.assertTrue(r.isIdenticalTo(expectedCodeLocations));
	}
	
	@Test
	public void test() {
		String[] routineNames = {
				"APIROU00", "APIROU01", "APIROU02", "APIROU03", 
				"DMI", "DDI", "DIE", "FIE"};
		ParseTreeSupply pts = MTestCommon.getParseTreeSupply(routineNames);
		LocalAssignmentToolParams p = new LocalAssignmentToolParams(pts);		
		RecursionSpecification rs = new RecursionSpecification();
		rs.setDepth(RecursionDepth.ALL);
		p.setRecursionSpecification(rs);
		p.addLocal("R");

		LocalAssignmentTool a = new LocalAssignmentTool(p);
				
		CodeLocation[] expectedCodeLocations = new CodeLocation[]{
				new CodeLocation("APIROU01", 9),
				new CodeLocation("APIROU00", 7),
				new CodeLocation("APIROU00", 8),
				new CodeLocation("APIROU00", 12),
				new CodeLocation("APIROU00", 13),
					};
		this.testLocations(a.getResult(new EntryId("APIROU01", "SUMFACT")), expectedCodeLocations);
	}
}
