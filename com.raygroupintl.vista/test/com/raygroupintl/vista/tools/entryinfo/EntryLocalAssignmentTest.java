package com.raygroupintl.vista.tools.entryinfo;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.AccumulatingBlocksSupply;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.vista.tools.AccumulatorTestCommon;
import com.raygroupintl.vista.tools.entry.CodeLocations;
import com.raygroupintl.vista.tools.entry.LocalAssignmentAccumulator;
import com.raygroupintl.vista.tools.entry.LocalAssignmentRecorder;

public class EntryLocalAssignmentTest {
	private void testLocations(CodeLocations r, CodeLocation[] expectedCodeLocations) {
		Assert.assertTrue(r.isIdenticalTo(expectedCodeLocations));
	}
	
	@Test
	public void test() {
		String[] resourceNames = {
				"resource/APIROU00.m", "resource/APIROU01.m", "resource/APIROU02.m", "resource/APIROU03.m", 
				"resource/DMI.m", "resource/DDI.m", "resource/DIE.m", "resource/FIE.m"};
		ParseTreeSupply pts = AccumulatorTestCommon.getParseTreeSupply(EntryCodeInfoToolTest.class, resourceNames);
		final Set<String> locals = new HashSet<String>();
		locals.add("R");
		BlockRecorderFactory<CodeLocations> brf = new BlockRecorderFactory<CodeLocations>() {
			@Override
			public LocalAssignmentRecorder getRecorder() {
				return new LocalAssignmentRecorder(locals);	
			}
		};  
		BlocksSupply<Block<CodeLocations>> blocksSupply = new AccumulatingBlocksSupply<CodeLocations>(pts, brf);
				
		LocalAssignmentAccumulator a = new LocalAssignmentAccumulator(blocksSupply);
				
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
