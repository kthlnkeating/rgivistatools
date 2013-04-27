package com.raygroupintl.vista.tools.entryinfo;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.RecursionDepth;
import com.raygroupintl.m.tool.RecursionSpecification;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesToolParams;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.vista.tools.AccumulatorTestCommon;

public class EntryAssumedVarTest {
	private void testAssumedLocal(AssumedVariables r, String[] expectedAssumeds) {
		Set<String> assumeds = r.toSet();
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
		ParseTreeSupply pts = AccumulatorTestCommon.getParseTreeSupply(EntryCodeInfoToolTest.class, resourceNames);		
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
	}
}