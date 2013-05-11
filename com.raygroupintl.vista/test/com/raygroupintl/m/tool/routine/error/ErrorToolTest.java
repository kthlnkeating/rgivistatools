package com.raygroupintl.m.tool.routine.error;

import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.m.MTestCommon;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.routine.error.ErrorRecorder;
import com.raygroupintl.m.tool.routine.error.ErrorWithLocation;

public class ErrorToolTest {
	private static ParseTreeSupply pts95;
	private static ParseTreeSupply ptsCache;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String[] routineNames = {"XRGITST0", "CMDTEST0", "ERRTEST0"};
		pts95 = MTestCommon.getParseTreeSupply(routineNames, MVersion.ANSI_STD_95, false);		
		ptsCache = MTestCommon.getParseTreeSupply(routineNames, MVersion.CACHE, false);		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		pts95 = null;
		ptsCache = null;
	}
		
	private ErrorsByLabel getErrors(String routineName, ParseTreeSupply pts) {
		Routine r = pts.getParseTree(routineName);
		ErrorRecorder v = new ErrorRecorder();
		ErrorsByLabel result = v.getErrors(r);
		return result;
	}
	
	public void testNonErrorFiles(ParseTreeSupply pts) {
		String[] routineNames = {"XRGITST0", "CMDTEST0"};
		for (String routineName : routineNames) {
			ErrorsByLabel result = this.getErrors(routineName, pts);
			Assert.assertTrue(result.isEmpty());
		}
	}

	@Test
	public void testNonErrorFiles() {
		testNonErrorFiles(ptsCache);
		testNonErrorFiles(pts95);
	}
	
	private void testErrTest0Error(ErrorWithLocation error, String expectedTag, int expectedOffset) {
		LineLocation location = error.getLocation();
		Assert.assertEquals(expectedTag, location.getTag());
		Assert.assertEquals(expectedOffset, location.getOffset());		
	}
	
	private void testErrTest0(ParseTreeSupply pts) {
		String routineName = "ERRTEST0";
		ErrorsByLabel result = this.getErrors(routineName, pts);
		
		List<ErrorWithLocation> ewl0 = result.getResults("MULTIPLY");		
		Assert.assertEquals(1, ewl0.size());		
		testErrTest0Error(ewl0.get(0), "MULTIPLY", 2);
		
		List<ErrorWithLocation> ewl1 = result.getResults("MAIN");		
		Assert.assertEquals(2, ewl1.size());		
		testErrTest0Error(ewl1.get(0), "MAIN", 3);
		testErrTest0Error(ewl1.get(1), "MAIN", 5);
		
		List<ErrorWithLocation> ewl2 = result.getResults("DOERR");		
		Assert.assertEquals(1, ewl2.size());		
		testErrTest0Error(ewl2.get(0), "DOERR", 2);
		
		List<ErrorWithLocation> ewl3 = result.getResults("DOERR2");		
		Assert.assertEquals(1, ewl3.size());		
		testErrTest0Error(ewl3.get(0), "DOERR2", 4);
	}

	@Test
	public void testErrTest0() {
		testErrTest0(ptsCache);
		testErrTest0(pts95);		
	}
}
