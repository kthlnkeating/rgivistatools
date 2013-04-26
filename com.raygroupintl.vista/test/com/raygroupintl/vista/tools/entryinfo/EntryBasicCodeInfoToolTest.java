package com.raygroupintl.vista.tools.entryinfo;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.AccumulatingBlocksSupply;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.vista.tools.AccumulatorTestCommon;
import com.raygroupintl.vista.tools.entryinfo.CodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoRecorder;

public class EntryBasicCodeInfoToolTest {
	private void testExpectedGlobal(BasicCodeInfoTR r, String[] expectedGlobals) {
		Set<String> globals = new HashSet<String>(r.getData().getGlobals());
		Assert.assertEquals(expectedGlobals.length, globals.size());
		for (String expectedGlobal : expectedGlobals) {
			Assert.assertTrue(globals.contains(expectedGlobal));			
		}				
	}
	
	private void filemanTest(BasicCodeInfoTR r, String[] expectedGlobals, String[] expectedCalls) {
		Set<String> globals = new HashSet<String>(r.getData().getFilemanGlobals());
		Assert.assertEquals(expectedGlobals.length, globals.size());
		for (String expectedGlobal : expectedGlobals) {
			Assert.assertTrue(globals.contains(expectedGlobal));			
		}				

		Set<String> calls = new HashSet<String>(r.getData().getFilemanCalls());
		Assert.assertEquals(expectedCalls.length, calls.size());
		for (String expectedCall : expectedCalls) {
			Assert.assertTrue(calls.contains(expectedCall));			
		}				
	}
	
	@Test
	public void testExpectedGlobals() {
		String[] resourceNames = {
				"resource/APIROU00.m", "resource/APIROU01.m", "resource/APIROU02.m", "resource/APIROU03.m", 
				"resource/DMI.m", "resource/DDI.m", "resource/DIE.m", "resource/FIE.m"};
		ParseTreeSupply pts = AccumulatorTestCommon.getParseTreeSupply(EntryCodeInfoToolTest.class, resourceNames);
		BlockRecorderFactory<CodeInfo> brf = new BlockRecorderFactory<CodeInfo>() {
			@Override
			public EntryCodeInfoRecorder getRecorder() {
				return new EntryCodeInfoRecorder(null);	
			}
		};  
		BlocksSupply<Block<CodeInfo>> blocksSupply = new AccumulatingBlocksSupply<CodeInfo>(pts, brf);
		
		BasicCodeInfoAccumulator a = new BasicCodeInfoAccumulator(blocksSupply);
				
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU00", "FACT")), new String[0]);
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU00", "SUM")), new String[]{"^RGI0(\"EF\""});
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU00", "SUMFACT")), new String[]{"^RGI0(\"EF\""});
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU00", "STORE")), new String[0]);
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU00", "STOREG")), new String[0]);
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU00", "TOOTHER")), new String[0]);
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU00", "TONONE")), new String[0]);
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU00", "ZZ")), new String[0]);
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU01", "SUMFACT")), new String[]{"^RGI0(\"EF\"", "^UD(", "^UD(5", "^UM"});
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU01", "STORE")), new String[0]);
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU01", "LOOP")), new String[]{"^RGI0(\"EF\"", "^UD(", "^UD(5", "^UM"});
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU03", "GPIND")), new String[0]);
		this.testExpectedGlobal(a.getResult(new EntryId("APIROU03", "CALL1")), new String[0]);
		this.filemanTest(a.getResult(new EntryId("APIROU03", "FILEMAN")), new String[]{"^DIC(9.4","^DIE(9.5", "^DIK(9.6"}, new String[]{"CHK^DIE(10.1", "CHK^DMI(10.2", "CHK^DDI(10.3"});				
	}
}