package com.raygroupintl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	com.raygroupintl.parser.AllTests.class, 
	com.raygroupintl.parser.annotation.AllTests.class, 
	com.raygroupintl.m.token.AllTests.class, 
	com.raygroupintl.vista.repository.AllTests.class, 
	com.raygroupintl.m.struct.AllTests.class, 
	com.raygroupintl.vista.tools.AllTests.class 
	})
public class AllTests {
}
