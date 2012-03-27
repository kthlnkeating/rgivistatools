package com.raygroupintl.vista.mtoken.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TCommandForTest.class, TCommandKillTest.class,
		TCommandLockTest.class, TCommandReadTest.class, TCommandSetTest.class,
		TCommandWriteTest.class, TFActualListTest.class,
		TFDelimitedListTest.class, TFExprItemTest.class, TFExprTest.class,
		TFIndirectionTest.class, TFIntrinsicTest.class, TFNumLitTest.class,
		TFPatternTest.class, TFTest.class, TLineTest0.class, TRoutineTest.class })
public class LocalTests {

}
