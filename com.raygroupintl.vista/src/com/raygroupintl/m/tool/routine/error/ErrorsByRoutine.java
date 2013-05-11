package com.raygroupintl.m.tool.routine.error;

import java.util.List;

import com.raygroupintl.m.tool.ResultsByLabel;
import com.raygroupintl.m.tool.ResultsByRoutine;

public class ErrorsByRoutine extends ResultsByRoutine<ErrorWithLocation, List<ErrorWithLocation>> {
	@Override
	public ResultsByLabel<ErrorWithLocation, List<ErrorWithLocation>> getNewResultsInstance() {
		return new ErrorsByLabel();
	}
}
