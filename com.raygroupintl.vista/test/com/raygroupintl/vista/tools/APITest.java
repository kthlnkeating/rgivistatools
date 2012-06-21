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
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.APIRecorder;
import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;

public class APITest {
	private static MTFSupply supply;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		supply = MTFSupply.getInstance(MVersion.CACHE);
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
	
	@Test
	public void testError() {
		String[] fileNames = {"resource/APIROU00.m", "resource/APIROU01.m"};
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
		APIRecorder recorder = new APIRecorder();
		Map<String, Blocks> blocksMap = new HashMap<String, Blocks>();
		for (int i=0; i<routines.length; ++i) {			
			routines[i].accept(recorder);
			Blocks blocks = recorder.getBlocks();
			blocksMap.put(routines[i].getName(), blocks);
		}
		Blocks rbs = blocksMap.get("APIROU01");
		Block lb = rbs.get("STORE");
		Set<EntryId> entryIdTrack = new HashSet<EntryId>();
		Set<String> inputs = lb.getUseds(blocksMap, entryIdTrack);
		Assert.assertEquals(3, inputs.size());
		String[] expectedInputs = {"D", "K", "R"};
		for (String expectedInput : expectedInputs) {
			Assert.assertTrue(inputs.contains(expectedInput));
			
		}
	}
}
