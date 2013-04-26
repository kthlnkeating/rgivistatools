package com.raygroupintl.vista.tools;

import junit.framework.Assert;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.SourceCodeResources;
import com.raygroupintl.m.tool.SourceCodeToParseTreeAdapter;

public class AccumulatorTestCommon {
	public static <T> ParseTreeSupply getParseTreeSupply(Class<T> cls, String[] resourceNames) {
		SourceCodeResources<T> scr = SourceCodeResources.getInstance(cls, resourceNames);
		ParseTreeSupply pts = new SourceCodeToParseTreeAdapter(scr);
		for (int i=0; i<resourceNames.length; ++i) {
			String resourceName = resourceNames[i];
			String routineName = resourceName.split("/")[1].split(".m")[0];
			Routine routine = pts.getParseTree(routineName);
			ErrorRecorder er = new ErrorRecorder();
			routine.accept(er);
			Assert.assertEquals(0, er.getLastErrors().size());
		}
		return pts;
	}
}
