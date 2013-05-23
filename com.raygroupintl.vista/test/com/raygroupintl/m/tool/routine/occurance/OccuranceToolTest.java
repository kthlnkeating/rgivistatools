package com.raygroupintl.m.tool.routine.occurance;

import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.m.MTestCommon;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.ResultsByLabel;
import com.raygroupintl.m.tool.ResultsByRoutine;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;

public class OccuranceToolTest {
	private static ParseTreeSupply pts95;
	private static ParseTreeSupply ptsCache;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String[] routineNames = {"CMDTEST0"};
		pts95 = MTestCommon.getParseTreeSupply(routineNames, MVersion.ANSI_STD_95, false);		
		ptsCache = MTestCommon.getParseTreeSupply(routineNames, MVersion.CACHE, false);		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		pts95 = null;
		ptsCache = null;
	}
		
	private void testCount(ResultsByLabel<Occurance, List<Occurance>> result, int expected, OccuranceType type) {
		int count = 0;
		Set<String> labels = result.getLabels();
		for (String label : labels) {
			List<Occurance> occurances = result.getResults(label);
			for (Occurance occurance : occurances) {
				if (occurance.getType() == type) {
					++count;
				}
			}
		}
		Assert.assertEquals(expected, count);		
	}
	
	private void test(ParseTreeSupply pts) {
		String routineName = "CMDTEST0";
		
		OccuranceToolParams params = new OccuranceToolParams(pts);
		params.clearTypes();
		params.addType(OccuranceType.INDIRECTION);
		params.addType(OccuranceType.GOTO);
		params.addType(OccuranceType.DO);
		params.addType(OccuranceType.EXTRINSIC);
		params.addType(OccuranceType.ATOMIC_DO);
		params.addType(OccuranceType.EXTERNAL_DO);
		params.addType(OccuranceType.ATOMIC_GOTO);
		params.addType(OccuranceType.DO_BLOCK);
		
		OccuranceTool tool = new OccuranceTool(params);
		
		MRoutineToolInput input = new MRoutineToolInput();
		input.addRoutine(routineName);
		
		ResultsByRoutine<Occurance, List<Occurance>> resultRaw = tool.getResult(input);
		ResultsByLabel<Occurance, List<Occurance>> result = resultRaw.getResults(routineName);
		
		testCount(result, 10, OccuranceType.DO_BLOCK);
		testCount(result, 21, OccuranceType.DO);
		testCount(result, 29, OccuranceType.ATOMIC_DO);
		testCount(result, 8, OccuranceType.EXTERNAL_DO);
		testCount(result, 27, OccuranceType.INDIRECTION);
		testCount(result, 17, OccuranceType.GOTO);
		testCount(result, 31, OccuranceType.ATOMIC_GOTO);
		testCount(result, 5, OccuranceType.EXTRINSIC);
	}

	@Test
	public void test() {
		test(ptsCache);
		test(pts95);		
	}
}
