package com.raygroupintl.vista.tools;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.APIData;
import com.raygroupintl.m.parsetree.data.APIDataStore;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.MapBlocksSupply;
import com.raygroupintl.m.parsetree.filter.BasicSourcedFanoutFilter;
import com.raygroupintl.m.parsetree.visitor.APIRecorder;
import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.struct.PassFilter;

public class APITest {
	private static MTFSupply supply;
	private static Map<String, String> replacement = new HashMap<String, String>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		supply = MTFSupply.getInstance(MVersion.CACHE);
		replacement.put("%DTC", "APIROU02");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		supply = null;
	}

	private Routine getRoutineToken(String fileName, MTFSupply m) {
		TFRoutine tf = new TFRoutine(m);
		InputStream is = this.getClass().getResourceAsStream(fileName);
		String fn = (fileName.split(".m")[0]).split("/")[1];
		MRoutineContent content = MRoutineContent.getInstance(fn, is);
		MRoutine r = tf.tokenize(content);
		return r.getNode();
	}
		
	private void usedTest(MapBlocksSupply blocksMap, String routineName, String tag, String[] expectedAssumeds, String[] expectedGlobals) {
		Blocks rbs = blocksMap.get(routineName);
		Block lb = rbs.get(tag);
		APIData apiData = lb.getAPIData(blocksMap, new APIDataStore(), new BasicSourcedFanoutFilter(new PassFilter<EntryId>()), replacement);
		
		Set<String> assumeds = new HashSet<String>(apiData.getAssumed());
		Assert.assertEquals(expectedAssumeds.length, assumeds.size());
		for (String expectedOutput : expectedAssumeds) {
			Assert.assertTrue(assumeds.contains(expectedOutput));			
		}				

		Set<String> globals = new HashSet<String>(apiData.getGlobals());
		Assert.assertEquals(expectedGlobals.length, globals.size());
		for (String expectedGlobal : expectedGlobals) {
			Assert.assertTrue(globals.contains(expectedGlobal));			
		}				
	}
	
	private void filemanTest(MapBlocksSupply blocksMap, String routineName, String tag, String[] expectedGlobals, String[] expectedCalls) {
		Blocks rbs = blocksMap.get(routineName);
		Block lb = rbs.get(tag);
		APIData apiData = lb.getAPIData(blocksMap, new APIDataStore(), new BasicSourcedFanoutFilter(new PassFilter<EntryId>()), replacement);
		
		Set<String> globals = new HashSet<String>(apiData.getFilemanGlobals());
		Assert.assertEquals(expectedGlobals.length, globals.size());
		for (String expectedGlobal : expectedGlobals) {
			Assert.assertTrue(globals.contains(expectedGlobal));			
		}				

		Set<String> calls = new HashSet<String>(apiData.getFilemanCalls());
		Assert.assertEquals(expectedCalls.length, calls.size());
		for (String expectedCall : expectedCalls) {
			Assert.assertTrue(calls.contains(expectedCall));			
		}				
	}
	
	@Test
	public void testError() {
		String[] fileNames = {"resource/APIROU00.m", "resource/APIROU01.m", "resource/APIROU02.m", "resource/APIROU03.m", 
				"resource/DMI.m", "resource/DDI.m", "resource/DIE.m", "resource/FIE.m"};
		Routine[] routines = new Routine[fileNames.length];
		{
			int i = 0;
			for (String fileName : fileNames) {
				routines[i] = this.getRoutineToken(fileName, supply);
				++i;
			}
		}
		ErrorRecorder er = new ErrorRecorder();
		for (int i=0; i<routines.length; ++i) {			
			Routine r = routines[i];
			r.accept(er);
			Assert.assertEquals(0, er.getLastErrors().size());
		}
		APIRecorder recorder = new APIRecorder(null);
		MapBlocksSupply blocksMap = new MapBlocksSupply();
		for (int i=0; i<routines.length; ++i) {			
			routines[i].accept(recorder);
			Blocks blocks = recorder.getBlocks();
			blocksMap.put(routines[i].getName(), blocks);
		}
		this.usedTest(blocksMap, "APIROU00", "FACT", new String[]{"I"}, new String[0]);
		this.usedTest(blocksMap, "APIROU00", "SUM", new String[]{"M", "R", "I"}, new String[]{"^RGI0(\"EF\""});
		this.usedTest(blocksMap, "APIROU00", "SUMFACT", new String[]{"S", "P"}, new String[]{"^RGI0(\"EF\""});
		this.usedTest(blocksMap, "APIROU00", "STORE", new String[]{"K", "D", "R"}, new String[0]);
		this.usedTest(blocksMap, "APIROU00", "STOREG", new String[]{"K", "A", "D", "R"}, new String[0]);
		this.usedTest(blocksMap, "APIROU00", "TOOTHER", new String[]{"I", "M"}, new String[0]);
		this.usedTest(blocksMap, "APIROU00", "TONONE", new String[]{"A", "D", "ME", "NE", "HR"}, new String[0]);
		this.usedTest(blocksMap, "APIROU00", "ZZ", new String[]{"A", "D"}, new String[0]);
		this.usedTest(blocksMap, "APIROU01", "SUMFACT", new String[]{"S", "P"}, new String[]{"^RGI0(\"EF\"", "^UD(", "^UD(5", "^UM"});
		this.usedTest(blocksMap, "APIROU01", "STORE", new String[]{"K", "D", "R"}, new String[0]);
		this.usedTest(blocksMap, "APIROU01", "LOOP", new String[]{"S", "A", "C", "I", "J", "B", "D", "P"}, new String[]{"^RGI0(\"EF\"", "^UD(", "^UD(5", "^UM"});
		this.usedTest(blocksMap, "APIROU03", "GPIND", new String[]{"B", "A"}, new String[0]);
		this.usedTest(blocksMap, "APIROU03", "CALL1", new String[]{"A", "B"}, new String[0]);
		
		this.filemanTest(blocksMap, "APIROU03", "FILEMAN", new String[]{"^DIC(9.4","^DIE(9.5", "^DIK(9.6"}, new String[]{"CHK^DIE(10.1", "CHK^DMI(10.2", "CHK^DDI(10.3"});
		
	}
}
