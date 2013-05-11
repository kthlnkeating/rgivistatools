package com.raygroupintl.m;

import java.io.InputStream;

import junit.framework.Assert;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.tool.AccumulatingParseTreeAdapter;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.SourceCodeResources;
import com.raygroupintl.m.tool.routine.error.ErrorRecorder;
import com.raygroupintl.m.tool.routine.error.ErrorsByLabel;

public class MTestCommon {	
	public static <T> ParseTreeSupply getParseTreeSupply(String[] routineNames, MVersion version, boolean checkError) {
		int n = routineNames.length;
		String[] resourceNames = new String[n];
		for (int i=0; i<n; ++i) {
			String resourceName = "resource/" + routineNames[i] + ".m";
			resourceNames[i] = resourceName;
		}
		SourceCodeResources<MTestCommon> scr = SourceCodeResources.getInstance(MTestCommon.class, resourceNames);
		ParseTreeSupply pts = new AccumulatingParseTreeAdapter(scr, version);
		if (checkError) for (int i=0; i<routineNames.length; ++i) {
			String routineName = routineNames[i];
			Routine routine = pts.getParseTree(routineName);
			if (checkError) {
				ErrorRecorder er = new ErrorRecorder();
				ErrorsByLabel errors = er.getErrors(routine);
				Assert.assertTrue(errors.isEmpty());
			}
		}
		return pts;
	}
	
	public static <T> ParseTreeSupply getParseTreeSupply(String[] routineNames) {
		return getParseTreeSupply(routineNames, MVersion.CACHE, true);
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
