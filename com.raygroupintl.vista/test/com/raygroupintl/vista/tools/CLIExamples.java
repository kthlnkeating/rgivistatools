package com.raygroupintl.vista.tools;

import org.junit.Test;

public class CLIExamples {
	//@Test
	public void testFanoutAll() {
		String args[] = {"fanout", "-o", "C:\\Sandbox\\j_fo_all.dat", "-md", "C:\\Users\\Afsin\\git\\M-Tools"};
		MRoutineAnalyzer.main(args);				
	}
	
	//@Test
	public void testFanoutSD() {
		String args[] = {"fanout", "-o", "C:\\Sandbox\\j_fo_sd.dat", "-p", "SD"};
		MRoutineAnalyzer.main(args);				
	}
	
	//@Test
	public void testFanoutGMPL() {
		String args[] = {"fanout", "-o", "C:\\Sandbox\\j_fo_gmpl.dat", "-p", "GMPL"};
		MRoutineAnalyzer.main(args);				
	}
	
	@Test
	public void testFaninAll() {
		String args[] = {"fanin", "-o", "C:\\Sandbox\\j_fi_all.dat", 
				"-md", "C:\\Users\\Afsin\\git\\M-Tools",
				"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGI.m",
				"-mf", "C:\\Users\\Afsin\\git\\VistA-FOIA\\Scripts\\ZGO.m",
				"-pe", "DENTAL RECORD MANAGER"};
		MRoutineAnalyzer.main(args);				
	}

	//@Test
	public void testFaninSD() {
		String args[] = {"fanin", "-o", "C:\\Sandbox\\j_fi_sd.dat", "-p", "SD"};
		MRoutineAnalyzer.main(args);				
	}

    //@Test
	public void testFaninGMPL() {
		String args[] = {"fanin", "-o", "C:\\Sandbox\\j_fi_gmpl.dat", "-p", "GMPL"};
		MRoutineAnalyzer.main(args);				
	}

	//@Test
	public void testCreateParseTreeFiles() {
		// Create files for parse tree
		String args1[] = {
				"-t", "serial",
				"-ptd", "C:\\Sandbox\\serial"};
		MRoutineAnalyzer.main(args1);		
	}
	
	
	/* This prints code quality report for all the tags in Problem List API
	 * routines GMPLAPI*, GMPLSITE, GMPLDAL*, GMPLEXT*.  It assumes environment
	 * variable VistA-FOIA that point to the VistA-FOIA library. It assumes
	 * testCreateParseTreeFiles is run.
	 */
	//@Test
	public void testProblemListAPITags() {
	    // Create entry points for the routines.
		String args0[] = {
				"-t", "entry",
				"-p", "PROBLEM LIST", 
				"-r", "GMPLAPI.*", "-r", "GMPLSITE", "-r", "GMPLDAL.*", "-r", "GMPLEXT.*", 
				"-o", "C:\\Sandbox\\j_gmplapitest_all_in.dat"};
		MRoutineAnalyzer.main(args0);
		
		// Write the API
		String args2[] = {
				"-t", "apis", 
				"-i", "C:\\Sandbox\\j_gmplapitest_all_in.dat", 
				"-o", "C:\\Sandbox\\j_gmplapitest_all.dat",
				"-ptd", "C:\\Sandbox\\serial"};
		MRoutineAnalyzer.main(args2);
	}
	
	//@Test
	public void testGeneric() {
		// Write the API
		String args2[] = {
				"-t", "apis", 
				"-e", "ASK^DIC", 
				"-o", "C:\\Sandbox\\j_test.dat",
				"-ptd", "C:\\Sandbox\\serial"};
		MRoutineAnalyzer.main(args2);
	}
}
