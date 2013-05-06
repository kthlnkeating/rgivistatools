package com.raygroupintl.m.tool.entry.quittype;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.m.MTestCommon;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.entry.RecursionDepth;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesToolParams;

public class QTTTest {
	private static ParseTreeSupply pts;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String[] routineNames = {"QTTTEST0", "QTTTEST1"};
		pts = MTestCommon.getParseTreeSupply(routineNames);		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		pts = null;
	}

	private static void testCodeLocation(CodeLocation underTest, String routineName, String label, int offset) {
		LineLocation ll = new LineLocation(label, offset);
		CodeLocation cl = new CodeLocation(routineName, ll);
		Assert.assertEquals(underTest, cl);					
	}
	
	@Test
	public void testQTTTest0() {
		CommonToolParams p = new AssumedVariablesToolParams(pts);
		p.getRecursionSpecification().setDepth(RecursionDepth.ALL);
		QuitTypeTool qtt = new QuitTypeTool(p);
		
		EntryId id = new EntryId("QTTTEST0","SUMXYZ");
		QuitType qt = qtt.getResult(id);
		
		QuitTypeState qts = qt.getQuitTypeState();
		Assert.assertEquals(QuitTypeState.CONFLICTING_QUITS, qts);			
		CodeLocation fql = qt.getFirstQuitLocation();
		testCodeLocation(fql, "QTTTEST0", "SUMC", 3);
		CodeLocation cql = qt.getConflictingLocation();
		testCodeLocation(cql, "QTTTEST0", "SUMY", 1);
		
		CallType ct = qt.getFanout(new EntryId("QTTTEST1", "SUMZ"));
		Assert.assertNotNull(ct);
		Assert.assertEquals(CallTypeState.DO_CONFLICTING, ct.getState());
	}
}
