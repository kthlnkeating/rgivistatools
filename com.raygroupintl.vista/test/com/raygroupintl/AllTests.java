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
	com.raygroupintl.vista.tools.AllTests.class, 
	com.raygroupintl.vista.tools.entryfanin.AllTests.class 
	})
public class AllTests {
}
