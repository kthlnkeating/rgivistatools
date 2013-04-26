package com.raygroupintl.vista.tools.entryinfo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EntryAssumedVarTest.class, EntryBasicCodeInfoToolTest.class,
		EntryCodeInfoToolTest.class, EntryFanoutToolTest.class,
		EntryLocalAssignmentTest.class })
public class AllTests {

}