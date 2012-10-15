package com.raygroupintl.parsergen.ruledef;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GrammarErrorTest.class, GrammarTest.class,
		TFDelimitedListTest.class })
public class AllTests {

}
