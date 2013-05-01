package com.raygroupintl.m.tool.entry.assumedvariables;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.MTestCommon;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.RecursionDepth;
import com.raygroupintl.m.tool.entry.RecursionSpecification;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesToolParams;

public class AVTTest {
	private void testAssumedLocal(AssumedVariables r, String[] expectedAssumeds) {
		Set<String> assumeds = r.toSet();
		Assert.assertEquals(expectedAssumeds.length, assumeds.size());
		for (String expectedOutput : expectedAssumeds) {
			Assert.assertTrue(assumeds.contains(expectedOutput));			
		}				
	}
	
	@Test
	public void testAssumedLocals() {
		String[] routineNames = {"APIROU00", "APIROU01", "APIROU02", "APIROU03", 
								 "APIROU04", "DMI", "DDI", "DIE", "FIE"};
		ParseTreeSupply pts = MTestCommon.getParseTreeSupply(routineNames);		
		AssumedVariablesToolParams p = new AssumedVariablesToolParams(pts);		
		RecursionSpecification rs = new RecursionSpecification();
		rs.setDepth(RecursionDepth.ALL);
		p.setRecursionSpecification(rs);
		AssumedVariablesTool a = new AssumedVariablesTool(p);
		
		
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
		this.testAssumedLocal(a.getResult(new EntryId("APIROU03", "NEWFOLVL")), new String[]{"V1"});
		this.testAssumedLocal(a.getResult(new EntryId("APIROU03", "NEWDOLVL")), new String[]{"B"});
		
		AssumedVariablesToolParams p2 = new AssumedVariablesToolParams(pts);		
		AssumedVariablesTool a2 = new AssumedVariablesTool(p2);
		this.testAssumedLocal(a2.getResult(new EntryId("APIROU04", "INDOBLK")), new String[]{"I", "Y"});		
		
		AssumedVariablesToolParams p3 = new AssumedVariablesToolParams(pts);
		p3.addExpected("I");
		AssumedVariablesTool a3 = new AssumedVariablesTool(p3);		
		this.testAssumedLocal(a3.getResult(new EntryId("APIROU04", "INDOBLK")), new String[]{"Y"});	
		
		AssumedVariablesToolParams p4 = new AssumedVariablesToolParams(pts);		
		AssumedVariablesTool a4 = new AssumedVariablesTool(p4);
		this.testAssumedLocal(a4.getResult(new EntryId("APIROU04", "ASSUMEV2")), new String[]{"I", "M"});		
		
		AssumedVariablesToolParams p5 = new AssumedVariablesToolParams(pts);		
		RecursionSpecification rs5 = new RecursionSpecification();
		rs5.setDepth(RecursionDepth.ENTRY);
		p5.setRecursionSpecification(rs5);
		AssumedVariablesTool a5 = new AssumedVariablesTool(p5);		
		this.testAssumedLocal(a5.getResult(new EntryId("APIROU04", "ASSUMEV2")), new String[]{"I", "M", "V3"});		
		
		AssumedVariablesToolParams p6 = new AssumedVariablesToolParams(pts);		
		RecursionSpecification rs6 = new RecursionSpecification();
		rs6.setDepth(RecursionDepth.ROUTINE);
		p6.setRecursionSpecification(rs6);
		AssumedVariablesTool a6 = new AssumedVariablesTool(p6);		
		this.testAssumedLocal(a6.getResult(new EntryId("APIROU04", "ASSUMEV2")), new String[]{"I", "M", "V1", "V3"});				
	}
}