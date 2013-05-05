package com.raygroupintl.m;

import java.io.InputStream;

import junit.framework.Assert;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.tool.AccumulatingParseTreeAdapter;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.SourceCodeResources;

public class MTestCommon {	
	public static <T> ParseTreeSupply getParseTreeSupply(String[] routineNames) {
		int n = routineNames.length;
		String[] resourceNames = new String[n];
		for (int i=0; i<n; ++i) {
			String resourceName = "resource/" + routineNames[i] + ".m";
			resourceNames[i] = resourceName;
		}
		SourceCodeResources<MTestCommon> scr = SourceCodeResources.getInstance(MTestCommon.class, resourceNames);
		ParseTreeSupply pts = new AccumulatingParseTreeAdapter(scr);
		for (int i=0; i<routineNames.length; ++i) {
			String routineName = routineNames[i];
			Routine routine = pts.getParseTree(routineName);
			ErrorRecorder er = new ErrorRecorder();
			routine.accept(er);
			Assert.assertEquals(0, er.getLastErrors().size());
		}
		return pts;
	}
	
	public static MRoutine getRoutineToken(String routineName, MTFSupply m) {
		TFRoutine tf = new TFRoutine(m);
		String resourceName = "resource/" + routineName + ".m";
		InputStream is = MTestCommon.class.getResourceAsStream(resourceName);
		MRoutineContent content = MRoutineContent.getInstance(routineName, is);
		MRoutine r = tf.tokenize(content);
		return r;
	}
}
