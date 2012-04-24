package com.raygroupintl.vista.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.util.CLIParamMgr;
import com.raygroupintl.util.CLIParameter;

public class CLIParams {
	@CLIParameter
	public List<String> reportContent = new ArrayList<String>();
	
	@CLIParameter(names={"-p", "--package"})
	public List<String> packages = new ArrayList<String>();
	
	@CLIParameter(names={"-o", "--output"})
	public String outputFile;
	 
	@CLIParameter(names={"-t", "--type"})
	public String analysisType = "error";
	
	private static void logError(String msg) {
		Logger logger = Logger.getLogger(MRoutineAnalyzer.class.getName());		
		logger.log(Level.SEVERE, msg);
	}
	
	private static void logError(String msg, Throwable t) {
		Logger logger = Logger.getLogger(MRoutineAnalyzer.class.getName());		
		logger.log(Level.SEVERE, msg, t);
	}
	
	public boolean validate() {
		if (this.outputFile == null) {
			CLIParams.logError("No output file is specified.");
			return false;
		}
		return true;
	}
	
	public static CLIParams getInstance(String[] args) {
		try {
			CLIParams options = CLIParamMgr.parse(CLIParams.class, args);
			if (options.validate()) {
				return options;
			} else {
				return null;
			}
		} catch (Throwable t) {
			CLIParams.logError("Invalid options", t);
			return null;
		}
	}
}

