package com.raygroupintl.vista.tools;

import junit.framework.Assert;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.token.MVersion;

public class AccumulatorTestCommon {
	public static Routine[] getRoutines(Class<?> cls, String[] resourceNames) {
		Routine[] routines = new Routine[resourceNames.length];
		MRARoutineFactory rf = MRARoutineFactory.getInstance(MVersion.CACHE);
		for (int i=0; i<resourceNames.length; ++i) {
			String resourceName = resourceNames[i];
			routines[i] = rf.getRoutineFromResource(cls, resourceName);
			ErrorRecorder er = new ErrorRecorder();
			routines[i].accept(er);
			Assert.assertEquals(0, er.getLastErrors().size());
		}
		return routines;
	}
}
