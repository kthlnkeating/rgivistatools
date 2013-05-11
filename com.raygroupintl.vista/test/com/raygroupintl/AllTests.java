package com.raygroupintl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	com.raygroupintl.parsergen.ruledef.AllTests.class, 
	com.raygroupintl.m.token.AllTests.class, 
	com.raygroupintl.vista.repository.AllTests.class, 
	com.raygroupintl.m.struct.AllTests.class,
	com.raygroupintl.m.tool.entry.assumedvariables.AllTests.class,
	com.raygroupintl.m.tool.entry.localassignment.AllTests.class,
	com.raygroupintl.m.tool.entry.fanout.AllTests.class,	
	com.raygroupintl.m.tool.entry.basiccodeinfo.AllTests.class,	
	com.raygroupintl.m.tool.entry.quittype.AllTests.class,	
	com.raygroupintl.m.tool.routine.error.AllTests.class,	
	com.raygroupintl.vista.tools.AllTests.class, 
	com.raygroupintl.vista.tools.entry.AllTests.class, 
	com.raygroupintl.m.tool.entry.fanin.AllTests.class 
	})
public class AllTests {
}
